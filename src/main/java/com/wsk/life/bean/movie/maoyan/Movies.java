package com.wsk.life.bean.movie.maoyan;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "movie_information", schema = "spring")
public class Movies implements Serializable {
    private String id;
    private String title;
    private String url;
    private String image;
    private String TAS;
    private String detail;
//    private String date;//即将上映
//    private Integer status;
    private String director;
    private String bianju;
    private String country;//即将上映
    private String company;
    private String more;
//    private Integer type;

    private String vedios;
    private String images;
    private String actors;
    private String chats;
    private String news;

    private String actor1;
    private String actor1_img1;
    private String actor1_img2;
    private String actor2;
    private String actor2_img1;
    private String actor2_img2;
    private String actor3;
    private String actor3_img1;
    private String actor3_img2;
    private String actor4;
    private String actor4_img1;
    private String actor4_img2;
    private String chatTitle1;
    private String chatAuthor1;
    private String chatImg1;
    private String chatContent1;
    private String chatTitle2;
    private String chatAuthor2;
    private String chatImg2;
    private String chatContent2;
    private String chatTitle3;
    private String chatAuthor3;
    private String chatImg3;
    private String chatContent3;
    private String chatTitle4;
    private String chatAuthor4;
    private String chatImg4;
    private String chatContent4;


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
    @Column(name = "url")
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
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

//    @Basic
//    @Column(name = "date")
//    public String getDate() {
//        return date;
//    }
//    public void setDate(String date) { this.date = date; }
//
//    @Basic
//    @Column(name = "status")
//    public Integer getStatus() {
//        return status;
//    }
//    public void setStatus(Integer status) {
//        this.status = status;
//    }

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

//    @Basic
//    @Column(name = "type")
//    public Integer getType() { return type; }
//    public void setType(Integer type) {
//        this.type = type;
//    }

    @Id
    @Column(name = "vedios")
    public String getVedios() {
        return vedios;
    }
    public void setVedios(String vedios) { this.vedios = vedios; }
    @Basic
    @Column(name = "images")
    public String getImages() {
        return images;
    }
    public void setImages(String images) {
        this. images=images ;
    }

    @Basic
    @Column(name = "actors")
    public String getActors() {
        return actors;
    }
    public void setActors(String actors) {
        this.actors= actors;
    }
    @Basic
    @Column(name = "chats")
    public String getChats() {
        return chats;
    }
    public void setChats(String chats) {
        this.chats = chats;
    }

    @Basic
    @Column(name = "news")
    public String getNews() {
        return news;
    }
    public void setNews(String news) {
        this.news = news;
    }
    @Basic
    @Column(name = "actor1")
    public String getActor1() {
        return actor1;
    }
    public void setActor1(String actor1) { this.actor1 = actor1; }
    @Basic
    @Column(name = "actor1_img1")
    public String getActor1_img1() {
        return actor1_img1;
    }
    public void setActor1_img1(String actor1_img1) {
        this.actor1_img1 = actor1_img1;
    }
    @Basic
    @Column(name = "actor1_img2")
    public String getActor1_img2() {
        return actor1_img2;
    }
    public void setActor1_img2(String actor1_img2) {
        this.actor1_img2 = actor1_img2;
    }

    @Basic
    @Column(name = "actor2")
    public String getActor2() {
        return actor2;
    }
    public void setActor2(String actor2) {
        this.actor2 = actor2;
    }
    @Basic
    @Column(name = "actor2_img1")
    public String getActor2_img1() {
        return actor2_img1;
    }
    public void setActor2_img1(String actor2_img1) {
        this.actor2_img1 = actor2_img1;
    }
    @Basic
    @Column(name = "actor2_img2")
    public String getActor2_img2() {
        return actor2_img2;
    }
    public void setActor2_img2(String actor2_img2) {
        this.actor2_img2 = actor2_img2;
    }

    @Basic
    @Column(name = "actor3")
    public String getActor3() {
        return actor3;
    }
    public void setActor3(String actor3) {
        this.actor3 = actor3;
    }
    @Basic
    @Column(name = "actor3_img1")
    public String getActor3_img1() {
        return actor3_img1;
    }
    public void setActor3_img1(String actor3_img1) {
        this.actor3_img1 = actor3_img1;
    }
    @Basic
    @Column(name = "actor3_img2")
    public String getActor3_img2() {
        return actor3_img2;
    }
    public void setActor3_img2(String actor3_img2) {
        this.actor3_img2 = actor3_img2;
    }

