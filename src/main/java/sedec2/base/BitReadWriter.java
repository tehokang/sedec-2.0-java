package sedec2.base;

import java.util.Arrays;

import sedec2.util.Logger;

public class BitReadWriter {
    protected byte[] m_buffer;

    private int m_pos;
    private int m_out_counter;
    private int m_by_buffer;

    public BitReadWriter(byte[] buffer) {
        m_buffer = buffer;

        m_by_buffer = 0;
        m_pos = 0;
        m_out_counter = 8;
    }

    public byte[] getBuffer() {
        return m_buffer;
    }

    public byte[] getCurrentBuffer() {
        try {
            return Arrays.copyOfRange(m_buffer, m_pos, m_buffer.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int calculateCRC32(byte[] temp, int real_data_length) {
        int bit_count = 0;
        int bit_in_byte = 0;
        int data_bit;
        int byte_count = 0;
        int shift_reg[] = new int[32];
        int g[] = { 1,1,1,0,1,1,0,1,1,0,1,1,1,0,0,
                       0,1,0,0,0,0,0,1,1,0,0,1,0,0,0,
                       0,0,1 };
        int i,nr_bits;
        byte[] data;
        int crc;

        /* Initialize shift register's to '1' */
        for(i=0; i<32; i++)
            shift_reg[i] = 1;

        /* Calculate nr of data bits */
        nr_bits = real_data_length*8;
        data = temp;

        while (bit_count < nr_bits) {
            data_bit = data[byte_count]  & (0x80 >> bit_in_byte);
            data_bit = data_bit >> (7 - bit_in_byte);
            bit_in_byte++; bit_count++;
            if (bit_in_byte == 8) {
                bit_in_byte = 0;
                byte_count++;
            }

            data_bit ^= shift_reg[31];
            i = 31;
            while (i != 0) {
                if ( g[i] != 0 )
                    shift_reg[i] = shift_reg[i-1] ^ data_bit;
                else
                    shift_reg[i] = shift_reg[i-1];
                i--;
            }
            shift_reg[0] = data_bit;
        }

        crc = 0x00000000;
        for (i= 0; i<32; i++)
            crc = (crc << 1) | (shift_reg[31-i]);

        return(crc);
    }

    public void skipOnBuffer(int len) {
        for ( int i=0; i<len; i++ ) {
            m_out_counter--;

            if (m_out_counter == 0) {
                m_out_counter = 8;
                m_pos++;
            }
        }
    }

    public int readOnBuffer(int len) {
        int mask;
        byte sp = m_buffer[m_pos];

        mask = 1 << (m_out_counter-1);
        m_by_buffer = 0;

        for ( int i=0; i<len; i++ ) {
            m_by_buffer <<= 1;

            byte v = (byte)(sp & mask);
            if ( v != 0 )
                m_by_buffer |= 1;

            mask >>=1;
            m_out_counter--;

            if ( m_out_counter == 0 ) {
                m_out_counter = 8;
                mask = 128;
                m_pos++;
                if ( m_pos < m_buffer.length)
                    sp = m_buffer[m_pos];
            }
        }
        return m_by_buffer;
    }

    public void writeOnBuffer(int value, int len) {
        int mask;
        mask = 1 << (len-1);

        for ( int i=0; i<len; i++ ) {
            m_by_buffer <<= 1;
            int v = value & mask;
            if ( v != 0 )
                m_by_buffer |= 1;

            mask >>= 1;
            m_out_counter--;

            if (m_out_counter==0) {
                m_buffer[m_pos] =  (byte) m_by_buffer;
                m_out_counter = 8;
                m_pos++;
                m_by_buffer = 0;
            }
        }
    }

    public void writeOnBuffer(long value, int len) {
        long mask;
        mask = 1 << (len-1);

        for ( int i=0; i<len; i++ ) {
            m_by_buffer <<= 1;
            long v = (value & mask);
            if ( v != 0 )
                m_by_buffer |= 1;

            mask >>= 1;
            m_out_counter--;

            if (m_out_counter==0) {
                m_buffer[m_pos] = (byte) m_by_buffer;
                m_out_counter = 8;
                m_pos++;
                m_by_buffer = 0;
            }
        }
    }

    public int readUnsignedExpGolombCodedInt() {
        return readExpGolombCodeNum();
    }

    public int readSignedExpGolombCodedInt() {
        int codeNum = readExpGolombCodeNum();
        return ((codeNum % 2) == 0 ? -1 : 1) * ((codeNum + 1) / 2);
    }

    protected int readExpGolombCodeNum() {
        int leadingZeros = 0;
        while ( 0 == readOnBuffer(1) ) {
            leadingZeros++;
        }
        return (1 << leadingZeros) - 1 + (leadingZeros > 0 ? readOnBuffer(leadingZeros) : 0);
    }

    public void printBuffer() {
        int j=1;
        System.out.print("########### Byte Align ########### \n");
        System.out.print(String.format("%03d : ", j));
        for(int i=0; i<m_buffer.length; i++)
        {
            System.out.print(String.format("%02x ", m_buffer[i]));
            if(i%10 == 9) Logger.p(String.format("\n%03d : ", (++j)));
        }
        System.out.print("\n################################### \n\n");
    }
}
