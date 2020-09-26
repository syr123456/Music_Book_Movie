package com.wsk.life.config;

import com.wsk.life.config.AesCBCUtils;
import com.wsk.life.bean.music.WyyParams;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * 网易云抓取
 *
 * @author Antoneo
 * @create 2018-06-28 22:21
 **/
public class WyySpider {

    private static String iv="0102030405060708";
    private static String g="0CoJUm6Qyw8W8jud";
    private static String i="Sx7KnWt4ttr85X0b";
    private static String encSecKey="252e2abdc25a5c8e4e4131b88db3df7d01ab4a139249b78e653b97ab52f53b873993b86648e54daa3a99eeb20fd3b2c4d1d551231a152bfa56ed0a13baae9243f978bf1fbcde4e70b25087fd0aeef413a698a0a37567a550876f8cdeedb25cf359f54532eb2681f63641a4fa98b837fb9978c3296b2923bca8f5d1661d3ec5dc";


    private static String a(int a){
        int d,e;
        String b = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String c="";
        for(d=0;a>d;d+=1){
            e=(int)Math.floor(Math.random()*b.length());
            c+=b.charAt(e);
        }
        return c;
    }

    private static String b(String d,String g) throws Exception {
        return AesCBCUtils.encrypt(d,"utf-8",g,iv).replace("\r\n","");
    }


    /**
     * 获得歌词url的参数对象
     */
    public static WyyParams getParams(String id) throws Exception {
        String d=String.format("{\"id\":\"%s\",\"lv\":-1,\"tv\":-1,\"csrf_token\":\"\"}",id);
        String encText=b(d,g);
        encText=b(encText,i);
        WyyParams params=new WyyParams();
        params.setEncSecKey(encSecKey);
        params.setEncText(encText);
        return params;
    }

    public static Document spider(String id){
        Document doc= null;
        Map data=new HashMap();
        Map head=new HashMap();
        try {
            WyyParams params=WyySpider.getParams(id);
            Connection con=Jsoup.connect("https://music.163.com/weapi/song/lyric?csrf_token=");
            data.put("encSecKey",params.getEncSecKey());
            data.put("params",params.getEncText());
            head.put("Content-Type","application/x-www-form-urlencoded");
            head.put("Accept","*/*");
            con.headers(head);
            con.data(data);
            doc= con.post();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }


    public static void main(String[] args){
    }
}