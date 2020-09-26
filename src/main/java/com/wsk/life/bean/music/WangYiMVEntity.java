package com.wsk.life.bean.music;

import javax.persistence.*;

@Entity
@Table(name = "music_singermv", schema = "spring")
public class WangYiMVEntity {
    private String id;//id
    private String singerid;
    private String title;//歌手名
    private String img;

    @Id
    @Column(name = "id")
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    @Column(name = "singerid")
    public String getSingerid() { return singerid; }
    public void setSingerid(String singerid) { this.singerid = singerid; }

    @Basic
    @Column(name = "img")
    public String getImg() { return img; }
    public void setImg(String img) { this.img = img; }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}
