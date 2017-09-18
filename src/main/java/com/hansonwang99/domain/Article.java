package com.hansonwang99.domain;

import com.hansonwang99.domain.enums.ArticleType;
import com.hansonwang99.domain.view.ArticleView;

import javax.persistence.*;
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
    private String userName;

    @Column(nullable = false)
    private Long categoryId;

    @Column(nullable = false)
    private String categoryName;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private ArticleType type;

    @Column(nullable = false)
    private Long publish_at;

    @Column(nullable = true, columnDefinition = "Text")
    private String create_time;

    @Column(nullable = true, columnDefinition = "Text")
    private String content;

    @Column(nullable = false)
    private String tag;

    @Column(nullable = false)
    private String profilePicture;

    public Article( ArticleView view ) {

        this.id = view.getId();
        this.userId = view.getUserId();
        this.userName = view.getUserName();
        this.categoryId = view.getCategoryId();
        this.categoryName = view.getCategoryName();
        this.title = view.getTitle();
        this.type = view.getType();
        this.publish_at = view.getPublish_at();
        this.create_time = view.getCreate_time();
        this.content = view.getContent();
        this.tag = view.getTag();
        this.profilePicture = view.getProfilePicture();
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

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getCategoryId() {
        return categoryId;
    }
    public void setCategoryId( Long categoryId ) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public ArticleType getType() {
        return type;
    }
    public void setType(ArticleType type) {
        this.type = type;
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

    public String getProfilePicture() {
        return profilePicture;
    }
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
