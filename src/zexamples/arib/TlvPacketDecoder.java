package zexamples.arib;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sedec2.arib.extractor.TlvDemultiplexer;
import sedec2.arib.tlv.container.packets.NetworkTimeProtocolData;
import sedec2.arib.tlv.mmt.mmtp.mfu.MFU_ClosedCaption;
import sedec2.arib.tlv.mmt.mmtp.mfu.MFU_GeneralPurposeData;
import sedec2.arib.tlv.mmt.mmtp.mfu.MFU_IndexItem;
import sedec2.arib.tlv.mmt.si.tables.DataAssetManagementTable;
import sedec2.arib.tlv.mmt.si.tables.DataContentConfigurationTable;
import sedec2.arib.tlv.mmt.si.tables.DataDirectoryManagementTable;
import sedec2.arib.tlv.mmt.si.tables.MMT_PackageTable;
import sedec2.arib.tlv.mmt.si.tables.MMT_PackageTable.Asset;
import sedec2.arib.tlv.mmt.si.tables.PackageListTable;
import sedec2.base.Table;
import sedec2.util.FileUtility;
import sedec2.util.Logger;

class SimpleTlvCoordinator implements TlvDemultiplexer.Listener {
    protected TlvDemultiplexer tlv_demuxer = null;

    /**
     * @note Tables to extract from TLV
     */
    protected MMT_PackageTable mpt = null;
    protected PackageListTable plt = null;
    protected DataAssetManagementTable damt = null;
    protected DataContentConfigurationTable dcct = null;
    protected DataDirectoryManagementTable ddmt = null;

    /**
     * @note Video, Audio, IndexItem of Application to extract from TLV
     */
    protected BufferedOutputStream video_bs = null;
    protected Map<Integer, BufferedOutputStream> audio_bs_map = new HashMap<>();
    protected Map<Integer, MFU_IndexItem.Item> application_items = new HashMap<>();
    
    public SimpleTlvCoordinator() throws FileNotFoundException {
        tlv_demuxer = new TlvDemultiplexer();
        tlv_demuxer.addEventListener(this);
        
        tlv_demuxer.enableSiFilter();
        tlv_demuxer.enableNtpFilter();
        tlv_demuxer.enableTtmlFilter();
        tlv_demuxer.enableAudioFilter();
        tlv_demuxer.enableVideoFilter();
        tlv_demuxer.enableApplicationFilter();
        tlv_demuxer.enableGeneralDataFilter();
        
//        tlv_demuxer.enableSiLogging();
//        tlv_demuxer.enableNtpLogging();
//        tlv_demuxer.enableTtmlLogging();
//        tlv_demuxer.enableAudioLogging();
//        tlv_demuxer.enableVideoLogging();
//        tlv_demuxer.enableApplicationLogging();
//        tlv_demuxer.enableGeneralDataLogging();
        
//        tlv_demuxer.addSiAllFilter();
        tlv_demuxer.addSiFilter(sedec2.arib.tlv.mmt.si.TableFactory.MPT);
        tlv_demuxer.addSiFilter(sedec2.arib.tlv.mmt.si.TableFactory.PLT);
        tlv_demuxer.addSiFilter(sedec2.arib.tlv.mmt.si.TableFactory.DDMT);
        tlv_demuxer.addSiFilter(sedec2.arib.tlv.mmt.si.TableFactory.DCMT);
        tlv_demuxer.addSiFilter(sedec2.arib.tlv.mmt.si.TableFactory.DAMT);
        tlv_demuxer.addSiFilter(sedec2.arib.tlv.mmt.si.TableFactory.MH_AIT);
    }
    
    public void destroy() throws IOException {
        tlv_demuxer.removeEventListener(this);
        tlv_demuxer.destroy();
        tlv_demuxer = null;
        
        video_bs.close();
        video_bs = null;
        
        audio_bs_map.clear();
        audio_bs_map = null;
        
        application_items.clear();
        application_items = null;
    }
    
    public boolean put(byte[] tlv_raw) {
        return tlv_demuxer.put(tlv_raw);
    }
    
