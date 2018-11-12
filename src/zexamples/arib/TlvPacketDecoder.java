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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import sedec2.arib.extractor.TlvDemultiplexer;
import sedec2.arib.tlv.container.packets.NetworkTimeProtocolData;
import sedec2.arib.tlv.mmt.mmtp.mfu.MFU_ClosedCaption;
import sedec2.arib.tlv.mmt.mmtp.mfu.MFU_GeneralPurposeData;
import sedec2.arib.tlv.mmt.mmtp.mfu.MFU_IndexItem;
import sedec2.arib.tlv.mmt.mmtp.mfu.MFU_IndexItem.Item;
import sedec2.arib.tlv.mmt.si.DescriptorFactory;
import sedec2.arib.tlv.mmt.si.descriptors.Descriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MPU_NodeDescriptor;
import sedec2.arib.tlv.mmt.si.tables.DataAssetManagementTable;
import sedec2.arib.tlv.mmt.si.tables.DataAssetManagementTable.MPU;
import sedec2.arib.tlv.mmt.si.tables.DataContentConfigurationTable;
import sedec2.arib.tlv.mmt.si.tables.DataDirectoryManagementTable;
import sedec2.arib.tlv.mmt.si.tables.MMT_PackageTable;
import sedec2.arib.tlv.mmt.si.tables.MMT_PackageTable.Asset;
import sedec2.arib.tlv.mmt.si.tables.PackageListTable;
import sedec2.base.Table;
import sedec2.util.FileUtility;
import sedec2.util.Logger;
import zexamples.arib.Application.SubDirectory;

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

    protected List<Application> applications = new ArrayList<>();
    /**
     * @note Video, Audio, IndexItem of Application to extract from TLV
     */
    protected BufferedOutputStream video_bs = null;
    protected Map<Integer, BufferedOutputStream> audio_bs_map = new HashMap<>();
    
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
        
        /**
         * @note To add NAL prefix of Video Sample
         */
        tlv_demuxer.enableVideoPreModification();
        
        /**
         * @note To add Sync-Word prefix of Audio Sample
         */
        tlv_demuxer.enableAudioPreModification();
        
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
            boolean found_app = false;
            for ( int i=0; i<applications.size(); i++ ) {
                if ( applications.get(i).base_directory_path.contains(
                        new String(ddmt.getBaseDirectoryPath())) == true ) {
                    found_app = true;
                    break;
                }
            }
            
            if ( found_app == false ) {
                Application app = new Application();
                app.base_directory_path = new String(ddmt.getBaseDirectoryPath());
                for ( int i=0; i<ddmt.getDirectoryNodes().size(); i++ ) {
                    SubDirectory sub_directory = app.new SubDirectory();
                    sub_directory.node_tag = ddmt.getDirectoryNodes().get(i).node_tag;
                    sub_directory.sub_directory_path = 
                            new String(ddmt.getDirectoryNodes().get(i).directory_node_path_byte);
                    app.sub_directories.add(sub_directory);
                }
                applications.add(app);
            }
            break;
        case sedec2.arib.tlv.mmt.si.TableFactory.DAMT:
            damt = (DataAssetManagementTable) table;
//            damt.print();
            
            for ( int i=0; i<damt.getMPUs().size(); i++ ) {
                MPU mpu = damt.getMPUs().get(i);
                for ( int j=0; j<mpu.mpu_info_byte.size(); j++ ) {
                    Descriptor desc = mpu.mpu_info_byte.get(j);
                    if ( desc.getDescriptorTag() == DescriptorFactory.MPU_NODE_DESCRIPTOR ) {
                        for ( int k=0; k<applications.size(); k++ ) {
                            Application app = applications.get(k);
                            for ( int n=0; n<app.sub_directories.size(); n++ ) {
                                SubDirectory sub_directory = app.sub_directories.get(n);
                                if ( ((MPU_NodeDescriptor)desc).getNodeTag()
                                        == sub_directory.node_tag ) {
                                    sub_directory.mpu_sequence_number = mpu.mpu_sequence_number;
                                }
                            }
                        }
                    }
                }
            }
            break;
        case sedec2.arib.tlv.mmt.si.TableFactory.DCMT:
            dcct = (DataContentConfigurationTable) table;
