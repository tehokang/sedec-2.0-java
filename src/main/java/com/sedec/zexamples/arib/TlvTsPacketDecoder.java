package com.sedec.zexamples.arib;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;

import com.sedec.arib.extractor.tlv.TlvDemultiplexer;
import com.sedec.arib.extractor.tlvts.TlvTsDemultiplexer;
import com.sedec.arib.tlv.container.mmt.si.DescriptorFactory;
import com.sedec.arib.tlv.container.mmt.si.TableFactory;
import com.sedec.arib.tlv.container.mmt.si.descriptors.Descriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MPU_NodeDescriptor;
import com.sedec.arib.tlv.container.mmt.si.tables.DataAssetManagementTable;
import com.sedec.arib.tlv.container.mmt.si.tables.DataContentConfigurationTable;
import com.sedec.arib.tlv.container.mmt.si.tables.DataDirectoryManagementTable;
import com.sedec.arib.tlv.container.mmt.si.tables.MMT_PackageTable;
import com.sedec.arib.tlv.container.mmt.si.tables.PackageListTable;
import com.sedec.arib.tlv.container.mmt.si.tables.MMT_PackageTable.Asset;
import com.sedec.arib.tlv.container.mmtp.mfu.MFU_IndexItem;
import com.sedec.arib.tlv.container.packets.NetworkTimeProtocolData;
import com.sedec.arib.tlvts.container.packets.TlvTransportStream;
import com.sedec.base.Table;
import com.sedec.util.CommandLineParam;
import com.sedec.util.ConsoleProgress;
import com.sedec.util.FileTs188PacketReader;
import com.sedec.util.HttpTsPacketReader;
import com.sedec.util.PacketReader;
import com.sedec.util.SimpleApplicationCoordinator;
import com.sedec.util.SimpleApplicationCoordinator.SubDirectory;

