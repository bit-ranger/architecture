package com.rainyalley.architecture.core.util;

public class Chunk{

    private int level;

    private int idx;

    private int size;

    public Chunk(int level, int idx, int size) {
        this.level = level;
        this.idx = idx;
        this.size = size;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Chunk{");
        sb.append("level=").append(level);
        sb.append(", idx=").append(idx);
        sb.append(", size=").append(size);
        sb.append('}');
        return sb.toString();
    }

    public int getLevel() {
        return level;
    }

    public int getIdx() {
        return idx;
    }

    public int getSize() {
        return size;
    }

}
