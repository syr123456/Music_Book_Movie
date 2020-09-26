package com.wsk.life.service.musicuser.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wsk.life.bean.music.*;
import com.wsk.life.bean.musicuser.UserGedan;
import com.wsk.life.bean.musicuser.WangYiUser;
import com.wsk.life.config.Api;
import com.wsk.life.config.JSSecret;
import com.wsk.life.config.UrlParamPair;
import com.wsk.life.config.WyySpider;
import com.wsk.life.config.redis.IRedisUtils;
import com.wsk.life.config.tool.JSONUtil;
import com.wsk.life.config.tool.Time;
import com.wsk.life.config.tool.Tool;
import com.wsk.life.service.music.*;
import com.wsk.life.service.musicuser.UserGedanRepository;
import com.wsk.life.service.musicuser.UserRepository;
import com.wsk.life.service.musicuser.UserService;
import org.hibernate.exception.DataException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserServiceImpl implements UserService {
    private final IRedisUtils redisUtils;
    private final UserRepository userRepository;
    private final UserGedanRepository userdanRepository;
    @Autowired
    public UserServiceImpl(UserGedanRepository userdanRepository,IRedisUtils redisUtils,UserRepository userRepository) {
        this.redisUtils = redisUtils;
        this.userRepository=userRepository;
        this.userdanRepository=userdanRepository;
    }
    //添加用户
    @Override
    public void addUser(String name) throws IOException{
        WangYiUser model=new WangYiUser();
        //按照名字搜索用户的信息
        String comment=comment(name);
        JSONObject myJsonObject1 = JSONObject.parseObject(comment);
        String value1=myJsonObject1.getString("result");
        JSONObject myJsonObject2 = JSONObject.parseObject(value1);
        String value2=myJsonObject2.getString("userprofiles");
        JSONArray myJsonArray = JSONArray.parseArray(value2);
        System.out.println("myJsonArray:"+myJsonArray);
        if(myJsonArray!=null){
            for(int i=0 ; i < myJsonArray.size();i++) {
                JSONObject myjObject = myJsonArray.getJSONObject(i);
                String userid=myjObject.getString("userId");//id
                String username=myjObject.getString("nickname");//名称
                if(userRepository.findOneUser(userid)!=null){
                    System.out.println("跳过");
                    continue;
                }
                else{
                    System.out.println("未存在");
                    String userqian=myjObject.getString("signature");//个签
                    String userimg=myjObject.getString("avatarUrl");//头像
                    String userbackimg=myjObject.getString("backgroundUrl");//背景图
                    String userguan=myjObject.getString("follows");//关注
                    String userbei=myjObject.getString("followeds");//粉丝
                    String usergedan=myjObject.getString("playlistCount");//歌单
                    String url="https://music.163.com/user/home?id="+userid;
                    Document document = Jsoup.connect(url).get();
                    Element eles=document.getElementsByTag("script").get(0);
                    String script = eles.childNode(0).toString();
                    JSONObject myjObject0 = myJsonArray.parseObject(script);
                    String userpubdate=myjObject0.getString("pubDate");//注册日期
                    Element detail=document.getElementsByClass("m-proifo f-cb").get(0).getElementsByClass("inf s-fc3").get(0);
                    String usercity=detail.text();//城市              //听歌数量
                    String userlistennum=document.getElementsByClass("u-title u-title-1 f-cb m-record-title f-hide").get(0).getElementsByTag("h4").get(0).text();
                    //存储
                    Map<String, Object> data = new HashMap<String, Object>();
                    data.put("id", userid);
                    data.put("name", username);
                    data.put("geqian", userqian);
                    data.put("img", userimg);
                    data.put("backimg", userbackimg);
                    data.put("guanzhu", userguan);
                    data.put("fensi",userbei);
                    data.put("pubdate", userpubdate);
                    data.put("gedan",usergedan);
                    data.put("city",usercity);
                    data.put("listennum",userlistennum);
                    //将
                    JSONObject obj = new JSONObject(data);
                    try {
                        model = JSONObject.parseObject(obj.toString(), WangYiUser.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        userRepository.save(model);
                    } catch (DataException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public static String comment(String music_name){
        String list="";
        try {
            UrlParamPair upp = Api.SearchMusicList(music_name,"1002");
            String req_str = upp.getParas().toJSONString();
            System.out.println("req_str:"+req_str);
            Connection.Response
                    response = Jsoup.connect("http://music.163.com/weapi/cloudsearch/get/web?csrf_token=")
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:57.0) Gecko/20100101 Firefox/57.0")
                    .header("Accept", "*/*")
                    .header("Cache-Control", "no-cache")
                    .header("Connection", "keep-alive")
                    .header("Host", "music.163.com")
                    .header("Accept-Language", "zh-CN,en-US;q=0.7,en;q=0.3")
                    .header("DNT", "1")
                    .header("Pragma", "no-cache")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .data(JSSecret.getDatas(req_str))
                    .method(Connection.Method.POST)
                    .ignoreContentType(true)
                    .timeout(10000)
                    .execute();
            list = response.body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    //添加歌单
    @Override
    public void addGedan(String userid) throws IOException{
        UserGedan model=new UserGedan();
        //根据用户的id获取相应的歌单信息
        String comment = comment1(userid);
        JSONObject myJsonObject1 = JSONObject.parseObject(comment);
        String value1 = myJsonObject1.getString("playlist");
        JSONArray myJsonArray = JSONArray.parseArray(value1);
        for(int i=0 ; i < myJsonArray.size();i++) {
            JSONObject myjObject = myJsonArray.getJSONObject(i);
            String id_gedan=myjObject.getString("id");
            if(userdanRepository.findOneGedan(id_gedan)!=null){
                System.out.println("跳过");
                continue;
            }
            else{
                String name_gedan=myjObject.getString("name");
                String img_gedan=myjObject.getString("coverImgUrl");
                String descrption=myjObject.getString("description");
                String num_gedan=myjObject.getString("playCount");
                //存储
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("id", id_gedan);
                data.put("userid", userid);
                data.put("name", name_gedan);
                data.put("img", img_gedan);
                data.put("description",descrption);
                data.put("num",num_gedan);
                //将
                JSONObject obj = new JSONObject(data);
                try {
                    model = JSONObject.parseObject(obj.toString(), UserGedan.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    userdanRepository.save(model);
                } catch (DataException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static String comment1(String uid){
        String list="";
        try {
            UrlParamPair upp = Api.getPlaylistOfUser(uid);
            System.out.println("UrlParamPair:"+upp.toString());
            String req_str = upp.getParas().toJSONString();
            System.out.println("req_str:"+req_str);
            JSSecret.getDatas(req_str);
            Connection.Response
                    response = Jsoup.connect("http://music.163.com/weapi/user/playlist?csrf_token=")
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:57.0) Gecko/20100101 Firefox/57.0")
                    .header("Accept", "*/*")
                    .header("Cache-Control", "no-cache")
                    .header("Connection", "keep-alive")
                    .header("Host", "music.163.com")
                    .header("Accept-Language", "zh-CN,en-US;q=0.7,en;q=0.3")
                    .header("DNT", "1")
                    .header("Pragma", "no-cache")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .data(JSSecret.getDatas(req_str))
                    .method(Connection.Method.POST)
                    .ignoreContentType(true)
                    .timeout(10000)
                    .execute();
            list = response.body();
//            System.out.println(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    @Override
    public List<WangYiUser> findUser(String name){
        WangYiUser model=new WangYiUser();
        List<WangYiUser> result = new ArrayList<>();
        List<String> id_name=userRepository.findUser();
        String id="";
        for(String n:id_name){
//            System.out.println(n);
            String index=n.substring(0,n.indexOf(","));
            System.out.println(n.substring(index.length()+1,n.length()));
            if(n.substring(index.length()+1,n.length()).contains(name)){
                id=n.substring(0,n.indexOf(","));
                model=userRepository.findOneUser(id);
                result.add(model);
            }
        }
        return result;
    }

}
