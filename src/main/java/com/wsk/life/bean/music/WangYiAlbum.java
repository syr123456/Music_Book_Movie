package com.wsk.life.bean.music;
import javax.persistence.*;

@Entity
@Table(name = "music_album", schema = "spring")
public class WangYiAlbum {
    private String id;//id
    private String singerid;
    private String singername;
    private String title;
    private String img;
    private String time;
    private String company;
    private String detail;

    @Id
    @Column(name = "id")
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    @Column(name = "singerid")
    public String getSingerid() { return singerid; }
    public void setSingerid(String singerid) { this.singerid = singerid; }

    @Column(name = "singername")
    public String getSingername() { return singername; }
    public void setSingername(String singername) { this.singername = singername; }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) { this.title = title; }

    @Basic
    @Column(name = "img")
    public String getImg() { return img; }
    public void setImg(String img) { this.img = img; }

    @Basic
    @Column(name = "time")
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    @Basic
    @Column(name = "company")
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    @Basic
    @Column(name = "detail")
    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }

}
