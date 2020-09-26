package com.wsk.life.bean.admin;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @DESCRIPTION :
 * @AUTHOR : WuShukai1103
 * @TIME : 2018/3/8  19:20
 */
@Entity
@Table(name = "critic_report", schema = "spring", catalog = "")
public class CriticReportEntity {
    private int id;
    private Integer pid;
    private Integer uid;
    private Timestamp ctime;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "pid")
    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    @Basic
    @Column(name = "uid")
    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    @Basic
    @Column(name = "ctime")
    public Timestamp getCtime() {
        return ctime;
    }

    public void setCtime(Timestamp ctime) {
        this.ctime = ctime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        com.wsk.life.bean.admin.CriticReportEntity that = (com.wsk.life.bean.admin.CriticReportEntity) o;

        if (id != that.id) return false;
        if (pid != null ? !pid.equals(that.pid) : that.pid != null) return false;
        if (uid != null ? !uid.equals(that.uid) : that.uid != null) return false;
        if (ctime != null ? !ctime.equals(that.ctime) : that.ctime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (pid != null ? pid.hashCode() : 0);
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        result = 31 * result + (ctime != null ? ctime.hashCode() : 0);
        return result;
    }
}
