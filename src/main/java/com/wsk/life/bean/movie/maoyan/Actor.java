package com.wsk.life.bean.movie.maoyan;

import javax.persistence.*;

/**
 * Created by wsk1103 on 2017/10/24.
 */
@Entity
@Table(name = "movie_top", schema = "spring")
public class Actor {
    private String id;
    private String img;
    private String title0;
    private String title;


    @Id
    @Column(name = "id")
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    @Basic
    @Column(name = "img")
    public String getImg() { return img; }
    public void setImg(String img) { this.img = img; }

    @Basic
    @Column(name = "title0")
    public String getTitle0() {
        return title0;
    }
    public void setTitle0(String title0) {
        this.title0 = title0;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}
