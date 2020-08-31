package org.nocoder.commons.mybatispage;

import java.util.List;

/**
 * 分页基本实现
 *
 * @param <T>
 * @author Admin
 */
public class Page<T> implements Pagination {

    /**
     * 页码
     */
    protected int pageNo;
    /**
     * 每页记录条数
     */
    protected int pageCount;
    /**
     * 总页数
     */
    protected int totalPage;
    /**
     * 总记录条数
     */
    protected int totalCount = -1;

    /**
     * 限制的总条数 不超过 500
     */
    protected int limitTotalCount = 0;

    /**
     * 用于存放查询结果
     */
    protected List<T> resultList;
    /**
     * 匹配数
     */
    protected int matchCount;

    public Page() {
    }

    public Page(Integer pageNo, int pageCount) {
        if (pageNo == null) {
            pageNo = 1;
        }
        if (pageNo <= 0) {
            pageNo = 1;
            //throw new IllegalArgumentException("pageNo must be greater than 0.");
        }
        if (pageCount <= 0 && pageCount > 50) {
            pageCount = 50;
            //throw new IllegalArgumentException("pageCount must be greater than 0.");
        }
        this.pageNo = pageNo;
        this.pageCount = pageCount;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    @Override
    public int getPageNo() {
        return pageNo;
    }

    @Override
    public int getPageCount() {
        return pageCount;
    }

    @Override
    public int getTotalCount() {
        return totalCount;
    }

    public int getMatchCount() {
        return matchCount;
    }

    public void setMatchCount(int matchCount) {
        this.matchCount = matchCount;
    }

    @Override
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        // 如果总数为负数, 表未设置
        if (totalCount < 0) {
            totalPage = 0;
        } else { // 计算总页数
            if (pageCount > 0) {
                totalPage = (totalCount / pageCount) + (totalCount % pageCount == 0 ? 0 : 1);
            }
        }
    }

    public List<T> getResultList() {
        return resultList;
    }

    public void setResultList(List<T> resultList) {
        if (this.pageCount <= 0 && resultList != null) {
            this.totalCount = resultList.size();
        }
        this.resultList = resultList;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getLimitTotalCount() {
        return getTotalCount() > 500 ? 500 : getTotalCount();
    }

    public void setLimitTotalCount(int limitTotalCount) {
        this.limitTotalCount = limitTotalCount;
    }
}
