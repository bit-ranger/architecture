package com.rainyalley.architecture.batch.firstPage;

import java.util.Date;

public class BatchData {

    private long id;
    private int stat;
    private Date crt_time;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStat() {
        return stat;
    }

    public void setStat(int stat) {
        this.stat = stat;
    }

    public Date getCrt_time() {
        return crt_time;
    }

    public void setCrt_time(Date crt_time) {
        this.crt_time = crt_time;
    }
}