    @Override
    public void onReceivedTable(Table table) {
        switch ( table.getTableId() ) {
        case sedec2.arib.tlv.mmt.si.TableFactory.MH_AIT:
//            table.print();
            break;
        case sedec2.arib.tlv.mmt.si.TableFactory.DDMT: 
            ddmt = (DataDirectoryManagementTable) table;
//            ddmt.print();
            break;
        case sedec2.arib.tlv.mmt.si.TableFactory.DAMT:
            damt = (DataAssetManagementTable) table;
//            damt.print();
            break;
        case sedec2.arib.tlv.mmt.si.TableFactory.DCMT:
            dcct = (DataContentConfigurationTable) table;
//            dcct.print();
            break;
        case sedec2.arib.tlv.mmt.si.TableFactory.MPT:
            if ( mpt == null ) {
                mpt = (MMT_PackageTable) table;
                mpt.print();
                List<Asset> assets = mpt.getAssets();
                for ( int i=0; i<assets.size(); i++) {
                    Asset asset = assets.get(i);
                    String asset_type = new String(asset.asset_type);                            
                    int pid = asset.getAssetId();
                    switch ( asset_type ) {
                        case "hev1":
                            /**
                             * @note Video including VPS, SPS and PPS in MFU
                             */
                            tlv_demuxer.addVideoPidFilter(pid);
                            break;
                        case "hvc1":
                            /**
                             * @note Video without VPS, SPS and PPS in MFU
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
            break;
        case sedec2.arib.tlv.mmt.si.TableFactory.PLT:
            if ( plt == null  ) {
                plt = (PackageListTable) table;  
//                plt.print();
            } else if ( plt != null && plt.getVersion() != 
                    ((PackageListTable)table).getVersion() ) {
                plt = (PackageListTable) table;
                mpt = null;
            }
            break;
        }                
    }

    @Override
    public void onReceivedVideo(int packet_id, byte[] buffer) {
        try {
            if ( video_bs == null ) {
                video_bs = new BufferedOutputStream(new FileOutputStream(
                        new File(String.format("video.mfu.0x%04x.hevc", packet_id))));
            }
            video_bs.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceivedAudio(int packet_id, byte[] buffer) {
        try {
            if ( audio_bs_map.containsKey(packet_id) == false ) {
                audio_bs_map.put(packet_id, new BufferedOutputStream(new FileOutputStream(
                        new File(String.format("audio.mfu.0x%04x.aac",packet_id)))));
            }
            
            BufferedOutputStream audio_bs = audio_bs_map.get(packet_id);
            audio_bs.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

    @Override
    public void onReceivedTtml(int packet_id, byte[] buffer) {
        MFU_ClosedCaption ttml = new MFU_ClosedCaption(buffer);
        switch ( ttml.getDataType() ) {
            case 0x00:
                Logger.d("\t [TTHML-DOC] \n");
                Logger.d(String.format("%s \n", new String(ttml.getDataByte())));
                break;
            case 0x01:
                Logger.d("\t [TTHML-PNG] \n");
                FileUtility.save(
                        String.format("ttml.mfu.0x%x.png", packet_id), buffer);
                break;
            case 0x02:
                Logger.d("\t [TTHML-SVG] \n");
                FileUtility.save(
                        String.format("ttml.mfu.0x%x.svg", packet_id), buffer);
                break;
            case 0x03:
                Logger.d("\t [TTHML-PCM] \n");
                FileUtility.save(
                        String.format("ttml.mfu.0x%x.pcm", packet_id), buffer);
                break;
            case 0x04:
                Logger.d("\t [TTHML-MP3] \n");
                FileUtility.save(
                        String.format("ttml.mfu.0x%x.mp3", packet_id), buffer);
                break;
            case 0x05:
                Logger.d("\t [TTHML-AAC] \n");
                FileUtility.save(
                        String.format("ttml.mfu.0x%x.aac", packet_id), buffer);
                break;
            case 0x06:
                Logger.d("\t [TTHML-FONT-SVG] \n");
                FileUtility.save(
                        String.format("ttml.mfu.0x%x.font.svg", packet_id), buffer);
                break;
            case 0x07:
                Logger.d("\t [TTHML-FONT-WOFF] \n");
                FileUtility.save(
                        String.format("ttml.mfu.0x%x.font.woff", packet_id), buffer);
                break;
            default:
                break;
        }
    }

    @Override
    public void onReceivedNtp(NetworkTimeProtocolData ntp) {
//        ntp.print();
    }

    @Override
    public void onReceivedApplication(int packet_id, int item_id, 
            int mpu_sequence_number, byte[] buffer) {
        /**
         * @todo File Processing
         */
//        System.out.println(String.format("[APP] packet_id : 0x%x, item_id : 0x%x, " +
//                "mpu_sequence_number : 0x%x", packet_id, item_id, mpu_sequence_number));
    }

    @Override
    public void onReceivedIndexItem(int packet_id, byte[] buffer) {
        MFU_IndexItem index_item = new MFU_IndexItem(buffer);
//        index_item.print();
        List<MFU_IndexItem.Item> items = index_item.getItems();
        for ( int i=0; i<items.size(); i++ ) {
            MFU_IndexItem.Item item = items.get(i);
            if ( application_items.containsKey(item.item_id) == false ) {
                application_items.put(item.item_id, item);
            }
        }
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
    public static void main(String []args) throws IOException {
        if ( args.length < 1 ) {
            System.out.println("Oops, " + 
                    "I need TLV packet to be parsed as 1st parameter");
            System.out.println(
                    "Usage: java -classpath . " +
                    "zexamples.decoder.TlvPacketDecoder " + 
                    "{TLV Raw File} \n");
        }
        
        SimpleTlvCoordinator tlv_coordinator = new SimpleTlvCoordinator();
        
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
                double file_size = dataInputStream.available();
                double read_size = 0;
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
                    
                    /**
                     * @note From here it's decoration to check counter and progress on console
                     */
                    if ( sample_counter++ > COUNT_OF_SAMPLES ) {
                        System.out.print(String.format(
                                "TLV Packet counter is over %d \n", COUNT_OF_SAMPLES));
                        break;
                    }
                    
                    read_size += tlv_raw.length;
                    double process_percentage = (double)(read_size / file_size) * 100;
                    System.out.print("\033[1;31m" + 
                            String.format("Processing : %.2f %% \r", process_percentage) + 
                            "\u001B[0m");
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
