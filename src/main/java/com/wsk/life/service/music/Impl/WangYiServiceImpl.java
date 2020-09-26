package com.wsk.life.service.music.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wsk.life.bean.music.*;
import com.wsk.life.config.JSSecret;
import com.wsk.life.config.WyySpider;
import com.wsk.life.config.tool.JSONUtil;
import com.wsk.life.config.tool.Time;
import com.wsk.life.config.tool.Tool;
import com.wsk.life.service.music.*;
import  com.wsk.life.config.Api;
import  com.wsk.life.config.UrlParamPair;
import com.wsk.life.config.redis.IRedisUtils;
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
public class WangYiServiceImpl implements WangYiService {
    private final IRedisUtils redisUtils;
    private final MusicRepository musicRepository;
    private final AllMusicsRepository allmusicsRepository;
    private final SingerRepository singerRepository;
    private final SingerAlbumRepository singerAlbumRepository;
    private final SingerMVRepository singerMVRepository;
    private final AlbumRepository albumRepository;
    @Autowired
    public WangYiServiceImpl(AlbumRepository albumRepository,SingerMVRepository singerMVRepository,SingerAlbumRepository singerAlbumRepository,AllMusicsRepository allmusicsRepository,MusicRepository musicRepository,SingerRepository singerRepository,IRedisUtils redisUtils) {
        this.redisUtils = redisUtils;
        this.musicRepository=musicRepository;
        this.singerRepository=singerRepository;
        this.allmusicsRepository=allmusicsRepository;
        this.singerAlbumRepository=singerAlbumRepository;
        this.singerMVRepository=singerMVRepository;
        this.albumRepository=albumRepository;
    }
    /* 获取10种热门音乐
     * @param url
     * @return
     */
    @Override
    public List<WangYiResponseEntity> randMusics(String type){ return allmusicsRepository.randMusics(type); }