//            dcct.print();
            break;
        case sedec2.arib.tlv.mmt.si.TableFactory.MPT:
            if ( mpt == null ) {
                mpt = (MMT_PackageTable) table;
//                mpt.print();
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
        for ( int i=0; i<applications.size(); i++ ) {
            Application app = applications.get(i);
            for ( int j=0; j<app.sub_directories.size(); j++ ) {
                SubDirectory sub_directory = app.sub_directories.get(j);
                if ( sub_directory.mpu_sequence_number == mpu_sequence_number ) {
                    for ( int k=0; k<sub_directory.files.size(); k++ ) {
                        Application.File file = sub_directory.files.get(k);
                        if ( file.item_id == item_id && file.read_completed == false ) {
                            System.out.println(String.format("Read Completed : 0x%x",
                                    file.item_id));
                            file.buffer = Arrays.copyOfRange(buffer, 0, buffer.length);
                            file.read_completed = true;
                        }
                    }
                }
            }
        }
        
        for ( int i=0; i<applications.size(); i++ ) {
            Application app = applications.get(i);
            app.done();
        }
    }

    @Override
    public void onReceivedIndexItem(int packet_id, int item_id, 
            int mpu_sequence_number, byte[] buffer) {
        MFU_IndexItem index_item = new MFU_IndexItem(buffer);
//        index_item.print();
        
        for ( int i=0; i<applications.size(); i++ ) {
            Application app = applications.get(i);
            for ( int j=0; j<app.sub_directories.size(); j++ ) {
                SubDirectory sub_directory = app.sub_directories.get(j);
                if ( sub_directory.mpu_sequence_number == mpu_sequence_number ) {
                    boolean exist = false;
                    for ( int k=0; k<index_item.getItems().size(); k++ ) {
                        Item item = index_item.getItems().get(k);
                        for ( int n=0; n<sub_directory.files.size(); n++ ) {
                            Application.File file = sub_directory.files.get(n);
                            if ( file.item_id == item.item_id ) { 
                                exist = true;
                                break;
                            }
                        }
                        
                        if ( exist == false ) {
                            Application.File file = app.new File();
                            file.item_id = item.item_id;
                            file.item_size = item.item_size;
                            file.file_name = new String(item.file_name_byte);
                            file.original_size = item.original_size;
                            file.compression_type = item.compression_type;
                            file.item_version = item.item_version;
                            file.mime_type = new String(item.item_type_byte);
                            file.read_completed = false;
                            file.write_completed = false;
                            sub_directory.files.add(file);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onReceivedGeneralData(int packet_id, byte[] buffer) {
        MFU_GeneralPurposeData data = new MFU_GeneralPurposeData(buffer);
        data.print();
    }
}

class ConsoleProgress {
    protected static final int PROGRESS_BAR_WIDTH=30;
    protected static long counter = 0;
    protected static long startTime = 0;
    protected static long processTime = 0;
    protected static double read_size = 0;
    protected static double total_size = 0;
    protected static double bitrate_average = 0;
    protected static StringBuilder anim_progress_bar;
    protected static char[] anim_circle = new char[]{'|', '/', '-', '\\'};
    
    public static void start(double file_size) {
        total_size = file_size;
        startTime = System.currentTimeMillis();
        processTime = System.currentTimeMillis();
    }
    
    public static void stop() {
        total_size = 0;
    }
    
    protected static String formatInterval(final long l) {
        final long hr = TimeUnit.MILLISECONDS.toHours(l);
        final long min = TimeUnit.MILLISECONDS.toMinutes(l - TimeUnit.HOURS.toMillis(hr));
        final long sec = TimeUnit.MILLISECONDS.toSeconds(l - 
                TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min));
        final long ms = TimeUnit.MILLISECONDS.toMillis(l - TimeUnit.HOURS.toMillis(hr) - 
                TimeUnit.MINUTES.toMillis(min) - TimeUnit.SECONDS.toMillis(sec));
        return String.format("%02d:%02d:%02d.%03d", hr, min, sec, ms);
    }
    
    protected static String getProgressBar(double percent) {
        int bars = (int)(percent * PROGRESS_BAR_WIDTH / 100);
        anim_progress_bar = new StringBuilder(PROGRESS_BAR_WIDTH);
        anim_progress_bar.append("[");
        for ( int i=0; i<bars; i++ ) {
            anim_progress_bar.append("=");
        }
        for ( int i=PROGRESS_BAR_WIDTH-bars; i>0; i-- ) {
            anim_progress_bar.append(" ");
        }
        anim_progress_bar.append("]");
        return anim_progress_bar.toString();
    }
    
    public static void update(int read) {
        counter+=1;
        read_size += read;

        /**
         * @note TLV counter
         */
        System.out.print(String.format(" TLV : %d ", counter));
        
        /**
         * @note Progress bar as percentage
         */
        System.out.print(String.format("%s ", 
                getProgressBar((double)(read_size / total_size) * 100)));

        /**
         * @note Percentage of processing while demuxing
         */
        System.out.print("\033[1;31m" + 
                String.format("%.2f %% ", (double)(read_size / total_size) * 100) + "\u001B[0m");
        
        /**
         * @note Circle animation of progressing 
         */
        System.out.print(anim_circle[(int) (counter%4)] + " "); 
        
        /**
         * @note Bitrate of processing as Mbps
         */
        bitrate_average += (((double) (1000 * read ) / 
                (double)((System.currentTimeMillis()-processTime) )) * 
                8) / 1024 / 1024;
        System.out.print(String.format("%4.2fMbps ", bitrate_average/counter)); 
        processTime = System.currentTimeMillis();
        
        /**
         * @note Total amount of processed
         */
        System.out.print(String.format("(%.2f / %.2f MBytes) ", 
                (double)(read_size/1024/1024),
                (double) total_size/1024/1024));
        
        /**
         * @note Duration time during demuxing
         */
        System.out.print(String.format("%s \r", 
                formatInterval(System.currentTimeMillis()-startTime)));
    }
}

class TlvFileReader {
    protected File tlv_file = null;
    protected final int TLV_HEADER_LENGTH = 4;
    protected DataInputStream input_stream  = null;
    
    public TlvFileReader(String tlv_file) {
        this.tlv_file = new File(tlv_file);
    }
    
    public boolean open() {
        try {
            input_stream  = 
                    new DataInputStream(
                            new BufferedInputStream(new FileInputStream(tlv_file)));
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }
    
    public void close() throws IOException {
        if ( input_stream != null ) input_stream.close();
    }
    
    public int filesize() throws IOException {
        if ( input_stream == null ) return 0;
        return input_stream.available();
    }
    
    public boolean readable() throws IOException {
        if ( input_stream == null ) return false;
        return input_stream.available() > 0 ? true : false;
    }
    
    public byte[] readPacket() throws IOException {
        ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
        /**
         * @note Making a packet of TLV which has a sync byte as beginning
         * In other words, user should put a perfect TLV packet with sync byte into. 
         */
        byte[] tlv_header_buffer = new byte[TLV_HEADER_LENGTH];
        input_stream.read(tlv_header_buffer, 0, tlv_header_buffer.length);  
        
        byte[] tlv_payload_buffer = 
                new byte[((tlv_header_buffer[2] & 0xff) << 8 | (tlv_header_buffer[3] & 0xff))];
        input_stream.read(tlv_payload_buffer, 0, tlv_payload_buffer.length);

        output_stream = new ByteArrayOutputStream();
        output_stream.write(tlv_header_buffer);
        output_stream.write(tlv_payload_buffer);
        return output_stream.toByteArray();
    }
}

/**
 * TlvPacketDecoder is an example for getting 
 * - Tables which are include in TLV-SI of TLV, MMT-SI of MMTP packet \n
 * - NTP which is included in IPv4, IPv6 packet of TLV as NetworkTimeProtocolData \n
 * - MPU, MFU to be used for media which is included in MMTP Packet \n 
 */
public class TlvPacketDecoder {
    public static void main(String []args) throws IOException, InterruptedException {
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
         * @note Getting each one TLV packet from specific file.
         * It assume that platform should give a TLV packet to us as input of TLVExtractor
         */
        for ( int i=0; i<args.length; i++ ) {
            TlvFileReader tlv_file_pumper = new TlvFileReader(args[i]);
            if ( false == tlv_file_pumper.open() ) continue;
            
            /**
             * @note Putting a TLV packet into TLVExtractor \n
             * and you can wait for both the results of TLV as table of MPEG2 and MFU
             */
            ConsoleProgress.start(tlv_file_pumper.filesize());
            while ( tlv_file_pumper.readable() ) {
                byte[] tlv_packet = tlv_file_pumper.readPacket();
                if ( tlv_packet.length == 0 || 
                        false == tlv_coordinator.put(tlv_packet) ) break;
                Thread.sleep(0, 1);
                ConsoleProgress.update(tlv_packet.length);
            }
            ConsoleProgress.stop();
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
