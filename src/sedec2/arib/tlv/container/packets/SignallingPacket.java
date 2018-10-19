package sedec2.arib.tlv.container.packets;

import java.io.ByteArrayOutputStream;

import sedec2.arib.tlv.si.TableFactory;
import sedec2.base.Table;

public class SignallingPacket extends TypeLengthValue {
    protected Table table;
    
    public SignallingPacket(byte[] buffer) {
        super(buffer);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(buffer, 4, buffer.length-4);
        
        table = TableFactory.CreateTable(outputStream.toByteArray());
        
        if ( null != table ) {
            SkipOnBuffer(table.GetTableLength());
        }
    }

    @Override
    public void PrintTypeLengthValue() {
        super.PrintTypeLengthValue();
        
        if ( table != null ) {
            table.PrintTable();
        }
    }
}
