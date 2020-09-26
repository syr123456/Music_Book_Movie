package com.wsk.life.bean.music;

import com.wsk.life.bean.movie.maoyan.Top;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "music_type", schema = "spring")
public class WangYiTypeEntity {
    private String id;
    private String title;
    private String image;
    private String time;

    @Id
    @Column(name = "id")
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    @Basic
    @Column(name = "image")
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    @Basic
    @Column(name = "time")
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WangYiTypeEntity entity = (WangYiTypeEntity) o;
        if (id != null ? !id.equals(entity.id) : entity.id != null) return false;
        if (image != null ? !image.equals(entity.image) : entity.image != null) return false;
        if (time != null ? !time.equals(entity.time) : entity.time != null) return false;
        if (title!= null ? !title.equals(entity.title) : entity.title != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }
}
