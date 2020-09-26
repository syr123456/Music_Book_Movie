package com.wsk.life.bean.music;

import javax.persistence.*;

@Entity
@Table(name = "music_singer", schema = "spring")
public class WangYiLyricEntity {
    private Integer id;//id
    private String title;

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
}
