package zexamples.decoder;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

import sedec2.arib.tlv.ITLVExtractorListener;
import sedec2.arib.tlv.TLVExtractor;
import sedec2.arib.tlv.container.packets.NetworkTimeProtocolData;
import sedec2.base.Table;

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
    
        TLVExtractor tlv_extractor = new TLVExtractor();
        ITLVExtractorListener listener = new ITLVExtractorListener() {
            
            @Override
            public void onReceivedTable(Table table) {
//                System.out.print(String.format("Received Table (id : 0x%x) \n", 
//                        table.getTableId()));
//                table.print();
            }

            @Override
            public void onNtpUpdated(NetworkTimeProtocolData ntp) {
                ntp.print();
            }
        };
        
        tlv_extractor.addEventListener(listener);
        
        for ( int i=0; i<args.length; i++ ) {
            File inOutFile = new File(args[i]);
            
            try {
                DataInputStream dataInputStream  = 
                        new DataInputStream(
                                new BufferedInputStream(
                                        new FileInputStream(inOutFile)));
                final int COUNT_OF_SAMPLES = 100000;    
                final int TLV_HEADER_LENGTH = 4;
                int sample_counter = 0;
                
                while ( dataInputStream.available() > 0) {
                    /**
                     * @note Step.1 Getting a TLV packet from specific file
                     */
                    byte[] tlv_header_buffer = new byte[(int) TLV_HEADER_LENGTH];
                    dataInputStream.read(tlv_header_buffer, 0, tlv_header_buffer.length);  
                    
                    byte[] tlv_payload_buffer = 
                            new byte[((tlv_header_buffer[2] & 0xff) << 8 | (tlv_header_buffer[3] & 0xff))];
                    dataInputStream.read(tlv_payload_buffer, 0, tlv_payload_buffer.length);

                    /**
                     * @note Step.2 Converting a TLV packet to byte array
                     */
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
                    
//                    if ( sample_counter > COUNT_OF_SAMPLES ) break;
//                    sample_counter++;
                }
                
                dataInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        
        tlv_extractor.removeEventListener(listener);
        tlv_extractor.destroy();
        tlv_extractor = null;
        System.out.println("ByeBye");
    }
}
