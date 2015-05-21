package top.rainynight.foundation.util;

public class Page {

    private static final int DEFAULT_PAGE_SIZE = 50;

    private int currentPage = 1;// 当前页
    private int pageSize = DEFAULT_PAGE_SIZE;// 每页记录数
    private int allRow;// 总记录数
    private int totalPage;// 总页数

    /**
     * 计算总页数,静态方法,供外部直接通过类名调用 　　
     * @param pageSize 每页记录数 　　
     * @param allRow 总记录数 　　
     * @return 总页数 　　
     */
    public static int countTotalPage(final int pageSize, final int allRow) {
        return allRow % pageSize == 0 ? allRow / pageSize : allRow / pageSize + 1;
    }

    /**
     *计算当前页开始记录 　　
     *@param pageSize 每页记录数 　　
     *@param currentPage 当前第几页
     *@return 当前页开始记录号 　　
     */
    public static int countOffset(final int pageSize, final int currentPage) {
        return pageSize * (currentPage - 1);
    }


    /**
     * @return 总页数
     */
    public int getTotalPage() {return totalPage; }

    /**
     * @return 偏移量(当前页第一条记录的index)
     */
    public int getOffset(){ return countOffset(getPageSize(), getCurrentPage()); }

    /**
     * @return 数据总条数
     */
    public int getAllRow() {return allRow; }

    /**
     * 该方法会同时计算总页数，请确保此前已经设置 pageSize，否则将使用缺省 pageSize
     * @param allRow 数据总条数
     */
    public void setAllRow(int allRow) {
        this.allRow = allRow;
        this.totalPage = countTotalPage(getPageSize(), getAllRow());
    }

    /**
     * @return 当前页码
     */
    public int getCurrentPage() {
         return currentPage;}

    /**
     * @param currentPage 当前页码
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage < 1 ? 1 : currentPage;
    }

    /**
     * @return 每页记录条数
     */
    public int getPageSize() {return pageSize; }

    /**
     * @param pageSize 每页记录条数
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize < 1 ? DEFAULT_PAGE_SIZE : pageSize;

    }

    public boolean getIsFirst() {
        return currentPage == 1;// 如是当前页是第1页
    }

    public boolean getIsLast() {
        return currentPage == totalPage;// 如果当前页是最后一页
    }

    public boolean getHasPrev() {
        return currentPage != 1;// 只要当前页不是第1页
    }

    public boolean getHasNext() {
        return currentPage != totalPage;// 只要当前页不是最后1页
    }

    /**
     * 设置当前页码，包含从String到int的类型转换,若输入异常数据，页码为1
     * @param pn 当前页码
     */
    public void setPn(String pn){
        int n;
        try{
            n = Integer.parseInt(pn);
        } catch (NumberFormatException e){
            n = 1;
        }
        setCurrentPage(n);
    }
}
