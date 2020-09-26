package com.wsk.life.bean.music;

import javax.persistence.*;

@Entity
@Table(name = "music_singer", schema = "spring")
public class WangYiSingerDetailEntity {
    private Integer id;//id
    private String title;
    private String titledel;

    @Id
    @Column(name = "id")
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) { this.title = title; }

    @Basic
    @Column(name = "titledel")
    public String getTitledel() {
        return titledel;
    }
    public void setTitledel(String titledel) {
        this.titledel = titledel;
    }

}
