package top.rainynight.site.blog.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.cxf.jaxrs.provider.json.utils.JSONUtils;
import org.hibernate.validator.constraints.NotBlank;
import top.rainynight.site.user.entity.User;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

public class Article implements Serializable{
    private static final long serialVersionUID = -7076464179594028194L;
    private Integer articleid;

    private Integer classid;
    private Integer userid;
    private String title;
    private String content;
    private Date releasetime;

    public Integer getArticleid() {
        return articleid;
    }

    public void setArticleid(Integer articleid) {
        this.articleid = articleid;
    }

    @NotNull
    public Integer getClassid() {
        return classid;
    }

    public void setClassid(Integer classid) {
        this.classid = classid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NotBlank
    public String getTitle() {
        return title;
    }

    @NotBlank
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Article article = (Article) o;

        if (articleid != null ? !articleid.equals(article.articleid) : article.articleid != null) return false;
        if (classid != null ? !classid.equals(article.classid) : article.classid != null) return false;
        if (userid != null ? !userid.equals(article.userid) : article.userid != null) return false;
        if (content != null ? !content.equals(article.content) : article.content != null) return false;
        if (releasetime != null ? !releasetime.equals(article.releasetime) : article.releasetime != null) return false;
        if (title != null ? !title.equals(article.title) : article.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = articleid != null ? articleid : 0;
        result = 31 * result + (classid != null ? classid : 0);
        result = 31 * result + (userid != null ? userid : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (releasetime != null ? releasetime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return super.toString();
        }
    }

    private Articleclass articleclass;
    private User user;
    public Articleclass getArticleclass() {
        return articleclass;
    }
    public void setArticleclass(Articleclass articleclass) {
        this.articleclass = articleclass;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
