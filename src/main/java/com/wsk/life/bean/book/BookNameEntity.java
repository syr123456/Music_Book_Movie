package com.wsk.life.bean.book;
import javax.persistence.*;
/**
 * @DESCRIPTION :
 * @AUTHOR : WuShukai1103
 * @TIME : 2018/5/1  17:44
 */
@Entity
@Table(name = "bookname", schema = "spring")
public class BookNameEntity {
    private String id;//
    private String title;//
    private String image;//
    private String price;//
    private String author;//
    private String publisher;//
    private String congbian;//
    private String tag;//
    private String ISBN;//
    private String publishtime;//
    private String baozhuang;//
    private String kaiben;//
    private String yeshu;//
    private String zishu;//
    private String content;
    private String acontent;
    private String log;
    private String wherebuy;
    private Integer num;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "ISBN")
    public String getISBN() {
        return ISBN;
    }
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    @Basic
    @Column(name = "publishtime")
    public String getPublishtime() {
        return publishtime;
    }
    public void setPublishtime(String publishtime) {
        this.publishtime = publishtime;
    }

    @Basic
    @Column(name = "publisher")
    public String getPublisher() {
        return publisher;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Basic
    @Column(name = "baozhuang")
    public String getBaozhuang() {
        return baozhuang;
    }
    public void setBaozhuang(String baozhuang) {
        this.baozhuang = baozhuang;
    }

    @Basic
    @Column(name = "kaiben")
    public String getKaiben() {
        return kaiben;
    }
    public void setKaiben(String kaiben) {
        this.kaiben = kaiben;
    }

    @Basic
    @Column(name = "yeshu")
    public String getYeshu() {
        return yeshu;
    }
    public void setYeshu(String yeshu) {
        this.yeshu = yeshu;
    }

    @Basic
    @Column(name = "zishu")
    public String getZishu() {
        return zishu;
    }
    public void setZishu(String zishu) {
        this.zishu = zishu;
    }

    @Basic
    @Column(name = "congbian")
    public String getCongbian() {
        return congbian;
    }
    public void setCongbian(String congbian) {
        this.congbian = congbian;
    }

    @Basic
    @Column(name = "author")
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    @Basic
    @Column(name = "tag")
    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }

    @Basic
    @Column(name = "wherebuy")
    public String getWherebuy() { return wherebuy; }
    public void setWherebuy(String wherebuy) {
        this.wherebuy= wherebuy;
    }

    @Basic
    @Column(name = "image")
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Basic
    @Column(name = "content")
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content =content;
    }

    @Basic
    @Column(name = "acontent")
    public String getAcontent() {
        return acontent;
    }
    public void setAcontent(String acontent) {
        this.acontent =acontent;
    }

    @Basic
    @Column(name = "log")
    public String getLog() {
        return log;
    }
    public void setLog(String log) {
        this.log =log;
    }

    @Basic
    @Column(name = "price")
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price =price;
    }

    @Basic
    @Column(name = "num")
    public Integer getNum() {
        return num;
    }
    public void setNum(int num) {
        this.num =num;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        com.wsk.life.bean.book.BookNameEntity entity = (com.wsk.life.bean.book.BookNameEntity) o;

        if (id != null ? !id.equals(entity.id) : entity.id != null) return false;
        if (author != null ? !author.equals(entity.author) : entity.author != null) return false;
        if (image != null ? !image.equals(entity.image) : entity.image != null) return false;
        if (publisher != null ? !publisher.equals(entity.publisher) : entity.publisher != null) return false;
        if (price != null ? !price.equals(entity.price) : entity.price != null) return false;
        if (log != null ? !log.equals(entity.log) : entity.log != null) return false;
        if (tag != null ? !tag.equals(entity.tag) : entity.tag != null) return false;
        if (congbian != null ? !congbian.equals(entity.congbian) : entity.congbian != null) return false;
        if (wherebuy != null ? !wherebuy.equals(entity.wherebuy) : entity.wherebuy!= null) return false;
        if (content != null ? !content.equals(entity.content) : entity.content != null) return false;
        if (acontent != null ? !acontent.equals(entity.acontent) : entity.acontent != null) return false;

        if (title != null ? !title.equals(entity.title) : entity.title != null) return false;
        if (ISBN != null ? !ISBN.equals(entity.ISBN) : entity.ISBN != null) return false;
        if (publishtime != null ? !publishtime.equals(entity.publishtime) : entity.publishtime != null) return false;
        if (kaiben != null ? !kaiben.equals(entity.kaiben) : entity.kaiben != null) return false;
        if (yeshu != null ? !yeshu.equals(entity.yeshu) : entity.yeshu != null) return false;
        if (zishu != null ? !zishu.equals(entity.zishu) : entity.zishu != null) return false;
        if (baozhuang != null ? !baozhuang.equals(entity.baozhuang) : entity.baozhuang != null) return false;
        if (num != null ? !num.equals(entity.num) : entity.num != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (publisher != null ? publisher.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (log != null ? log.hashCode() : 0);
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        result = 31 * result + (congbian != null ? congbian.hashCode() : 0);
        result = 31 * result + (wherebuy != null ? wherebuy.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (acontent != null ? acontent.hashCode() : 0);

        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (ISBN != null ? ISBN.hashCode() : 0);
        result = 31 * result + (publishtime != null ? publishtime.hashCode() : 0);
        result = 31 * result + (kaiben != null ? kaiben.hashCode() : 0);
        result = 31 * result + (baozhuang != null ? baozhuang.hashCode() : 0);
        result = 31 * result + (yeshu != null ? yeshu.hashCode() : 0);
        result = 31 * result + (zishu != null ? zishu.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (num != null ? num.hashCode() : 0);
        return result;
    }
}
