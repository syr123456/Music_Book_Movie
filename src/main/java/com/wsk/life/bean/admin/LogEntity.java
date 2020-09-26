package com.wsk.life.bean.admin;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @DESCRIPTION :
 * @AUTHOR : WuShukai1103
 * @TIME : 2018/5/1  23:33
 */
@Data
public class LogEntity {
    private long aid;
    private String adminName;
    private String phone;
    private Timestamp time;
    private String action;
}
