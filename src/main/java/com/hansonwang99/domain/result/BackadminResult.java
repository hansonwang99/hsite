package com.hansonwang99.domain.result;

import com.hansonwang99.domain.Article;

import java.util.List;

/**
 * Created by Administrator on 2017/8/24.
 */
public class BackadminResult {

    private int page = 1;
    private int pageSize = 10;
    private Long pageTotalNum = 30l;
    private List<Article> articles = null;

    public BackadminResult( ) {
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Long getPageTotalNum() {
        return pageTotalNum;
    }

    public void setPageTotalNum(Long pageTotalNum) {
        this.pageTotalNum = pageTotalNum;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
