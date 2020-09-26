package com.wsk.life.bean.music;

import javax.persistence.*;

@Entity
@Table(name = "music_all", schema = "spring")
public class WangYiResponseEntity {
    private String id;//id
    private String title;//歌曲名
    private String img;
    private String play;
    private String album;//专辑名
    private String urlalbum;//专辑名
    private String singer;//歌手名
    private String urlsinger;//专辑id
    private String type;
    private Integer status;


    @Id
    @Column(name = "id")
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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

    @Basic
    @Column(name = "play")
    public String getPlay() {
        return play;
    }
    public void setPlay(String play) { this.play = play; }

    @Basic
    @Column(name = "singer")
    public String getSinger() {
        return singer;
    }
    public void setSinger(String singer) {
        this.singer = singer;
    }
    @Basic
    @Column(name = "urlsinger")
    public String getUrlsinger() {
        return urlsinger;
    }
    public void setUrlsinger(String urlsinger) {
        this.urlsinger = urlsinger;
    }

    @Basic
    @Column(name = "album")
    public String getAlbum() {
        return album;
    }
    public void setAlbum(String album) {
        this.album = album;
    }
    @Basic
    @Column(name = "urlalbum")
    public String getUrlalbum() {
        return urlalbum;
    }
    public void setUrlalbum(String urlalbum) {
        this.urlalbum = urlalbum;
    }

    @Basic
    @Column(name = "type")
    public String getType() { return type; }
    public void setType(String type) {
        this.type = type;
    }
    @Basic
    @Column(name = "status")
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) {
        this.status = status;
    }
}
