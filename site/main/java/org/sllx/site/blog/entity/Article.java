package org.sllx.site.blog.entity;

import java.sql.Date;

/**
 * Created by sllx on 14-11-27.
 */
public class Article {
    private int articleid;
    private int classid;
    private int userid;
    private String title;
    private String content;
    private Date releasetime;
    private int sort;
    private int state;

    public int getArticleid() {
        return articleid;
    }

    public void setArticleid(int articleid) {
        this.articleid = articleid;
    }

    public int getClassid() {
        return classid;
    }

    public void setClassid(int classid) {
        this.classid = classid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getReleasetime() {
        return releasetime;
    }

    public void setReleasetime(Date releasetime) {
        this.releasetime = releasetime;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Article article = (Article) o;

        if (articleid != article.articleid) return false;
        if (classid != article.classid) return false;
        if (sort != article.sort) return false;
        if (state != article.state) return false;
        if (userid != article.userid) return false;
        if (content != null ? !content.equals(article.content) : article.content != null) return false;
        if (releasetime != null ? !releasetime.equals(article.releasetime) : article.releasetime != null) return false;
        if (title != null ? !title.equals(article.title) : article.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = articleid;
        result = 31 * result + classid;
        result = 31 * result + userid;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (releasetime != null ? releasetime.hashCode() : 0);
        result = 31 * result + sort;
        result = 31 * result + state;
        return result;
    }
}
