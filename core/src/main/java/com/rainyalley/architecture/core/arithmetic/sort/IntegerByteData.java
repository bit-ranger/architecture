package com.rainyalley.architecture.core.arithmetic.sort;

import java.nio.ByteBuffer;

public class IntegerByteData implements ByteData<Integer> {

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
}
