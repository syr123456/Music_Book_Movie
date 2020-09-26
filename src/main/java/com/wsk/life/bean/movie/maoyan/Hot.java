package com.wsk.life.bean.movie.maoyan;
import javax.persistence.*;

/**
 * Created by wsk1103 on 2017/10/24.
 */
@Entity
@Table(name = "movie_hot", schema = "spring")
public class Hot {
    private String id;
    private String title;
    private String image;
    private String TAS;
    private String detail;
    private String date;//即将上映
    private Integer status;
    private String director;
    private String bianju;
    private String country;//即将上映
    private String company;
    private String more;
    private Integer type;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }
    public void setId(String id) { this.id = id; }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "image")
    public String getImage() {
        return image;
    }
    public void setImage(String image) { this.image = image; }

    @Basic
    @Column(name = "TAS")
    public String getTAS() {
        return TAS;
    }
    public void setTAS(String TAS) {
        this.TAS = TAS;
    }

    @Basic
    @Column(name = "detail")
    public String getDetail() {
        return detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Basic
    @Column(name = "date")
    public String getDate() {
        return date;
    }
    public void setDate(String date) { this.date = date; }

    @Basic
    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    @Basic
    @Column(name = "director")
    public String getDirector() {
        return director;
    }
    public void setDirector(String director) {
        this.director = director;
    }

    @Basic
    @Column(name = "bianju")
    public String getBianju() {
        return bianju;
    }
    public void setBianju(String bianju) { this.bianju = bianju; }

    @Basic
    @Column(name = "country")
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    @Basic
    @Column(name = "company")
    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }

    @Basic
    @Column(name = "more")
    public String getMore() {
        return more;
    }
    public void setMore(String more) {
        this.more = more;
    }

    @Basic
    @Column(name = "type")
    public Integer getType() { return type; }
    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        com.wsk.life.bean.movie.maoyan.Hot entity = (com.wsk.life.bean.movie.maoyan.Hot) o;
        if (id != null ? !id.equals(entity.id) : entity.id != null) return false;
        if (title!= null ? !title.equals(entity.title) : entity.title != null) return false;
        if (TAS != null ? !TAS.equals(entity.TAS) : entity.TAS != null) return false;
        if (date != null ? !date.equals(entity.date) : entity.date != null) return false;
        if (detail != null ? !detail.equals(entity.detail) : entity.detail != null) return false;
        if (image != null ? !image.equals(entity.image) : entity.image != null) return false;
        if (status != null ? !status.equals(entity.status) : entity.status != null) return false;
        if (director!= null ? !director.equals(entity.director) : entity.director != null) return false;
        if (bianju != null ? !bianju.equals(entity.bianju) : entity.bianju != null) return false;
        if (country != null ? !country.equals(entity.country) : entity.country != null) return false;
        if (company != null ? !company.equals(entity.company) : entity.company != null) return false;
        if (more != null ? !more.equals(entity.more) : entity.more != null) return false;
        if (type != null ? !type.equals(entity.type) : entity.type != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (detail != null ? detail.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (TAS != null ? TAS.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (director != null ? director.hashCode() : 0);
        result = 31 * result + (bianju != null ? bianju.hashCode() : 0);
        result = 31 * result + (company != null ? company.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (more != null ? more.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
