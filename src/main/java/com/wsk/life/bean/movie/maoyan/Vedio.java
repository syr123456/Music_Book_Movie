package com.wsk.life.bean.movie.maoyan;

import javax.persistence.*;

/**
 * Created by wsk1103 on 2017/10/24.
 */
@Entity
@Table(name = "movie_top", schema = "spring")
public class Vedio {
    private String id;
    private String url;
    private String title;
    private String length;
    private String img;

    @Id
    @Column(name = "id")
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    @Basic
    @Column(name = "img")
    public String getImg() { return img; }
    public void setImg(String img) { this.img = img; }

    @Basic
    @Column(name = "url")
    public String getUrl() { return url; }
    public void setUrl(String url) {
        this.url = url;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "length")
    public String getLength() {
        return length;
    }
    public void setLength(String length) {
        this.length = length;
    }
}
