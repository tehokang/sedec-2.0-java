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
import java.util.List;

import sedec2.arib.extractor.TlvDemultiplexer;
import sedec2.arib.tlv.container.packets.NetworkTimeProtocolData;
import sedec2.arib.tlv.mmt.mmtp.mfu.MFU_ClosedCaption;
import sedec2.arib.tlv.mmt.mmtp.mfu.MFU_GeneralPurposeData;
import sedec2.arib.tlv.mmt.mmtp.mfu.MFU_IndexItem;
import sedec2.arib.tlv.mmt.si.tables.MMT_PackageTable;
import sedec2.arib.tlv.mmt.si.tables.MMT_PackageTable.Asset;
import sedec2.arib.tlv.mmt.si.tables.PackageListTable;
import sedec2.base.Table;

class TlvCoordinator implements TlvDemultiplexer.Listener {
    protected MMT_PackageTable mpt = null;
    protected PackageListTable plt = null;
    protected BufferedOutputStream video_bs = null;
    protected BufferedOutputStream audio_bs = null;
    protected TlvDemultiplexer tlv_demuxer = null;
    
    public TlvCoordinator() throws FileNotFoundException {
        video_bs = new BufferedOutputStream(new FileOutputStream(new File("video.mfu.hevc")));
        audio_bs = new BufferedOutputStream(new FileOutputStream(new File("audio.mfu.aac")));

        tlv_demuxer = new TlvDemultiplexer();
        tlv_demuxer.addEventListener(this);
        
        tlv_demuxer.enableSiFilter();
        tlv_demuxer.enableTtmlFilter();
        tlv_demuxer.enableAudioFilter();
        tlv_demuxer.enableVideoFilter();
        tlv_demuxer.enableApplicationFilter();
        tlv_demuxer.enableGeneralDataFilter();
        
//        tlv_demuxer.enableSiLogging();
        tlv_demuxer.enableTtmlLogging();
//        tlv_demuxer.enableAudioLogging();
//        tlv_demuxer.enableVideoLogging();
//        tlv_demuxer.enableApplicationLogging();
//        tlv_demuxer.enableGeneralDataLogging();
        
//        tlv_demuxer.addSiAllFilter();
        tlv_demuxer.addSiFilter(sedec2.arib.tlv.mmt.si.TableFactory.MPT);
        tlv_demuxer.addSiFilter(sedec2.arib.tlv.mmt.si.TableFactory.PLT);
    }
    
    public void destroy() {
        tlv_demuxer.removeEventListener(this);
        tlv_demuxer.destroy();
        tlv_demuxer = null;
    }
    
    public boolean put(byte[] tlv_raw) {
        return tlv_demuxer.put(tlv_raw);
    }
    
    @Override
    public void onReceivedTable(Table table) {
        if ( table.getTableId() == sedec2.arib.tlv.mmt.si.TableFactory.MPT ) {
            if ( mpt == null ) {
                mpt = (MMT_PackageTable) table;
                mpt.print();
                List<Asset> assets = mpt.getAssets();
                for ( int i=0; i<assets.size(); i++) {
                    Asset asset = assets.get(i);
                    String asset_type = new String(asset.asset_type);                            
                    int pid = ((asset.asset_id_byte[0] & 0xff) << 8 | asset.asset_id_byte[1]);
                    switch ( asset_type ) {
                        case "hev1":
                        case "hvc1":
                            /**
                             * @note Video
                             */
                            tlv_demuxer.addVideoPidFilter(pid);
                            break;
                        case "mp4a":
                            /**
                             * @note Audio
                             */
                            tlv_demuxer.addAudioPidFilter(pid);
                            break;
                        case "stpp":
                            /**
                             * @note TTML
                             */
                            tlv_demuxer.addTtmlPidFilter(pid);
                            break;
                        case "aapp":
                            /**
                             * @note Application
                             */
                            tlv_demuxer.addApplicationPidFilter(pid);
                            break;
                        case "asgd":
                            /**
                             * @note Synchronous type general purpose data
                             */
                            tlv_demuxer.addGeneralPurposeDataPidFilter(pid);
                            break;
                        case "aagd":
                            /**
                             * @note Asynchronous type general purpose data
                             */
                            tlv_demuxer.addGeneralPurposeDataPidFilter(pid);
                            break;
                    }
                }
            }
        } else if (table.getTableId() == sedec2.arib.tlv.mmt.si.TableFactory.PLT ) {
            if ( plt == null  ) {
                plt = (PackageListTable) table;  
                plt.print();
            } else if ( plt != null && plt.getVersion() != 
                    ((PackageListTable)table).getVersion() ) {
                plt = (PackageListTable) table;
                mpt = null;
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
    public void onReceivedTtml(int packet_id, byte[] buffer) {
        MFU_ClosedCaption ttml = new MFU_ClosedCaption(buffer);
        ttml.print();
    }

    @Override
    public void onReceivedNtp(NetworkTimeProtocolData ntp) {
        ntp.print();
    }

    @Override
    public void onReceivedApplication(int packet_id, byte[] buffer) {
//        MFU_IndexItem data = new MFU_IndexItem(buffer);
//        data.print();
    }

    @Override
    public void onReceivedGeneralData(int packet_id, byte[] buffer) {
        MFU_GeneralPurposeData data = new MFU_GeneralPurposeData(buffer);
        data.print();
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
                     * @note Assume.2 Making a packet of TLV which has a sync byte as beginning
                     * In other words, user should put a perfect TLV packet with sync byte into. 
                     */
                    byte[] tlv_header_buffer = new byte[(int) TLV_HEADER_LENGTH];
                    dataInputStream.read(tlv_header_buffer, 0, tlv_header_buffer.length);  
                    
                    byte[] tlv_payload_buffer = 
                            new byte[((tlv_header_buffer[2] & 0xff) << 8 | (tlv_header_buffer[3] & 0xff))];
                    dataInputStream.read(tlv_payload_buffer, 0, tlv_payload_buffer.length);

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    outputStream.write(tlv_header_buffer);
                    outputStream.write(tlv_payload_buffer);
                    
                    byte[] tlv_raw = outputStream.toByteArray();
                    /**
                     * @note Step.3 Putting a TLV packet into TLVExtractor \n
                     * and you can wait for both the results of TLV as table of MPEG2 and MFU
                     */
                    if ( false == tlv_coordinator.put(tlv_raw) ) {
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
        System.exit(0);
    }
}
