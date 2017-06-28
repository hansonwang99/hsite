package com.hansonwang99.domain.view;

/**
 * Created by hansonwang on 2017/6/28.
 */
public interface ArticleView {

    Long getId();
    Long getUserId();
    Long getCategoryId();
    String getTitle();
    Long getPublish_at();
    String getContent();
    String getTag();
}
