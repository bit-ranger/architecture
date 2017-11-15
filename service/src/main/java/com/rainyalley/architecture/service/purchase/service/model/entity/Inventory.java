package com.rainyalley.architecture.service.purchase.service.model.entity;

import java.io.Serializable;

public class Inventory implements Serializable{

    private static final long serialVersionUID = 1832905356005680256L;
    private long total;
    private long frozen;
    private long sold;

    public Inventory(){}

    public Inventory(long total, long frozen, long sold) {
        this.total = total;
        this.frozen = frozen;
        this.sold = sold;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getFrozen() {
        return frozen;
    }

    public void setFrozen(long frozen) {
        this.frozen = frozen;
    }

    public long getSold() {
        return sold;
    }

    public void setSold(long sold) {
        this.sold = sold;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Inventory{");
        sb.append("total=").append(total);
        sb.append(", frozen=").append(frozen);
        sb.append(", sold=").append(sold);
        sb.append('}');
        return sb.toString();
    }
}
