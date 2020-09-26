package com.wsk.life.bean.musicuser;

import javax.persistence.*;

@Entity
@Table(name = "wangyi_user_gedan", schema = "spring")
public class UserGedan {
    private String id;//id
    private String userid;
    private String name;//歌曲名
    private String img;
    private String description;
    private String num;

    @Id
    @Column(name = "id")
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    @Basic
    @Column(name = "userid")
    public String getUserid() { return userid; }
    public void setUserid(String userid) { this.userid = userid; }

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
    @Column(name = "description")
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description= description; }
    @Basic
    @Column(name = "num")
    public String getNum() { return num; }
    public void setNum(String num) { this.num = num; }
}