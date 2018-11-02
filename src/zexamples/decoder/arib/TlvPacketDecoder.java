package zexamples.decoder.arib;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sedec2.arib.tlv.TlvMfuExtractor.IMediaExtractorListener;
import sedec2.arib.tlv.TlvMfuExtractor;
import sedec2.arib.tlv.TlvTableExtractor;
import sedec2.arib.tlv.TlvTableExtractor.ITableExtractorListener;
import sedec2.arib.tlv.mmt.si.tables.MMT_PackageTable;
import sedec2.arib.tlv.mmt.si.tables.MMT_PackageTable.Asset;
import sedec2.arib.tlv.mmt.si.tables.PackageListTable;
import sedec2.base.Table;

class TlvCoordinator implements ITableExtractorListener, IMediaExtractorListener {
    protected MMT_PackageTable mpt = null;
    protected PackageListTable plt = null;
    protected TlvTableExtractor tlv_table_extractor = new TlvTableExtractor();
    protected TlvMfuExtractor tlv_mpu_extractor = new TlvMfuExtractor();
    
    FileOutputStream video_fs = null;
    BufferedOutputStream video_bs = null;
    
    FileOutputStream audio_fs = null;
    BufferedOutputStream audio_bs = null;

    FileOutputStream data_fs = null;
    BufferedOutputStream data_bs = null;

    public TlvCoordinator() throws FileNotFoundException {
        video_fs = new FileOutputStream(new File("video.mfu.hevc"));
        video_bs = new BufferedOutputStream(video_fs);
        
        audio_fs = new FileOutputStream(new File("audio.mfu.aac"));
        audio_bs = new BufferedOutputStream(audio_fs);

        data_fs = new FileOutputStream(new File("data.mfu"));
        data_bs = new BufferedOutputStream(data_fs);

        List<Byte> filters = new ArrayList<>();
        filters.add(sedec2.arib.tlv.mmt.si.TableFactory.MPT);
        filters.add(sedec2.arib.tlv.mmt.si.TableFactory.PLT);
        tlv_table_extractor.enableTableFilter(filters);
        
        /* @note Option Enable all of filters which you want to receive */
//        tlv_extractor.enableAllOfTableFilter();
        
        /* @note Option Disable all of filters which you don't want to receive */
//        tlv_extractor.disableTableFilter();
        
        /* @note Option Enable NTP data if user want to receive */
//        tlv_extractor.disableNtpFilter();

        tlv_table_extractor.addEventListener(this);
        tlv_mpu_extractor.addEventListener(this);
    }
    
    public void destroy() {
        tlv_mpu_extractor.removeEventListener(this);
        tlv_table_extractor.removeEventListener(this);
        
        tlv_mpu_extractor = null;
        tlv_table_extractor = null;
    }
    
    public boolean putTlvForTable(byte[] tlv_raw) {
        return tlv_table_extractor.put(tlv_raw);
    }
    
    public boolean putTlvForMedia(byte[] tlv_raw) {
        return tlv_mpu_extractor.put(tlv_raw);
    }
    
    @Override
    public void onReceivedTable(Table table) {
        if ( table.getTableId() == sedec2.arib.tlv.mmt.si.TableFactory.MPT ) {
            if ( mpt == null ) {
                mpt = (MMT_PackageTable) table;
                List<Asset> assets = mpt.getAssets();
                for ( int i=0; i<assets.size(); i++) {
                    Asset asset = assets.get(i);
                    String asset_type = new String(asset.asset_type);                            
                    int pid = ((asset.asset_id_byte[0] & 0xff) << 8 | asset.asset_id_byte[1]);
                    switch ( asset_type ) {
                        case "hev1":
                        case "hvc1":
                            tlv_mpu_extractor.setVideoPidFilter(Arrays.asList(pid));
                            break;
                        case "mp4a":
                            tlv_mpu_extractor.setAudioPidFilter(Arrays.asList(pid));
                            break;
                        case "stpp":
                            tlv_mpu_extractor.setTimedTextPidFilter(Arrays.asList(pid));
                            break;
                        case "aapp":
                            tlv_mpu_extractor.setApplicationPidFilter(Arrays.asList(pid));
                            break;
                        case "asgd":
                            break;
                        case "aagd":
                            break;
                    }
                }
            }
        } else if (table.getTableId() == sedec2.arib.tlv.mmt.si.TableFactory.PLT ) {
            if ( plt == null  ) {
                plt = (PackageListTable) table;                        
            } else if ( plt != null && plt.getVersion() != 
                    ((PackageListTable)table).getVersion() ) {
                plt = (PackageListTable) table;
                mpt = null;
            }
            
            if ( plt != null && mpt != null ) {
//                plt.print();
//                mpt.print();
//                System.exit(1);
            }
        }                
    }