    @Basic
    @Column(name = "actor4")
    public String getActor4() {
        return actor4;
    }
    public void setActor4(String actor4) {
        this.actor4 = actor4;
    }
    @Basic
    @Column(name = "actor4_img1")
    public String getActor4_img1() {
        return actor4_img1;
    }
    public void setActor4_img1(String actor4_img1) {
        this.actor4_img1 = actor4_img1;
    }
    @Basic
    @Column(name = "actor4_img2")
    public String getActor4_img2() {
        return actor4_img2;
    }
    public void setActor4_img2(String actor4_img2) {
        this.actor4_img2 = actor4_img2;
    }

    @Column(name = "chatTitle1")
    public String getChatTitle1() { return chatTitle1; }
    public void setChatTitle1(String chatTitle1) { this.chatTitle1 =chatTitle1 ; }
    @Basic
    @Column(name = "chatAuthor1")
    public String getChatAuthor1() { return chatAuthor1; }
    public void setChatAuthor1(String chatAuthor1) { this.chatAuthor1 = chatAuthor1; }
    @Column(name = "chatImg1")
    public String getChatImg1() { return chatImg1; }
    public void setChatImg1(String chatImg1) { this.chatImg1 = chatImg1; }
    @Basic
    @Column(name = "chatContent1")
    public String getChatContent1() { return chatContent1; }
    public void setChatContent1(String chatContent1) { this.chatContent1 = chatContent1; }

    @Column(name = "chatTitle2")
    public String getChatTitle2() { return chatTitle2; }
    public void setChatTitle2(String chatTitle2) { this.chatTitle2 =chatTitle2 ; }
    @Basic
    @Column(name = "chatAuthor2")
    public String getChatAuthor2() { return chatAuthor2; }
    public void setChatAuthor2(String chatAuthor2) { this.chatAuthor2 = chatAuthor2; }
    @Column(name = "chatImg2")
    public String getChatImg2() { return chatImg2; }
    public void setChatImg2(String chatImg2) { this.chatImg2 = chatImg2; }
    @Basic
    @Column(name = "chatContent2")
    public String getChatContent2() { return chatContent2; }
    public void setChatContent2(String chatContent2) { this.chatContent2 = chatContent2; }

    @Column(name = "chatTitle3")
    public String getChatTitle3() { return chatTitle3; }
    public void setChatTitle3(String chatTitle3) { this.chatTitle3 =chatTitle3 ; }
    @Basic
    @Column(name = "chatAuthor3")
    public String getChatAuthor3() { return chatAuthor3; }
    public void setChatAuthor3(String chatAuthor3) { this.chatAuthor3 = chatAuthor3; }
    @Column(name = "chatImg3")
    public String getChatImg3() { return chatImg3; }
    public void setChatImg3(String chatImg3) { this.chatImg3 = chatImg3; }
    @Basic
    @Column(name = "chatContent3")
    public String getChatContent3() { return chatContent3; }
    public void setChatContent3(String chatContent3) { this.chatContent3 = chatContent3; }