class SimpleTlvTsCoordinator implements
        TlvDemultiplexer.Listener,
        TlvTsDemultiplexer.Listener {
    protected static final String TAG = SimpleTlvTsCoordinator.class.getSimpleName();
    protected CommandLine commandLine;
    protected TlvDemultiplexer tlv_demuxer = null;
    protected TlvTsDemultiplexer tlvts_demuxer = null;

    /**
     * Tables to extract from TLV
     */
    protected MMT_PackageTable mpt = null;
    protected PackageListTable plt = null;
    protected DataAssetManagementTable damt = null;
    protected DataContentConfigurationTable dcct = null;
    protected DataDirectoryManagementTable ddmt = null;

    protected final String download_path = "./download/";
    protected final String app_download_path = download_path + "/applications/";
    protected final String video_download_path = download_path + "/video/";
    protected final String audio_download_path = download_path + "/audio/";
    protected final String ttml_download_path = download_path + "/ttml/";

    protected List<SimpleApplicationCoordinator> applications = new ArrayList<>();
    /**
     * Video, Audio, IndexItem of Application to extract from TLV
     */
    protected Map<Integer, BufferedOutputStream> video_bs_map = new HashMap<>();
    protected Map<Integer, BufferedOutputStream> audio_bs_map = new HashMap<>();

    public SimpleTlvTsCoordinator(CommandLine commandLine) {
        this.commandLine = commandLine;

        tlvts_demuxer = new TlvTsDemultiplexer();
        tlvts_demuxer.addEventListener(this);
        tlvts_demuxer.enableTlvFilter();
        tlvts_demuxer.addTlvAllFilter();

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
         * To add NAL prefix of Video Sample
         */
        tlv_demuxer.enableVideoPreModification();

        /**
         * To add Sync-Word prefix of Audio Sample
         */
        tlv_demuxer.enableAudioPreModification();
        tlv_demuxer.addSiAllFilter();
    }

    public void destroy() {
        tlvts_demuxer.removeEventListener(this);
        tlvts_demuxer.destroy();
        tlvts_demuxer = null;

        tlv_demuxer.removeEventListener(this);
        tlv_demuxer.destroy();
        tlv_demuxer = null;

        video_bs_map.clear();
        video_bs_map = null;

        audio_bs_map.clear();
        audio_bs_map = null;
    }

    public void clearQueue() {
        tlv_demuxer.clearQueue();
    }

    public boolean put(byte[] tlvts_raw) {
        return tlvts_demuxer.put(tlvts_raw);
    }

    public boolean put(TlvTransportStream tlvts ) {
        return tlvts_demuxer.put(tlvts);
    }

    @Override
    public void onReceivedTable(Table table) {
        if ( commandLine.hasOption(CommandLineParam.SHOW_TABLES) ) table.print();

        switch ( table.getTableId() ) {
        case TableFactory.MH_AIT:
//            table.print();
            break;
        case TableFactory.DDMT:
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
                SimpleApplicationCoordinator app =
                        new SimpleApplicationCoordinator(app_download_path);
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
        case TableFactory.DAMT:
            damt = (DataAssetManagementTable) table;
//            damt.print();

            for ( int i=0; i<damt.getMPUs().size(); i++ ) {
                DataAssetManagementTable.MPU mpu = damt.getMPUs().get(i);
                for ( int j=0; j<mpu.mpu_info_byte.size(); j++ ) {
                    Descriptor desc = mpu.mpu_info_byte.get(j);
                    if ( desc.getDescriptorTag() == DescriptorFactory.MPU_NODE_DESCRIPTOR ) {
                        for ( int k=0; k<applications.size(); k++ ) {
                            SimpleApplicationCoordinator app = applications.get(k);
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
        case TableFactory.DCMT:
            dcct = (DataContentConfigurationTable) table;
//            dcct.print();
            break;
        case TableFactory.MPT:
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
                            tlv_demuxer.addVideoFilter(pid);
                            break;
                        case "hvc1":
                            /**
                             * @note Video without VPS, SPS and PPS in MFU
                             */
                            tlv_demuxer.addVideoFilter(pid);
                            break;
                        case "mp4a":
                            /**
                             * @note Audio
                             */
                            tlv_demuxer.addAudioFilter(pid);
                            break;
                        case "stpp":
                            /**
                             * @note TTML
                             */
                            tlv_demuxer.addTtmlFilter(pid);
                            break;
                        case "aapp":
                            /**
                             * @note Application
                             */
                            tlv_demuxer.addApplicationFilter(pid);
                            break;
                        case "asgd":
                            /**
                             * @note Synchronous type general purpose data
                             */
                            tlv_demuxer.addGeneralPurposeDataFilter(pid);
                            break;
                        case "aagd":
                            /**
                             * @note Asynchronous type general purpose data
                             */
                            tlv_demuxer.addGeneralPurposeDataFilter(pid);
                            break;
                    }
                }
            }
            break;
        case TableFactory.PLT:
            if ( plt == null  ) {
                plt = (PackageListTable) table;
//                plt.print();
            } else if ( plt != null && plt.getVersion() !=
                    ((PackageListTable)table).getVersion() ) {
                plt = (PackageListTable) table;
                mpt = null;
            }
            break;
        default:
            if ( table.isUnknownTable() == true ) {
                System.out.print(String.format(
                        "There might be a table which sedec couldn't decode (table_id : 0x%x)\n",
                        table.getTableId()));
            }
            break;
        }
    }

    @Override
    public void onReceivedVideo(int packet_id, int mpu_sequence_number, int sample_number, byte[] buffer) {
        try {
            if ( video_bs_map.containsKey(packet_id) == false ) {
                new File(video_download_path).mkdirs();
                video_bs_map.put(packet_id, new BufferedOutputStream(new FileOutputStream(
                        new File(String.format("%s/video.mfu.0x%04x.hevc",
                                video_download_path, packet_id)))));
            }

            if ( commandLine.hasOption(CommandLineParam.EXTRACT) ) {
                BufferedOutputStream video_bs = video_bs_map.get(packet_id);
                video_bs.write(buffer);
            }
            /**
             * sedec provide wrapper class to check SPS, AUD of H.265
             * If user do {@link VideoExtractor#enablePreModification},
             * the buffer will include nal_prefix as 0x00000001 of 4 bytes or
             * the buffer starts with nal_unit_header
             */
//            MFU_H265NalUnit non_vcl_nal_unit = new MFU_H265NalUnit(buffer);
//            non_vcl_nal_unit.print();
//            if ( non_vcl_nal_unit.getSPS() != null ) {
//                non_vcl_nal_unit.getSPS().print();
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceivedAudio(int packet_id, int mpu_sequence_number, int sample_number, byte[] buffer) {
        try {
            if ( audio_bs_map.containsKey(packet_id) == false ) {
                new File(audio_download_path).mkdirs();
                audio_bs_map.put(packet_id, new BufferedOutputStream(new FileOutputStream(
                        new File(String.format("%s/audio.mfu.0x%04x.aac",
                                audio_download_path, packet_id)))));
            }

            if ( commandLine.hasOption(CommandLineParam.EXTRACT) ) {
                BufferedOutputStream audio_bs = audio_bs_map.get(packet_id);
                audio_bs.write(buffer);
            }

            /**
             * sedec provide wrapper class to check AudioSyncStream of LATM of ISO 14496-3
             * If user do {@link AudioExtractor#enablePremodification},
             * the buffer will include syncword and audioMuxLengthBytes of Table 1.23 of ISO14496
             * or the buffer starts from AudioMuxElement(1).
             */
//            MFU_AACLatm aac = new MFU_AACLatm(buffer);
//            aac.print();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceivedTtml(int packet_id, byte[] buffer) {
        /*
        MFU_ClosedCaption ttml = new MFU_ClosedCaption(buffer);
        new File(ttml_download_path).mkdirs();
        switch ( ttml.getDataType() ) {
            case 0x00:
                Logger.d("\t [TTML-DOC] \n");
                Logger.d(String.format("%s \n", new String(ttml.getDataByte())));
                FileUtility.save(
                        String.format("%s/ttml.mfu.0x%x.txt",
                                ttml_download_path, packet_id),
                                new String(ttml.getDataByte()));
                break;
            case 0x01:
                Logger.d("\t [TTML-PNG] \n");
                FileUtility.save(
                        String.format("%s/ttml.mfu.0x%x.png",
                                ttml_download_path, packet_id), buffer);
                break;
            case 0x02:
                Logger.d("\t [TTML-SVG] \n");
                FileUtility.save(
                        String.format("%s/ttml.mfu.0x%x.svg",
                                ttml_download_path, packet_id), buffer);
                break;
            case 0x03:
                Logger.d("\t [TTML-PCM] \n");
                FileUtility.save(
                        String.format("%s/ttml.mfu.0x%x.pcm",
                                ttml_download_path, packet_id), buffer);
                break;
            case 0x04:
                Logger.d("\t [TTML-MP3] \n");
                FileUtility.save(
                        String.format("%s/ttml.mfu.0x%x.mp3",
                                ttml_download_path, packet_id), buffer);
                break;
            case 0x05:
                Logger.d("\t [TTML-AAC] \n");
                FileUtility.save(
                        String.format("%s/ttml.mfu.0x%x.aac",
                                ttml_download_path, packet_id), buffer);
                break;
            case 0x06:
                Logger.d("\t [TTML-FONT-SVG] \n");
                FileUtility.save(
                        String.format("%s/ttml.mfu.0x%x.font.svg",
                                ttml_download_path, packet_id), buffer);
                break;
            case 0x07:
                Logger.d("\t [TTML-FONT-WOFF] \n");
                FileUtility.save(
                        String.format("%s/ttml.mfu.0x%x.font.woff",
                                ttml_download_path, packet_id), buffer);
                break;
            default:
                break;
        }
        */
    }

    @Override
    public void onReceivedNtp(NetworkTimeProtocolData ntp) {
//        ntp.print();
    }

    @Override
    public void onReceivedApplication(int packet_id, int item_id,
            int mpu_sequence_number, byte[] buffer) {
        for ( int i=0; i<applications.size(); i++ ) {
            SimpleApplicationCoordinator app = applications.get(i);
            for ( int j=0; j<app.sub_directories.size(); j++ ) {
                SubDirectory sub_directory = app.sub_directories.get(j);
                if ( sub_directory.mpu_sequence_number == mpu_sequence_number ) {
                    for ( int k=0; k<sub_directory.files.size(); k++ ) {
                        SimpleApplicationCoordinator.File file = sub_directory.files.get(k);
                        if ( file.item_id == item_id && file.read_completed == false ) {
                            file.buffer = Arrays.copyOfRange(buffer, 0, buffer.length);
                            file.read_completed = true;
                        }
                    }
                }
            }
        }

        for ( int i=0; i<applications.size(); i++ ) {
            applications.get(i).done();
        }
    }

    @Override
    public void onReceivedIndexItem(int packet_id, int item_id,
            int mpu_sequence_number, byte[] buffer) {
        MFU_IndexItem index_item = new MFU_IndexItem(buffer);
//        index_item.print();

        for ( int i=0; i<applications.size(); i++ ) {
            SimpleApplicationCoordinator app = applications.get(i);
            for ( int j=0; j<app.sub_directories.size(); j++ ) {
                SubDirectory sub_directory = app.sub_directories.get(j);
                if ( sub_directory.mpu_sequence_number == mpu_sequence_number ) {
                    boolean exist = false;
                    for ( int k=0; k<index_item.getItems().size(); k++ ) {
                        MFU_IndexItem.Item item = index_item.getItems().get(k);
                        for ( int n=0; n<sub_directory.files.size(); n++ ) {
                            SimpleApplicationCoordinator.File file = sub_directory.files.get(n);
                            if ( file.item_id == item.item_id ) {
                                exist = true;
                                break;
                            }
                        }

                        if ( exist == false ) {
                            SimpleApplicationCoordinator.File file = app.new File();
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
//        MFU_GeneralPurposeData data = new MFU_GeneralPurposeData(buffer);
//        data.print();
    }

    @Override
    public void onReceivedTlv(int packet_id, byte[] buffer) {
        /**
         * Putting a TLV formatted packet which's including MPU-MFU scrambled
         */
//        TypeLengthValue tlv = PacketFactory.createPacket(buffer);
//        switch ( tlv.getPacketType() ) {
//            case PacketFactory.COMPRESSED_IP_PACKET:
//                CompressedIpPacket compress_ip_packet = (CompressedIpPacket) tlv;
//                MMTP_Packet mmtp_packet = compress_ip_packet.getPacketData().mmtp_packet;
//                if ( mmtp_packet != null && mmtp_packet.isScrambled() == true ) {
//                    byte[] mmtp_payload = mmtp_packet.getPayloadBytes();
//                    // ...
//                    // Descrambling payload of MMTP which's scrambled
//                    // ...
//                    mmtp_packet.updateDescramblePayload(mmtp_payload);
//                }
//                break;
//            default:
//                // Others couldn't be scrambled
//                break;
//        }

        tlv_demuxer.put(buffer);
    }
}

public class TlvTsPacketDecoder extends BaseSimpleDecoder {
    @Override
    public void justDoIt(CommandLine commandLine) {
        String target_file = commandLine.getOptionValue(CommandLineParam.TSTLV_TYPE);
        SimpleTlvTsCoordinator simple_tlvts_coordinator =
                new SimpleTlvTsCoordinator(commandLine);

        /**
         * Decoration of console user interface
         */
        ConsoleProgress progress_bar = new ConsoleProgress("TLV-TS").
                show(true, true, true, true, true, false);
        /**
         * Getting each one TLV packet from specific file.
         * It assume that platform should give a TLV packet to us as input of TLVExtractor
         */
        PacketReader ts_reader = new FileTs188PacketReader(target_file);

        if ( commandLine.hasOption(CommandLineParam.REMOTE_RESOURCES) )
            ts_reader = new HttpTsPacketReader(target_file);

        if ( false == ts_reader.open() ) return;

        progress_bar.start(ts_reader.filesize());

        while ( ts_reader.readable() > 0) {
            final byte[] ts_packet = ts_reader.readPacket();
            if ( ts_packet == null ||
                    ts_packet.length == 0 ||
                    ts_packet[0] != 0x47 ) continue;

            if ( false == simple_tlvts_coordinator.put(ts_packet) ) break;
            /**
             * Updating of console user interface
             */

            if ( commandLine.hasOption(CommandLineParam.SHOW_PROGRESS) )
                progress_bar.update(ts_packet.length);
        }

        simple_tlvts_coordinator.clearQueue();

        progress_bar.stop();
        ts_reader.close();
        ts_reader = null;

        /**
         * Destroy of SimpleTlvCoordinator to not handle and released by garbage collector
         */
        simple_tlvts_coordinator.destroy();
        simple_tlvts_coordinator = null;

    }
}