    @Override
    public void onReceivedVideo(int packet_id, byte[] buffer) {
        try {
            video_bs.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceivedAudio(int packet_id, byte[] buffer) {
        try {
            audio_bs.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

    @Override
    public void onReceivedTimedText(int packet_id, byte[] buffer) {
        try {
            data_bs.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
}

/**
 * TlvPacketDecoder is an example for getting 
 * - Tables which are include in TLV-SI of TLV, MMT-SI of MMTP packet \n
 * - NTP which is included in IPv4, IPv6 packet of TLV as NetworkTimeProtocolData \n
 * - MPU, MFU to be used for media which is included in MMTP Packet \n 
 */
public class TlvPacketDecoder {
    public static void main(String []args) throws FileNotFoundException {
        if ( args.length < 1 ) {
            System.out.println("Oops, " + 
                    "I need TLV packet to be parsed as 1st parameter");
            System.out.println(
                    "Usage: java -classpath . " +
                    "zexamples.decoder.TlvPacketDecoder " + 
                    "{TLV Raw File} \n");
        }
        
        TlvCoordinator tlv_coordinator = new TlvCoordinator();
        
        /**
         * @note Assume.1 Getting each one TLV packet from specific file.
         * It assume that platform should give a TLV packet to us as input of TLVExtractor
         */
        for ( int i=0; i<args.length; i++ ) {
            File inOutFile = new File(args[i]);
            
            try {
                DataInputStream dataInputStream  = 
                        new DataInputStream(
                                new BufferedInputStream(
                                        new FileInputStream(inOutFile)));
                final long COUNT_OF_SAMPLES = 10000000;    
                final int TLV_HEADER_LENGTH = 4;
                long sample_counter = 0;
                
                while ( dataInputStream.available() > 0) {
                    /**
                     * @note Assume.2 Making a packet of TLV 
                     */
                    byte[] tlv_header_buffer = new byte[(int) TLV_HEADER_LENGTH];
                    dataInputStream.read(tlv_header_buffer, 0, tlv_header_buffer.length);  
                    
                    byte[] tlv_payload_buffer = 
                            new byte[((tlv_header_buffer[2] & 0xff) << 8 | (tlv_header_buffer[3] & 0xff))];
                    dataInputStream.read(tlv_payload_buffer, 0, tlv_payload_buffer.length);

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    outputStream.write(tlv_header_buffer);
                    outputStream.write(tlv_payload_buffer);
                    
                    /**
                     * @note Step.3 Putting a TLV packet into TLVExtractor \n
                     * and you can wait for both the results of TLV as table of MPEG2 and MFU
                     */
                    if ( false == 
                            tlv_coordinator.putTlvForTable(outputStream.toByteArray()) ||
                         false == 
                            tlv_coordinator.putTlvForMedia(outputStream.toByteArray())  ) {
                        System.out.println("Oops, they have problem to decode TLV ");
                        break;
                    }
                    outputStream = null;
                    Thread.sleep(1);
                    
                    if ( sample_counter++ > COUNT_OF_SAMPLES ) {
                        System.out.print(String.format(
                                "TLV Packet counter is over %d \n", COUNT_OF_SAMPLES));
                        break;
                    }
                }
                
                dataInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        
        /**
         * @note Destroy of TLVExtractor to not handle and released by garbage collector
         */
        tlv_coordinator.destroy();
        tlv_coordinator = null;
        
        System.out.println("ByeBye");
    }
}