    @Column(name = "chatTitle4")
    public String getChatTitle4() { return chatTitle4; }
    public void setChatTitle4(String chatTitle4) { this.chatTitle4 =chatTitle4 ; }
    @Basic
    @Column(name = "chatAuthor4")
    public String getChatAuthor4() { return chatAuthor4; }
    public void setChatAuthor4(String chatAuthor4) { this.chatAuthor4 = chatAuthor4; }
    @Column(name = "chatImg4")
    public String getChatImg4() { return chatImg4; }
    public void setChatImg4(String chatImg4) { this.chatImg4 = chatImg4; }
    @Basic
    @Column(name = "chatContent4")
    public String getChatContent4() { return chatContent4; }
    public void setChatContent4(String chatContent4) { this.chatContent4 = chatContent4; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        com.wsk.life.bean.movie.maoyan.Movies entity = (com.wsk.life.bean.movie.maoyan.Movies) o;
        if (id != null ? !id.equals(entity.id) : entity.id != null) return false;
        if (title!= null ? !title.equals(entity.title) : entity.title != null) return false;
        if (url!= null ? !url.equals(entity.url) : entity.url != null) return false;
        if (TAS != null ? !TAS.equals(entity.TAS) : entity.TAS != null) return false;
//        if (date != null ? !date.equals(entity.date) : entity.date != null) return false;
        if (detail != null ? !detail.equals(entity.detail) : entity.detail != null) return false;
        if (image != null ? !image.equals(entity.image) : entity.image != null) return false;
//        if (status != null ? !status.equals(entity.status) : entity.status != null) return false;
        if (director!= null ? !director.equals(entity.director) : entity.director != null) return false;
        if (bianju != null ? !bianju.equals(entity.bianju) : entity.bianju != null) return false;
        if (country != null ? !country.equals(entity.country) : entity.country != null) return false;
        if (company != null ? !company.equals(entity.company) : entity.company != null) return false;
        if (more != null ? !more.equals(entity.more) : entity.more != null) return false;
//        if (type != null ? !type.equals(entity.type) : entity.type != null) return false;

        if (vedios != null ? !vedios.equals(entity.vedios) : entity.vedios != null) return false;
        if (images != null ? !images.equals(entity.images) : entity.images != null) return false;
        if (actors != null ? !actors.equals(entity.actors) : entity.actors != null) return false;
        if (chats != null ? !chats.equals(entity.chats) : entity.chats != null) return false;
        if (news != null ? !news.equals(entity.news) : entity.news != null) return false;
        if (actor1 != null ? !actor1.equals(entity.actor1) : entity.actor1 != null) return false;
        if (actor1_img1 != null ? !actor1_img1.equals(entity.actor1_img1) : entity.actor1_img1 != null) return false;
        if (actor1_img2 != null ? !actor1_img2.equals(entity.actor1_img2) : entity.actor1_img2 != null) return false;
        if (actor2 != null ? !actor2.equals(entity.actor2) : entity.actor2 != null) return false;
        if (actor2_img1 != null ? !actor2_img1.equals(entity.actor2_img1) : entity.actor2_img1 != null) return false;
        if (actor2_img2 != null ? !actor2_img2.equals(entity.actor2_img2) : entity.actor2_img2 != null) return false;
        if (actor3 != null ? !actor3.equals(entity.actor3) : entity.actor3 != null) return false;
        if (actor3_img1 != null ? !actor3_img1.equals(entity.actor3_img1) : entity.actor3_img1 != null) return false;
        if (actor3_img2 != null ? !actor3_img2.equals(entity.actor3_img2) : entity.actor3_img2 != null) return false;
        if (actor4 != null ? !actor4.equals(entity.actor4) : entity.actor4 != null) return false;
        if (actor4_img1 != null ? !actor4_img1.equals(entity.actor4_img1) : entity.actor4_img1 != null) return false;
        if (actor4_img2 != null ? !actor4_img2.equals(entity.actor4_img2) : entity.actor4_img2 != null) return false;
        if (chatTitle1 != null ? !chatTitle1.equals(entity.chatTitle1) : entity.chatTitle1 != null) return false;
        if (chatAuthor1 != null ? !chatAuthor1.equals(entity.chatAuthor1) : entity.chatAuthor1 != null) return false;
        if (chatImg1 != null ? !chatImg1.equals(entity.chatImg1) : entity.chatImg1 != null) return false;
        if (chatContent1 != null ? !chatContent1.equals(entity.chatContent1) : entity.chatContent1 != null) return false;

        if (chatTitle2 != null ? !chatTitle2.equals(entity.chatTitle2) : entity.chatTitle2 != null) return false;
        if (chatAuthor2 != null ? !chatAuthor2.equals(entity.chatAuthor2) : entity.chatAuthor2 != null) return false;
        if (chatImg2 != null ? !chatImg2.equals(entity.chatImg2) : entity.chatImg2 != null) return false;
        if (chatContent2 != null ? !chatContent2.equals(entity.chatContent2) : entity.chatContent2 != null) return false;

        if (chatTitle3 != null ? !chatTitle3.equals(entity.chatTitle3) : entity.chatTitle3 != null) return false;
        if (chatAuthor3 != null ? !chatAuthor3.equals(entity.chatAuthor3) : entity.chatAuthor3 != null) return false;
        if (chatImg3 != null ? !chatImg3.equals(entity.chatImg3) : entity.chatImg3 != null) return false;
        if (chatContent3 != null ? !chatContent3.equals(entity.chatContent3) : entity.chatContent3 != null) return false;

        if (chatTitle4 != null ? !chatTitle4.equals(entity.chatTitle4) : entity.chatTitle4 != null) return false;
        if (chatAuthor4 != null ? !chatAuthor4.equals(entity.chatAuthor4) : entity.chatAuthor4 != null) return false;
        if (chatImg4 != null ? !chatImg4.equals(entity.chatImg4) : entity.chatImg4 != null) return false;
        if (chatContent4 != null ? !chatContent4.equals(entity.chatContent4) : entity.chatContent4 != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (detail != null ? detail.hashCode() : 0);
//        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (TAS != null ? TAS.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
//        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (director != null ? director.hashCode() : 0);
        result = 31 * result + (bianju != null ? bianju.hashCode() : 0);
        result = 31 * result + (company != null ? company.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (more != null ? more.hashCode() : 0);
//        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (vedios != null ? vedios.hashCode() : 0);
        result = 31 * result + (images != null ? images.hashCode() : 0);
        result = 31 * result + (actors != null ? actors.hashCode() : 0);
        result = 31 * result + (chats != null ? chats.hashCode() : 0);
        result = 31 * result + (news != null ? news.hashCode() : 0);
        result = 31 * result + (actor1 != null ? actor1.hashCode() : 0);
        result = 31 * result + (actor1_img1 != null ? actor1_img1.hashCode() : 0);
        result = 31 * result + (actor1_img2 != null ? actor1_img2.hashCode() : 0);
        result = 31 * result + (actor2 != null ? actor2.hashCode() : 0);
        result = 31 * result + (actor2_img1 != null ? actor2_img1.hashCode() : 0);
        result = 31 * result + (actor2_img2 != null ? actor2_img2.hashCode() : 0);
        result = 31 * result + (actor3 != null ? actor3.hashCode() : 0);
        result = 31 * result + (actor3_img1 != null ? actor3_img1.hashCode() : 0);
        result = 31 * result + (actor3_img2 != null ? actor3_img2.hashCode() : 0);
        result = 31 * result + (actor4 != null ? actor4.hashCode() : 0);
        result = 31 * result + (actor4_img1 != null ? actor4_img1.hashCode() : 0);
        result = 31 * result + (actor4_img2 != null ? actor4_img2.hashCode() : 0);
        result = 31 * result + (chatTitle1 != null ? chatTitle1.hashCode() : 0);
        result = 31 * result + (chatAuthor1 != null ? chatAuthor1.hashCode() : 0);
        result = 31 * result + (chatImg1 != null ? chatImg1.hashCode() : 0);
        result = 31 * result + (chatContent1 != null ? chatContent1.hashCode() : 0);

        result = 31 * result + (chatTitle2 != null ? chatTitle2.hashCode() : 0);
        result = 31 * result + (chatAuthor2 != null ? chatAuthor2.hashCode() : 0);
        result = 31 * result + (chatImg2 != null ? chatImg2.hashCode() : 0);
        result = 31 * result + (chatContent2 != null ? chatContent2.hashCode() : 0);

        result = 31 * result + (chatTitle3 != null ? chatTitle3.hashCode() : 0);
        result = 31 * result + (chatAuthor3 != null ? chatAuthor3.hashCode() : 0);
        result = 31 * result + (chatImg3 != null ? chatImg3.hashCode() : 0);
        result = 31 * result + (chatContent3 != null ? chatContent3.hashCode() : 0);

        result = 31 * result + (chatTitle4 != null ? chatTitle4.hashCode() : 0);
        result = 31 * result + (chatAuthor4 != null ? chatAuthor4.hashCode() : 0);
        result = 31 * result + (chatImg4 != null ? chatImg4.hashCode() : 0);
        result = 31 * result + (chatContent4 != null ? chatContent4.hashCode() : 0);
        return result;
    }
}