    @Override
    public WangYiTypeEntity getType(String id_type) throws IOException {
        WangYiTypeEntity result=new WangYiTypeEntity();
        result=musicRepository.findType(id_type);
        if (result!= null) {
            System.out.println("已存在");
            return result;
        }
        else{
            System.out.println("不存在");
            String url="https://music.163.com/discover/toplist?id="+id_type;
            Document document = Jsoup.connect(url).get();
//        System.out.println(document+"\n\n");
            Element music=document.getElementsByClass("g-wrap").get(0);
            //歌单名称+歌单名片+歌单更新日期
            Set<String> pics = new HashSet<String>();
            String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
            Pattern p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
            Matcher m_image = p_image.matcher(music.getElementsByTag("img").toString());
            while (m_image.find()) {
                String img = m_image.group();
                Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
                while (m.find()) {
                    pics.add(m.group(1));
                }
            }
            String pic_movie = pics.toString();//图片地址+[]
            String image_type= pic_movie.substring(1, pic_movie.length() - 1);//mingpian
            Element detail=music.getElementsByClass("cnt").get(0);
            String title_type=detail.getElementsByTag("h2").get(0).text();
            String time_type=detail.getElementsByClass("sep s-fc3").get(0).text()+detail.getElementsByClass("s-fc4").get(0).text();
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("id", id_type);
            data.put("title", title_type);
            data.put("image", image_type);
            data.put("time", time_type);
            JSONObject obj = new JSONObject(data);
            try {
                result = JSONObject.parseObject(obj.toString(), WangYiTypeEntity.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                musicRepository.save(result);
            } catch (DataException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    @Override
    public void getHot(String id_type,String type) throws IOException {
        WangYiResponseEntity model=new WangYiResponseEntity();
        //从网站获取
        String url="https://music.163.com/discover/toplist?id="+id_type;
        Document document = Jsoup.connect(url).get();
        //System.out.println(document+"\n\n");
        //该歌单所有歌曲信息
        Element text=document.getElementsByTag("textarea").get(0);
        String script = text.childNode(0).toString();
        JSONArray myJsonArray=JSONArray.parseArray(script);
        String artists="";
        String artistsid="";
        for(int i=0 ; i < myJsonArray.size();i++) {//获取每一个JsonObject对象
            JSONObject myjObject = myJsonArray.getJSONObject(i);
//            System.out.println(myjObject);
            //歌曲分类
            String id_music = myjObject.getString("id");
            if (allmusicsRepository.findmusic(id_music,type) != null) {
                System.out.println("跳过flase");
                continue;
            } else {
                System.out.println("没有歌曲信息");
                String title_music = myjObject.getString("name");
                String play_music = "http://music.163.com/song/media/outer/url?id=" + id_music + ".mp3";
                String album = myjObject.getString("album");
                JSONObject myjObject1 = JSONObject.parseObject(album);
                String img_music = myjObject1.getString("picUrl");
                String album_music=myjObject1.getString("name");
                String albumid_music = myjObject1.getString("id");
                String artist = myjObject.getString("artists");
                JSONArray myJsonArray1 = JSONArray.parseArray(artist);
                for (int j = 0; j < myJsonArray1.size(); j++) {//获取每一个JsonObject对象
                    JSONObject myjObject2 = myJsonArray1.getJSONObject(j);
                    artists = myjObject2.getString("name");
                    artistsid = myjObject2.getString("id");
                    break;
                }
                //存储
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("id", id_music);
                data.put("title", title_music);
                data.put("img", img_music);
                data.put("play", play_music);
                data.put("album", album_music);
                data.put("urlalbum", albumid_music);
                data.put("singer", artists);
                data.put("urlsinger", artistsid);
                data.put("type",type);
                data.put("status",1);
                //将
                JSONObject obj = new JSONObject(data);
                try {
                    model = JSONObject.parseObject(obj.toString(), WangYiResponseEntity.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    allmusicsRepository.save(model);
                } catch (DataException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<WangYiContent> findcontentbyid(String id) throws IOException{
        List<WangYiContent> result = new ArrayList<>();
        WangYiContent model = new WangYiContent();
        //从redis中获取
        String movie = redisUtils.hget("musiccontent", id);
        if (Tool.getInstance().isNotNull(movie)) {
            System.out.println("yibaocun");
            JSONArray array = JSONArray.parseArray(movie);
            for (Object o : array) {
                try {
                    result.add(JSONUtil.toBean(o.toString(), WangYiContent.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            String comment=comment(id);
            JSONObject myJsonObject1 = JSONObject.parseObject(comment);
//            System.out.println(myJsonObject1);
            String value1=myJsonObject1.getString("hotComments");
            Random r = new Random();
            JSONArray myJsonArray = JSONArray.parseArray(value1);
            int flag=1;
            if(myJsonArray.size()==0){
                return null;
            }
            for(int i=0 ; i < myJsonArray.size();i++) {
                JSONObject myjObject = myJsonArray.getJSONObject(i);
                String user=myjObject.getString("user");
                JSONObject myjObject0 = JSONObject.parseObject(user);
                String name_user=myjObject0.getString("nickname");
                String image_user=myjObject0.getString("avatarUrl");
                String content_user=myjObject.getString("content");
                String time_user=randomDate("2016-7-25", "2020-7-25").toString();
                Double credit_user=(r.nextDouble()*5+5);
                //存储
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("id", flag);
                data.put("author", name_user);
                data.put("detail", content_user);
                data.put("credit", (String.format("%.1f", credit_user)));
                data.put("time", time_user);
                data.put("img",image_user);
                //将
                JSONObject obj = new JSONObject(data);
                try {
                    model = JSONObject.parseObject(obj.toString(), WangYiContent.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                result.add(model);
                redisUtils.hset("musiccontent", id, JSONUtil.toJson(result), Time.ONE_MONTH);//半个月更新一次
                flag=flag+1;
            }
        }
        return result;
    }

    public static Date randomDate(String beginDate, String endDate){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date start = format.parse(beginDate);// 构造开始日期
            Date end = format.parse(endDate);// 构造结束日期
            // getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
            if (start.getTime() >= end.getTime()) {
                return null;
            }
            long date = random(start.getTime(), end.getTime());

            return new Date(date);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    private static long random(long begin,long end){
        long rtn = begin + (long)(Math.random() * (end - begin));
        //如果返回的是开始时间和结束时间，通过递归调用本函数查找随机值
        if(rtn == begin || rtn == end){
            return random(begin,end);
        }
        return rtn;
    }

    public static String comment(String id){
        String list="";
        try {
                UrlParamPair upp = Api.getDetailOfPlaylist(id);
                String req_str = upp.getParas().toJSONString();
//            System.out.println("req_str:"+req_str);
                //某个歌的评论json地址
                //http://music.163.com/weapi/v1/resource/comments/R_SO_4_516392300?csrf_token=1ac15bcb947b3900d9e8e6039d121a81
                Connection.Response
                        response = Jsoup.connect("http://music.163.com/weapi/v1/resource/comments/R_SO_4_"+id+"?csrf_token=")
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
            }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override//从数据库搜索
    public WangYiResponseEntity findmusicByName(String name){
        WangYiResponseEntity model=new WangYiResponseEntity();
        List<String> id_name=allmusicsRepository.findMusics();
        String id="";
        for(String n:id_name){
            System.out.println(n);
            String index=n.substring(0,n.indexOf(","));
            System.out.println(n.substring(index.length()+1,n.length()));
            if(name.contains(n.substring(index.length()+1,n.length()))){
                id=n.substring(0,n.indexOf(","));
                break;
            }
        }
        System.out.println(id);
        model=allmusicsRepository.findmusicbyid(id);
        return model;
    }
    @Override
    public List<WangYiLyricEntity> findlyric(String id) throws IOException {
        List<WangYiLyricEntity> result = new ArrayList<>();
        WangYiLyricEntity model = new WangYiLyricEntity();
        //从redis中获取
        String movie = redisUtils.hget("musiclyric", id);
        if (Tool.getInstance().isNotNull(movie)) {
            System.out.println("yibaocun");
            JSONArray array = JSONArray.parseArray(movie);
            for (Object o : array) {
                try {
                    result.add(JSONUtil.toBean(o.toString(), WangYiLyricEntity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else{
            System.out.println("weibaocun");
            Document doc= WyySpider.spider(id);
            String ly="";
            int i=1;
            if(null!=doc) {
                String detail_music=doc.text();
                JSONObject myJsonObject = JSONObject.parseObject(detail_music);
                String lrc=myJsonObject.getString("lrc");
                JSONObject myJsonObject1 = JSONObject.parseObject(lrc);
                if(null!=myJsonObject1) {
                    ly=myJsonObject1.toString().substring(10,myJsonObject1.toString().length()-15);
                    System.out.println(ly+"\n\n");
                    String [] split=ly.split("\\\\n");
                    for(String info:split){
                        String index=info.substring(0,info.indexOf("]"));
                        String lyrics=info.substring(index.length()+1,info.length());
                        System.out.println(lyrics);
                        //存储
                        Map<String, Object> data = new HashMap<String, Object>();
                        data.put("id", i);
                        data.put("title", lyrics);
                        //将
                        JSONObject obj = new JSONObject(data);
                        try {
                            model = JSONObject.parseObject(obj.toString(), WangYiLyricEntity.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        result.add(model);
                        redisUtils.hset("musiclyric", id, JSONUtil.toJson(result), Time.ONE_MONTH);//半个月更新一次
                        i=i+1;
                    }
                }
            }
        }
        return result;
    }
    //获取歌手详细信息
    @Override
    public List<WangYiSingerDetailEntity> findsingerdetail(String id, Integer flag) throws IOException {
        //作者个人信息(id,name,个人简介:个人简介，代表作品，歌曲，从业经历，主要成就，荣誉记录（6个）)
        String url_detail="https://music.163.com/artist/desc?id="+id;
        Document doc_detail = Jsoup.connect(url_detail).get();
        if(flag==1){
            Element simple_detail=doc_detail.getElementsByClass("n-artist f-cb").get(0);
            String name_detail=simple_detail.getElementsByTag("h2").get(0).text();
            Set<String> pics = new HashSet<String>();
            String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
            Pattern p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
            Matcher m_image = p_image.matcher(simple_detail.getElementsByTag("img").toString());
            while (m_image.find()) {
                String img = m_image.group();
                Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
                while (m.find()) {
                    pics.add(m.group(1));
                }
            }
            String pic_movie = pics.toString();//图片地址+[]
            String image_detail= pic_movie.substring(1, pic_movie.length() - 1);
            WangYiSingerEntity result=new WangYiSingerEntity();
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("id", id);
            data.put("name", name_detail);
            data.put("img", image_detail);
            JSONObject obj = new JSONObject(data);
            try {
                result = JSONObject.parseObject(obj.toString(), WangYiSingerEntity.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                singerRepository.save(result);
            } catch (DataException e) {
                e.printStackTrace();
            }
        }
        List<WangYiSingerDetailEntity> result = new ArrayList<>();
        WangYiSingerDetailEntity model = new WangYiSingerDetailEntity();
        //从redis中获取
        String movie = redisUtils.hget("singerdetail", id);
        if (Tool.getInstance().isNotNull(movie)) {
            JSONArray array = JSONArray.parseArray(movie);
            for (Object o : array) {
                try {
                    result.add(JSONUtil.toBean(o.toString(), WangYiSingerDetailEntity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result;
        }
        Element singer_detail=doc_detail.getElementsByClass("n-artdesc").get(0);
        Elements h2_detail=singer_detail.getElementsByTag("h2");
        String title_detail[]=new String[10];
        String de_detail[]=new String[10];
        int i=0;
        for(Element info:h2_detail){
            title_detail[i]=info.text();
            Element p=singer_detail.getElementsByTag("p").get(i);
            de_detail[i]=p.text();
            //存储
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("id", i+1);
            data.put("title", title_detail[i]);
            data.put("titledel", de_detail[i]);
            //将
            JSONObject obj = new JSONObject(data);
            try {
                model = JSONObject.parseObject(obj.toString(), WangYiSingerDetailEntity.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            result.add(model);
            redisUtils.hset("singerdetail", id, JSONUtil.toJson(result), Time.HALF_OF_MONTH);//半个月更新一次
            i=i+1;
        }
        return result;
    }
    //获取歌手相关歌曲
    @Override
    public void findsingermusic(String id) throws IOException{
        WangYiResponseEntity model = new WangYiResponseEntity();
        //从网上获取
        String url_music="https://music.163.com/artist?id="+id;
        Document doc_music = Jsoup.connect(url_music).get();
//        System.out.println(doc_music+"\n\n");
        Element songs_music=doc_music.getElementsByClass("f-cb").get(0);
        Element text_music=songs_music.getElementsByTag("textarea").get(0);
        String script = text_music.childNode(0).toString();
        JSONArray myJsonArray=JSONArray.parseArray(script);
        String artists=singerRepository.findNameById(id);
        String artistsid=id;
        for(int i=0 ; i < myJsonArray.size();i++) {//获取每一个JsonObject对象
            JSONObject myjObject = myJsonArray.getJSONObject(i);
            //歌曲分类
            String id_music = myjObject.getString("id");
            if (allmusicsRepository.findmusic(id_music, id) != null) {
                System.out.println("跳过flase");
                continue;
            } else {
                String title_music = myjObject.getString("name");
                String play_music = "http://music.163.com/song/media/outer/url?id=" + id_music + ".mp3";
                String album = myjObject.getString("album");
                JSONObject myjObject1 = JSONObject.parseObject(album);
                String img_music = myjObject1.getString("picUrl");
                String album_music=myjObject1.getString("name");
                String albumid_music = myjObject1.getString("id");
                String artist = myjObject.getString("artists");
                JSONArray myJsonArray1 = JSONArray.parseArray(artist);
                for (int j = 0; j < myJsonArray1.size(); j++) {//获取每一个JsonObject对象
                    JSONObject myjObject2 = myJsonArray1.getJSONObject(j);
                    artists = myjObject2.getString("name");
                    artistsid = myjObject2.getString("id");
                    break;
                }
                //存储
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("id", id_music);
                data.put("title", title_music);
                data.put("img", img_music);
                data.put("play", play_music);
                data.put("album", album_music);
                data.put("urlalbum", albumid_music);
                data.put("singer", artists);
                data.put("urlsinger", artistsid);
                data.put("type",id);
                data.put("status",1);
                //将
                JSONObject obj = new JSONObject(data);
                try {
                    model = JSONObject.parseObject(obj.toString(), WangYiResponseEntity.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    allmusicsRepository.save(model);
                } catch (DataException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //获取歌手相关专辑
    @Override
    public void findsingeralbum(String id) throws IOException{
        WangYiAlbumEntity model = new WangYiAlbumEntity();
        //所有专辑https://music.163.com/#/artist/album?id=5781
        String url_album="https://music.163.com/artist/album?id="+id;
        Document doc_album = Jsoup.connect(url_album).get();
        Element ul=doc_album.getElementsByClass("g-wrap6").get(0).getElementsByTag("ul").get(1);
        Elements lis=ul.getElementsByTag("li");
        for(Element info:lis) {
            String idyuan_album = info.getElementsByTag("a").get(0).toString();
            String id_album = idyuan_album.substring(19, idyuan_album.length() - 18);
            if (singerAlbumRepository.findAlbumById(id_album, id) != null) {
                System.out.println("跳过flase");
                continue;
            } else {
                System.out.println("专辑不存在");
                Set<String> pics = new HashSet<String>();
                String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
                Pattern p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
                Matcher m_image = p_image.matcher(info.getElementsByTag("img").toString());
                while (m_image.find()) {
                    String img = m_image.group();
                    Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
                    while (m.find()) {
                        pics.add(m.group(1));
                    }
                }
                String pic_movie = pics.toString();//图片地址+[]
                String image_album = pic_movie.substring(1, pic_movie.length() - 1);
                String title_album = info.getElementsByTag("p").get(0).text();
                String time_album = info.getElementsByTag("p").get(1).text();
                //存储
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("id", id_album);
                data.put("singerid", id);
                data.put("title", title_album);
                data.put("img", image_album);
                data.put("time", time_album);
                //将
                JSONObject obj = new JSONObject(data);
                try {
                    model = JSONObject.parseObject(obj.toString(), WangYiAlbumEntity.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    singerAlbumRepository.save(model);
                } catch (DataException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //获取歌手相关MV
    @Override
    public void findsingerMV(String id) throws IOException{
        WangYiMVEntity model = new WangYiMVEntity();
        //从网上获取
        String url_mv="https://music.163.com/artist/mv?id="+id;
        Document doc_mv = Jsoup.connect(url_mv).get();
        Element ul=doc_mv.getElementsByClass("g-wrap6").get(0).getElementsByTag("ul").get(1);
        Elements lis=ul.getElementsByTag("li");
        int i=0;
        for(Element info:lis){
            String idyuan_mv=info.getElementsByTag("a").get(0).toString();
            String id_mv=idyuan_mv.substring(16,idyuan_mv.length()-18);
            System.out.println(idyuan_mv);
            System.out.println(id_mv);
            if (singerMVRepository.findMVById(id_mv, id) != null) {
                System.out.println("跳过flase");
                continue;
            } else {
                System.out.println("MV存在");
                Set<String> pics = new HashSet<String>();
                String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
                Pattern p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
                Matcher m_image = p_image.matcher(info.getElementsByTag("img").toString());
                while (m_image.find()) {
                    String img = m_image.group();
                    Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
                    while (m.find()) {
                        pics.add(m.group(1));
                    }
                }
                String pic_movie = pics.toString();//图片地址+[]
                String image_mv= pic_movie.substring(1, pic_movie.length() - 1);
                String title_mv=info.getElementsByTag("p").get(0).text();
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("id", id_mv);
                data.put("singerid",id);
                data.put("title",title_mv);
                data.put("img", image_mv);
                //将
                JSONObject obj = new JSONObject(data);
                try {
                    model = JSONObject.parseObject(obj.toString(), WangYiMVEntity.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    singerMVRepository.save(model);
                } catch (DataException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //获取专辑相关信息
    @Override
    public void findalbum(String id) throws IOException{
        WangYiAlbum model = new WangYiAlbum();
        WangYiResponseEntity result = new WangYiResponseEntity();
        String url="https://music.163.com/album?id="+id;
        Document doc = Jsoup.connect(url).get();
//        System.out.println(doc);
        Element detail=doc.getElementsByClass("g-mn4c").get(0);
        Set<String> pics = new HashSet<String>();
        String regEx_img = "<img.*data-src\\s*=\\s*(.*?)[^>]*?>";
        Pattern p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        Matcher m_image = p_image.matcher(detail.getElementsByTag("img").toString());
        while (m_image.find()) {
            String img = m_image.group();
            Matcher m = Pattern.compile("data-src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        String pic_movie = pics.toString();//图片地址+[]
        String img_album= pic_movie.substring(1, pic_movie.length() - 1);
        Element detail1=detail.getElementsByClass("topblk").get(0);
        String title_album=detail1.getElementsByTag("h2").get(0).text();
        Elements p=detail1.getElementsByTag("p");
        String s[]=new String[3];
        int o=0;
        String singerid_album="";
        for(Element info:p){
            s[o]=info.text();
            if(s[o].substring(0,2).equals("歌手")){
                String singerid=info.getElementsByTag("a").get(0).toString();
                pics = new HashSet<String>();
                regEx_img = "<a.*href\\s*=\\s*(.*?)[^>]*?>";
                p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
                m_image = p_image.matcher(singerid);
                while (m_image.find()) {
                    String img = m_image.group();
                    Matcher m = Pattern.compile("href\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
                    while (m.find()) {
                        pics.add(m.group(1));
                    }
                }
                pic_movie = pics.toString();//图片地址+[]
                String singerm= pic_movie.substring(1, pic_movie.length() - 1);
                System.out.println(singerm);
                singerid_album=singerm.substring(11,singerm.length());
                System.out.println(singerid_album);
            }
            else{
                s[o]=info.text();
            }
            o=o+1;
        }
        String detail_album=detail.getElementsByClass("n-albdesc").get(0).getElementsByTag("p").get(0).text();
        //存储
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("id", id);
        data.put("singerid", singerid_album);
        data.put("singername", s[0]);
        data.put("title", title_album);
        data.put("img", img_album);
        data.put("time", s[1]);
        data.put("company", s[2]);
        data.put("detail", detail_album);
        //将
        JSONObject obj = new JSONObject(data);
        try {
            model = JSONObject.parseObject(obj.toString(), WangYiAlbum.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            albumRepository.save(model);
        } catch (DataException e) {
            e.printStackTrace();
        }
        Element text_music=detail.getElementsByTag("textarea").get(0);
        String script = text_music.childNode(0).toString();
        JSONArray myJsonArray=JSONArray.parseArray(script);
        String artists="";
        String artistsid="";
        for(int i=0 ; i < myJsonArray.size();i++) {//获取每一个JsonObject对象
            JSONObject myjObject = myJsonArray.getJSONObject(i);
            //歌曲分类
            String id_music = myjObject.getString("id");
            if (allmusicsRepository.findmusic(id_music, id) != null) {
                System.out.println("跳过flase");
                continue;
            } else {
                System.out.println("专辑歌曲未获取");
                String title_music = myjObject.getString("name");
                String play_music = "http://music.163.com/song/media/outer/url?id=" + id_music + ".mp3";
                String album = myjObject.getString("album");
                JSONObject myjObject1 = JSONObject.parseObject(album);
                String img_music = myjObject1.getString("picUrl");
                String album_music = myjObject1.getString("name");
                String albumid_music = myjObject1.getString("id");
                String artist = myjObject.getString("artists");
                JSONArray myJsonArray1 = JSONArray.parseArray(artist);
                for (int j = 0; j < myJsonArray1.size(); j++) {//获取每一个JsonObject对象
                    JSONObject myjObject2 = myJsonArray1.getJSONObject(j);
                    artists = myjObject2.getString("name");
                    artistsid = myjObject2.getString("id");
                    break;
                }
                //存储
                Map<String, Object> data1 = new HashMap<String, Object>();
                data1.put("id", id_music);
                data1.put("title", title_music);
                data1.put("img", img_music);
                data1.put("play", play_music);
                data1.put("album", album_music);
                data1.put("urlalbum", albumid_music);
                data1.put("singer", artists);
                data1.put("urlsinger", artistsid);
                data1.put("type",id);
                data1.put("status",1);
                //将
                JSONObject obj1 = new JSONObject(data1);
                try {
                    result = JSONObject.parseObject(obj1.toString(), WangYiResponseEntity.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    allmusicsRepository.save(result);
                } catch (DataException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void findusermusic(String gedanid) throws IOException{
        WangYiResponseEntity result = new WangYiResponseEntity();
        String url="https://music.163.com/playlist?id="+gedanid;
        Document document = Jsoup.connect(url).get();
        Element ele=document.getElementById("song-list-pre-cache");
        ele=ele.getElementsByTag("ul").get(0);
        Elements lis=ele.getElementsByTag("li");
        for(Element info:lis) {
            String id_music = Img(info.toString()).substring(9);
            if (allmusicsRepository.findmusic(id_music, gedanid) != null) {
                System.out.println("用户已存歌单歌曲");
                continue;
            } else {
                System.out.println("歌单歌曲不存在");
                //结界————————————————————————结界
                String url1 = "https://music.163.com/song?id=" + id_music;
                Document document1 = Jsoup.connect(url1).get();
                Element ele1 = document1.getElementsByClass("m-lycifo").get(0);
                String title_music = ele1.getElementsByTag("em").get(0).text();
                Set<String> pics = new HashSet<String>();
                String regEx_img = "<img.*data-src\\s*=\\s*(.*?)[^>]*?>";
                Pattern p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
                Matcher m_image = p_image.matcher(ele1.getElementsByTag("img").toString());
                while (m_image.find()) {
                    String img = m_image.group();
                    Matcher m = Pattern.compile("data-src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
                    while (m.find()) {
                        pics.add(m.group(1));
                    }
                }
                String pic_movie = pics.toString();//图片地址+[]
                String img_music = pic_movie.substring(1, pic_movie.length() - 1);
                Element p1a1 = ele1.getElementsByTag("p").get(0).getElementsByTag("a").get(0);
                String singer_music = p1a1.text();//;//12174521
                String urlsinger_music = Img(p1a1.toString()).substring(11);
                Element p2a1 = ele1.getElementsByTag("p").get(1).getElementsByTag("a").get(0);
                String album_music = p2a1.text();//;//75853338
                String urlalbum_music = Img(p2a1.toString()).substring(10);
                String play_music="http://music.163.com/song/media/outer/url?id=" + id_music + ".mp3";
                //存储
                Map<String, Object> data1 = new HashMap<String, Object>();
                data1.put("id", id_music);
                data1.put("title", title_music);
                data1.put("img", img_music);
                data1.put("play", play_music);
                data1.put("album", album_music);
                data1.put("urlalbum", urlalbum_music);
                data1.put("singer", singer_music);
                data1.put("urlsinger", urlsinger_music);
                data1.put("type",gedanid);
                data1.put("status",1);
                //将
                JSONObject obj1 = new JSONObject(data1);
                try {
                    result = JSONObject.parseObject(obj1.toString(), WangYiResponseEntity.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    allmusicsRepository.save(result);
                } catch (DataException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String Img(String image){
        Set<String> pics = new HashSet<String>();
        String regEx_img = "<a.*href\\s*=\\s*(.*?)[^>]*?>";
        Pattern p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        Matcher m_image = p_image.matcher(image);
        while (m_image.find()) {
            String img = m_image.group();
            Matcher m = Pattern.compile("href\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        String pic_movie = pics.toString();//图片地址+[]
        String img_music = pic_movie.substring(1, pic_movie.length() - 1);
        return img_music;
    }

}
