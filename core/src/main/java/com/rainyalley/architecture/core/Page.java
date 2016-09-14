package com.rainyalley.architecture.core;

import java.io.Serializable;

public class Page implements Serializable {

    private static final int DEFAULT_PAGE_SIZE = 20;

    private int pageNumber = 1;// 当前页
    private int start = 1;
    private int size = Page.DEFAULT_PAGE_SIZE;// 每页记录数
    private int recordsCount;// 总记录数
    private int pagesCount;// 总页数

    /**
     * @return 总页数
     */
    public int getPagesCount() {
        return this.pagesCount;
    }

    /**
     * @return 数据总条数
     */
    public int getRecordsCount() {
        return this.recordsCount;
    }

    /**
     * 该方法会同时计算总页数，请确保此前已经设置 size，否则将使用缺省 size
     *
     * @param recordsCount 数据总条数
     */
    public void setRecordsCount(int recordsCount) {
        this.recordsCount = recordsCount;
        this.pagesCount = recordsCount % this.size == 0 ? recordsCount / this.size : recordsCount / this.size + 1;
    }

    /**
     * @return 当前页码
     */
    public int getPageNumber() {
        return this.pageNumber;
    }

    /**
     * @param pageNumber 当前页码
     */
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber < 1 ? 1 : pageNumber;
        this.start = this.size * (pageNumber - 1) + 1;
    }

    /**
     * @return 每页记录条数
     */
    public int getSize() {
        return this.size;
    }

    /**
     * @param size 每页记录条数
     */
    public void setSize(int size) {
        this.size = size < 1 ? Page.DEFAULT_PAGE_SIZE : size;
        this.setPageNumber(this.pageNumber);
        this.setRecordsCount(this.recordsCount);
    }

    public int getStart() {
        return this.start;
    }

    public boolean getIsFirst() {
        return this.pageNumber == 1;// 如是当前页是第1页
    }

    public boolean getIsLast() {
        return this.pageNumber == this.pagesCount;// 如果当前页是最后一页
    }

    public boolean getHasPrev() {
        return this.pageNumber != 1;// 只要当前页不是第1页
    }

    public boolean getHasNext() {
        return this.pageNumber != this.pagesCount;// 只要当前页不是最后1页
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Page)) return false;

        Page page = (Page) o;

        if (this.pageNumber != page.pageNumber) return false;
        if (this.pagesCount != page.pagesCount) return false;
        if (this.recordsCount != page.recordsCount) return false;
        if (this.size != page.size) return false;
        return this.start == page.start;

    }

    @Override
    public int hashCode() {
        int result = this.pageNumber;
        result = 31 * result + this.start;
        result = 31 * result + this.size;
        result = 31 * result + this.recordsCount;
        result = 31 * result + this.pagesCount;
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        sb.append("pageNumber:").append(this.pageNumber).append(",");
        sb.append("size:").append(this.size).append(",");
        sb.append("start:").append(this.start).append(",");
        sb.append("recordsCount:").append(this.recordsCount).append(",");
        sb.append("pagesCount:").append(this.pagesCount);
        sb.append("}");
        return sb.toString();
    }
}
