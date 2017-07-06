package com.hansonwang99.domain;

import com.hansonwang99.domain.view.ArticleView;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/24.
 */
@Entity
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long categoryId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Long publish_at;

    @Column(nullable = true, columnDefinition = "Text")
    private String create_time;

    @Column(nullable = true, columnDefinition = "Text")
    private String content;

    @Column(nullable = false)
    private String tag;

    public Article( ArticleView view ) {

        this.id = view.getId();
        this.userId = view.getUserId();
        this.categoryId = view.getCategoryId();
        this.title = view.getTitle();
        this.publish_at = view.getPublish_at();
        this.create_time = view.getCreate_time();
        this.content = view.getContent();
        this.tag = view.getTag();
    }

    public Article() {
        super();
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId( Long userId ) {
        this.userId = userId;
    }

    public Long getCategoryId() {
        return categoryId;
    }
    public void setCategoryId( Long categoryId ) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Long getPublish_at() {
        return publish_at;
    }
    public void setPublish_at(Long publish_at) {
        this.publish_at = publish_at;
    }

    public String getCreate_time() {
        return create_time;
    }
    public void setCreate_time( String create_time ) {
        this.create_time = create_time;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
}
