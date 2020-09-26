package com.wsk.life.bean.admin;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @DESCRIPTION : 多表查询结果
 * @AUTHOR : WuShukai1103
 * @TIME : 2018/3/8  18:31
 */
@Data
@ToString
public class ReportEntity implements Serializable {
    private Integer uid;
    private Integer pid;
    private Date ctime;
    private Date ptime;
    private String critic;
    private String image;
    private String username;
    private String phone;

    public String getImage() {
        return image;
    }

    public void setImage(String s) {
        this.image=image;
    }
}
