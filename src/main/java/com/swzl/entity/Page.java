package com.swzl.entity;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class Page{

    // 页大小(暂时写死,每页只显示10条数据)
    private Integer pageSize = 10;

    // 当前页码
    private Integer currPage;

    // 总页数
    private Integer totalPage;

    // 总的记录数
    private Integer totalCount;

    public Page() {
    }

    public Page( Integer pageSize, Integer currPage, Integer totalPage, Integer totalCount) {
        this.pageSize = pageSize;
        this.currPage = currPage;
        this.totalPage = totalPage;
        this.totalCount = totalCount;
    }




    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrPage() {
        return currPage;
    }

    public void setCurrPage(Integer currPage) {
        this.currPage = currPage;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public String toString() {
        return "Page{" +
                ", pageSize=" + pageSize +
                ", currPage=" + currPage +
                ", totalPage=" + totalPage +
                ", totalCount=" + totalCount +
                '}';
    }
}
