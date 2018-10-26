package zexamples.decoder;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import sedec2.arib.tlv.ITLVExtractorListener;
import sedec2.arib.tlv.TLVExtractor;
import sedec2.arib.tlv.container.packets.NetworkTimeProtocolData;
import sedec2.base.Table;

/**
 * TlvPacketDecoder is an example for getting 
 * - Tables which are include in TLV-SI of TLV, MMT-SI of MMTP packet \n
 * - NTP which is included in IPv4, IPv6 packet of TLV as NetworkTimeProtocolData \n
 * - MPU, MFU to be used for media which is included in MMTP Packet \n 
 */
public class TlvPacketDecoder {
    public static void main(String []args) {
        if ( args.length < 1 ) {
            System.out.println("Oops, " + 
                    "I need TLV packet to be parsed as 1st parameter");
            System.out.println(
                    "Usage: java -classpath . " +
                    "zexamples.decoder.TlvPacketDecoder " + 
                    "{TLV Raw File} \n");
        }
    
        /**
         * @note Step.1 Creating TLVExtractor
         */
        TLVExtractor tlv_extractor = new TLVExtractor();
        
        /**
         * @note Step.2 Add Event Listener to listen Table, NTP, MFU
         * and you can set table filters only what you want to receive or not
         */
        ITLVExtractorListener listener = new ITLVExtractorListener() {
            int counter = 0;
            
            @Override
            public void onReceivedTable(Table table) {
                System.out.print(String.format("[%d] User Received Table (id : 0x%x) \n", 
                        counter++, table.getTableId()));
                table.print();
            }
            
            @Override
            public void onUpdatedNtp(NetworkTimeProtocolData ntp) {
                ntp.print();
            }
        };
        tlv_extractor.addEventListener(listener);
        
        /**
         * @note Option.1 Enable specific filters which you want to receive
         */
        List<Byte> filters = new ArrayList<>();
        filters.add((byte)sedec2.arib.tlv.si.TableFactory.AMT);
        filters.add((byte)sedec2.arib.tlv.si.TableFactory.TLV_NIT_ACTUAL);
        filters.add((byte)sedec2.arib.tlv.si.TableFactory.TLV_NIT_OTHER);
        tlv_extractor.enableTableFilter(filters);
        
        /**
         * @note Option.2 Enable all of filters which you want to receive
         */
        tlv_extractor.enableAllOfTableFilter();
        
        /**
         * @note Option.3 Disable all of filters which you don't want to receive
         */
        tlv_extractor.disableTableFilter();
        
        /**
         * @note Option.4 Enable NTP data if user want to receive
         */
        tlv_extractor.disableNtpFilter();
        
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
                    if ( false == tlv_extractor.put(outputStream.toByteArray()) ) {
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
         * @note Step.4 Destroy of TLVExtractor to not handle and released by garbage collector
         */
        tlv_extractor.removeEventListener(listener);
        tlv_extractor.destroy();
        tlv_extractor = null;
        
        System.out.println("ByeBye");
    }
}
