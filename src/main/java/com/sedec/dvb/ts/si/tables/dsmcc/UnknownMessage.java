package com.sedec.dvb.ts.si.tables.dsmcc;

public class UnknownMessage extends Message {

    public UnknownMessage(byte[] buffer) {
        super(buffer);

        skipOnBuffer(messageLength*8);
    }

    @Override
    public void print() {
        super._print_();
    }
}
