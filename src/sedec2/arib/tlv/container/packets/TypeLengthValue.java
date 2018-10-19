package sedec2.arib.tlv.container.packets;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class TypeLengthValue extends BitReadWriter {
    protected byte sync = 0x01;
    protected byte packet_type;
    protected int length;
    
    public TypeLengthValue(byte[] buffer) {
        super(buffer);
    
        sync = (byte) ReadOnBuffer(2);
        SkipOnBuffer(6);
            
        packet_type = (byte) ReadOnBuffer(8);
        length = ReadOnBuffer(16);
    }
    
    public byte GetPacketType() {
        return packet_type;
    }
    
    public int GetLength() {
        return 4 + length;
    }
    
    public void PrintTypeLengthValue() {
        Logger.d(String.format("======= TLV Header ======= (%s)\n", getClass().getName()));
        Logger.d(String.format("sync : 0x%x \n", sync));
        Logger.d(String.format("packet_type : 0x%x \n", packet_type));
        Logger.d(String.format("length : 0x%x (%d) \n", length, length));
        Logger.d(String.format("-------------------------- \n"));
    }
}
