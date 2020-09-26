package com.wsk.life.bean.movie.maoyan;

import javax.persistence.*;
import java.util.List;

/**
 * Created by wsk1103 on 2017/10/24.
 */
@Entity
@Table(name = "movie_top", schema = "spring")
public class Top {
    private Integer id;
    private String title;
    private String detail;
    private String image;

    @Id
    @Column(name = "id")
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    @Basic
    @Column(name = "image")
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    @Basic
    @Column(name = "detail")
    public String getDetail() {
        return detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Top entity = (Top) o;
        if (id != null ? !id.equals(entity.id) : entity.id != null) return false;
        if (image != null ? !image.equals(entity.image) : entity.image != null) return false;
        if (detail != null ? !detail.equals(entity.detail) : entity.detail != null) return false;
        if (title!= null ? !title.equals(entity.title) : entity.title != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (detail != null ? detail.hashCode() : 0);
        return result;
    }
}
