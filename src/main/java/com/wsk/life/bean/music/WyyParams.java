package com.wsk.life.bean.music;

import lombok.Data;

/**
 * 参数
 *
 * @author Antoneo
 * @create 2018-06-28 22:46
 **/
@Data
public class WyyParams {

    private String encText;
    private String encSecKey;

    public Object getEncSecKey() {
        return encSecKey;
    }
    public void setEncSecKey(String encSecKey) {
        this.encSecKey=encSecKey;
    }

    public void setEncText(String encText) {
        this.encText=encText;
    }
    public Object getEncText() {
        return encText;
    }
}