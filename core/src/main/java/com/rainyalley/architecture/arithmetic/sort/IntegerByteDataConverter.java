package com.rainyalley.architecture.arithmetic.sort;

import java.nio.ByteBuffer;

public class IntegerByteDataConverter implements ByteDataConverter<Integer> {

    @Override
    public byte[] toByteArray(Integer data) {
        return ByteBuffer.allocate(unitBytes()).putInt(data).array();
    }

    @Override
    public Integer toData(byte[] dataBytes) {
        return ByteBuffer.wrap(dataBytes).asIntBuffer().get();
    }

    @Override
    public int unitBytes() {
        return 4;
    }

    @Override
    public byte[] unitSeparator() {
        return new byte[0];
    }
}
