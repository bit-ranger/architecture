package top.rainynight.foundation.util;

public class Page {

    private static final int DEFAULT_PAGE_SIZE = 20;

    private int pageNumber = 1;// 当前页
    private int start = 1;
    private int size = DEFAULT_PAGE_SIZE;// 每页记录数
    private int recordsCount;// 总记录数
    private int pagesCount;// 总页数


    /**
     * @param pageNumber 当前页码
     */
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber < 1 ? 1 : pageNumber;
        start = size * (pageNumber - 1) + 1;
    }

    /**
     * 该方法会同时计算总页数，请确保此前已经设置 size，否则将使用缺省 size
     * @param recordsCount 数据总条数
     */
    public void setRecordsCount(int recordsCount) {
        this.recordsCount = recordsCount;
        pagesCount = recordsCount % size == 0 ? recordsCount / size : recordsCount / size + 1;
    }

    /**
     * @param size 每页记录条数
     */
    public void setSize(int size) {
        this.size = size < 1 ? DEFAULT_PAGE_SIZE : size;
        setPageNumber(pageNumber);
        setRecordsCount(recordsCount);
    }

    /**
     * @return 总页数
     */
    public int getPagesCount() {return pagesCount; }

    /**
     * @return 数据总条数
     */
    public int getRecordsCount() {return recordsCount; }

    /**
     * @return 当前页码
     */
    public int getPageNumber() {return pageNumber; }

    /**
     * @return 每页记录条数
     */
    public int getSize() {return size; }

    public int getStart() {return start; }

    public boolean getIsFirst() {
        return pageNumber == 1;// 如是当前页是第1页
    }

    public boolean getIsLast() {
        return pageNumber == pagesCount;// 如果当前页是最后一页
    }

    public boolean getHasPrev() {
        return pageNumber != 1;// 只要当前页不是第1页
    }

    public boolean getHasNext() {
        return pageNumber != pagesCount;// 只要当前页不是最后1页
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Page)) return false;

        Page page = (Page) o;

        if (pageNumber != page.pageNumber) return false;
        if (pagesCount != page.pagesCount) return false;
        if (recordsCount != page.recordsCount) return false;
        if (size != page.size) return false;
        if (start != page.start) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pageNumber;
        result = 31 * result + start;
        result = 31 * result + size;
        result = 31 * result + recordsCount;
        result = 31 * result + pagesCount;
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        sb.append("pageNumber:").append(pageNumber).append(",");
        sb.append("size:").append(size).append(",");
        sb.append("start:").append(start).append(",");
        sb.append("recordsCount:").append(recordsCount).append(",");
        sb.append("pagesCount:").append(pagesCount);
        sb.append("}");
        return sb.toString();
    }
}
