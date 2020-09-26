package com.wsk.life.bean.musicuser;

import javax.persistence.*;

@Entity
@Table(name = "wangyi_user", schema = "spring")
public class WangYiUser {
    private String id;//id
    private String name;//歌曲名
    private String geqian;
    private String img;
    private String backimg;
    private String guanzhu;
    private String fensi;//专辑名
    private String pubdate;//专辑名
    private String gedan;//歌手名
    private String city;//专辑id
    private String listennum;

    @Id
    @Column(name = "id")
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    @Basic
    @Column(name = "img")
    public String getImg() { return img; }
    public void setImg(String img) { this.img = img; }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "geqian")
    public String getGeqian() {
        return geqian;
    }
    public void setGeqian(String geqian) { this.geqian = geqian; }

    @Basic
    @Column(name = "backimg")
    public String getBackimg() {
        return backimg;
    }
    public void setBackimg(String backimg) {
        this.backimg = backimg;
    }
    @Basic
    @Column(name = "guanzhu")
    public String getGuanzhu() {
        return guanzhu;
    }
    public void setGuanzhu(String guanzhu) {
        this.guanzhu = guanzhu;
    }

    @Basic
    @Column(name = "fensi")
    public String getFensi() {
        return fensi;
    }
    public void setFensi(String fensi) {
        this.fensi = fensi;
    }
    @Basic
    @Column(name = "pubdate")
    public String getPubdate() {
        return pubdate;
    }
    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    @Basic
    @Column(name = "gedan")
    public String getGedan() { return gedan; }
    public void setGedan(String gedan) {
        this.gedan = gedan;
    }
    @Basic
    @Column(name = "city")
    public String getCity() { return city; }
    public void setCity(String city) { this.city= city; }
    @Basic
    @Column(name = "listennum")
    public String getListennum() { return listennum; }
    public void setListennum(String listennum) { this.listennum = listennum; }

}