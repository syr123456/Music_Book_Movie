package com.wsk.life.bean.music;

import javax.persistence.*;

@Entity
@Table(name = "movie_top", schema = "spring")
public class WangYiContent {
    private String id;
    private String author;
    private String detail;
    private String credit;
    private String time;
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
    @Column(name = "author")
    public String getAuthor() { return author; }
    public void setAuthor(String author) {
        this.author = author;
    }

    @Basic
    @Column(name = "detail")
    public String getDetail() {
        return detail;
    }
    public void setDetail(String detail) { this.detail = detail; }

    @Basic
    @Column(name = "time")
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    @Basic
    @Column(name = "credit")
    public String getCredit() {
        return credit;
    }
    public void setCredit(String credit) {
        this.credit = credit;
    }

}
