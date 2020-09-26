package com.wsk.life.service.movie.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wsk.life.bean.movie.maoyan.*;
import com.wsk.life.config.redis.IRedisUtils;
import com.wsk.life.config.tool.JSONUtil;
import com.wsk.life.config.tool.Time;
import com.wsk.life.config.tool.Tool;
import com.wsk.life.service.movie.HotMovieRepository;
import com.wsk.life.service.movie.SearchHotMovieService;
import com.wsk.life.service.movie.TopMovieRepository;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.hibernate.exception.DataException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @DESCRIPTION :
 * @AUTHOR : WuShukai1103
 * @TIME : 2018/2/15  15:00
 */
@Component
public class SearchHotMovieServiceImpl implements SearchHotMovieService {
    String url = "http://theater.mtime.com/China_Henan_Province_Zhengzhou/";//电影+影院
    private static HotMovieRepository hotMovieRepository;//即将上映+热映
    private static TopMovieRepository topMovieRepository;//top榜单
    private static IRedisUtils redisUtils;

    @Autowired
    public SearchHotMovieServiceImpl(HotMovieRepository hotMovieRepository,TopMovieRepository topMovieRepository,IRedisUtils redisUtils) {
        this.redisUtils = redisUtils;
        this.hotMovieRepository=hotMovieRepository;
        this.topMovieRepository=topMovieRepository;
    }
    @Override
    public List<Hot>[] searchHotMovies(int number) throws IOException {
        List<Hot> result1[] = new List[number];
        List<Hot> result2[] = new List[32];
        List<Hot> result3[] = new List[number];
        //从redis中获取
        int flag = 32/number;
        System.out.println("flag::::::::::::::::"+flag);
        int i, num = 1;
        for (i = 1; i <= number; i++) {
            int f = (int) (i * flag - flag + 1 + Math.random() * (flag));
            System.out.println("Redis--f::::::::::::::::"+f);
            String movies = redisUtils.hget("movie", String.valueOf(f));
            if(movies!=null){
                if (Tool.getInstance().isNotNull(movies)) {
                    JSONArray array = JSONArray.parseArray(movies);
                    for (Object o : array) {
                        try {
                            num++;
                            result1[i-1]=new ArrayList<>();
                            result1[i-1].add(JSONUtil.toBean(o.toString(), Hot.class));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            else{
                break;
            }
        }
        num=num-1;
        if (num == number) {
            return result1;
        }
        //redis 中没有，从豆瓣获取
        Document document = Jsoup.connect(url).get(); /*页面html内容*/
        //System.out.println(document);
        //正在热映,第一部分——后面几部电影<不要第一部电影了>
        Element movienow1 = document.getElementsByClass("othermovie fr").get(0);//System.out.println(movienow1);
        Elements linow = movienow1.getElementsByTag("li");
        result2= (List<Hot>[]) save1(linow,0,result2);
        //正在热映,第二部分——后面几部电影<不要第一部电影了>
        Element movienow2 = document.getElementsByClass("moviemore none").get(0);//System.out.println(movienow2);
        Elements linext = movienow2.getElementsByTag("li");
        result2= (List<Hot>[]) save1(linext,6,result2);
        for (i = 1; i <= number; i++) {
            int f = (int) (i * flag - flag + 1 + Math.random() * (flag));
            System.out.println("网上f::::::::::::::::"+f);
            result3[i-1]=result2[f-1];
        }
        return result3;
    }
    public List<Hot>[] save1(Elements li, int i, List<Hot>[] result){
        for(Element info:li) { /*遍历每一个li标签*/
            Element a1=info.getElementsByTag("a").get(0);
            //url
            String url_movie=a1.toString().substring(9,39);
            //电影id
            String id_movie=url_movie.substring(23,29);
            //title
            Element a2=info.getElementsByTag("a").get(1);
            String title_movie=a2.text();
            //image
            Set<String> pics = new HashSet<String>();
            String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
            Pattern p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
            Matcher m_image = p_image.matcher(a1.toString());
            while (m_image.find()) {
                String img = m_image.group();
                Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
                while (m.find()) {
                    pics.add(m.group(1));
                }
            }
            String pic_movie = pics.toString();//图片地址+[]
            String image_movie = pic_movie.substring(1, pic_movie.length() - 1);
            //TimeAndSytle
            Element dd0=info.getElementsByTag("dd").get(0);
            String TAS_movie=dd0.text();
            //detail
            Element dd1=info.getElementsByTag("dd").get(1);
            String detail_movie=dd1.text();
            if(detail_movie.length()==4){
                detail_movie=" ";
            }
            //进入电影的详情页面
            /* System.out.println(id_movie);
            System.out.println(title_movie);
            System.out.println(image_movie);
            System.out.println(TAS_movie);
            System.out.println(detail_movie);
            System.out.println("\n\n");*/
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("id", id_movie);
            data.put("title", title_movie);
            data.put("image", image_movie);
            data.put("TAS", TAS_movie);
            data.put("detail", detail_movie);
            data.put("date", " ");
            JSONObject obj = new JSONObject(data);
            Hot model = new Hot();
            try {
                model = JSONObject.parseObject(obj.toString(),Hot.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            result[i] = new ArrayList<>();
            result[i].add(model);
            redisUtils.hset("movie" ,String.valueOf(i+1), JSONUtil.toJson(result[i]), Time.ONE_DAY);
            i=i+1;
        }
        return result;
    }

    @Override//热映电影+即将上映+top电影
    public List<Hot> randMovies(Integer id) { return hotMovieRepository.randMovies(id); }
    @Override//top电影
    public List<Top> ranktop(){
        return topMovieRepository.randTop();
    }

    @Override//top电影
    public List<String> findMovieByName(){ return hotMovieRepository.findMovieByName(); }

    @Override//存入热映电影+即将上映
    public void InsertMovie(String title) throws IOException {
        Document document = Jsoup.connect(url).get(); /*页面html内容*/
        //System.out.println(document);
        if(title.equals("hot")){
            //正在热映,第一部分——后面几部电影<不要第一部电影了>
            Element movienow1 = document.getElementsByClass("othermovie fr").get(0);//System.out.println(movienow1);
            Elements linow = movienow1.getElementsByTag("li");
            save(linow,title);
            //正在热映,第二部分——后面几部电影<不要第一部电影了>
            Element movienow2 = document.getElementsByClass("moviemore none").get(0);//System.out.println(movienow2);
            Elements linext = movienow2.getElementsByTag("li");
            save(linext,title);
        }
        else if(title.equals("coming")){
            //即将上映,电影信息保存
            Element movienext = document.getElementsByClass("upcoming").get(0);//System.out.println(movienext);
            Elements ul = movienext.getElementsByTag("ul");
            save(ul,title);
        }
    }
    public void save(Elements li,String title) throws IOException {
        for(Element info:li) { /*遍历每一个li标签*/
            Element a1=info.getElementsByTag("a").get(0);
            //url
            String url_movie=a1.toString().substring(9,39);
            //电影id
            String id_movie=url_movie.substring(23,29);
            if (hotMovieRepository.findOneMovie(id_movie)!=null) {
                System.out.println("跳过flase");
                continue;
            }
            else{
                System.out.println("为跳过flase");
                String title_movie="";
                String date_movie="";
                String TAS_movie="";
                String detail_movie="";
                String director="";
                String bianju="";
                String country="";
                String company="";
                String more="";
                Integer type=0;
                if(title.equals("hot")){
                    //title
                    Element a2=info.getElementsByTag("a").get(1);
                    title_movie=a2.text();
                    //TimeAndSytle
                    Element dd0=info.getElementsByTag("dd").get(0);
                    TAS_movie=dd0.text();
                    //detail
                    Element dd1=info.getElementsByTag("dd").get(1);
                    detail_movie=dd1.text();
                    if(detail_movie.length()==4){
                        detail_movie=" ";
                    }
                    //类型
                    type=100;//热映电影
                }
                else if(title.equals("coming")){
                    //title
                    Element a2=info.getElementsByTag("h3").get(0);
                    title_movie=a2.text();
                    //date
                    Element date=info.getElementsByClass("day").get(0);
                    date_movie=date.text();
                    //TimeAndSytle
                    Element dd0=info.getElementsByTag("p").get(0);
                    TAS_movie=dd0.text();
                    //类型
                    type=101;//即将上映电影
                }
                //image
                Set<String> pics = new HashSet<String>();
                String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
                Pattern p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
                Matcher m_image = p_image.matcher(a1.toString());
                while (m_image.find()) {
                    String img = m_image.group();
                    Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
                    while (m.find()) {
                        pics.add(m.group(1));
                    }
                }
                String pic_movie = pics.toString();//图片地址+[]
                String image_movie = pic_movie.substring(1, pic_movie.length() - 1);
                //进入电影的详情页面
                if(id_movie.substring(id_movie.length()-1,id_movie.length())=="/"){
                    id_movie=id_movie.substring(0,id_movie.length()-1);
                    System.out.println(id_movie);
                }
                String URL="http://movie.mtime.com/"+id_movie+"/";
                Document document = Jsoup.connect(URL).get(); /*页面html内容*/
                Element movie = document.getElementsByClass("info_l").get(0);
                //System.out.println(movie);
                Elements dd=movie.getElementsByTag("dd");
                for(Element m:dd){
                    String some=m.text();
                    if(some.substring(0,2).equals("导演")){
                        director=some;
                    }
                    else if(some.substring(0,2).equals("编剧")){
                        bianju=some;
                    }
                    else if(some.substring(0,2).equals("国家")){
                        country=some;
                    }
                    else if((some.substring(0,2).equals("发行"))||(some.substring(0,2).equals("制作"))){
                        company=some;
                    }
                    else if(some.substring(0,2).equals("更多")){
                        more=some;
                    }
                }
                if(title.equals("coming")){
                    Elements dt=movie.getElementsByTag("dt");
                    if(dt.text().length()>80){
                        detail_movie = dt.text().substring(0, 80) + "...";
                    }
                }
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("id", id_movie);
                data.put("title", title_movie);
                data.put("image", image_movie);
                data.put("TAS", TAS_movie);
                data.put("detail", detail_movie);
                data.put("date", date_movie);
                data.put("status", 1);
                data.put("director",director);
                data.put("bianju",bianju);
                data.put("country", country);
                data.put("company", company);
                data.put("more", more);
                data.put("type", type);
                JSONObject obj = new JSONObject(data);
                Hot model = new Hot();
                try {
                    model = JSONObject.parseObject(obj.toString(),Hot.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    hotMovieRepository.save(model);
                } catch (DataException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override//top榜单概要
    public void top() throws IOException {
        String URL = "http://movie.mtime.com/list/";
        //日本动画+热血电竞+IMDb剧集+时光网评分+动画电影
        String url[] = {//7,13,15,8,
                URL+"1709.html", URL+"1689.html", URL+"1682.html", URL+"1658.html", URL+"1437.html",
                URL+"1398.html", URL+"1392.html", URL+"1357.html", URL+"1328.html", URL+"1687.html",
                URL+"1470.html", URL+"1351.html", URL+"1526.html", URL+"1685.html", URL+"1410.html",
                URL+"1492.html", URL+"217.html", URL+"1493.html", URL+"1400.html", URL+"1505.html",
                URL+"255.html",//1
                URL+"1506.html", URL+"1498.html", URL+"1496.html", URL+"1495.html",
                URL+"1511.html", URL+"1453.html", URL+"1518.html"};
        String image_top[]=new String[28];
        int id_top = 0,i=0;
        for (String u : url) {//top内容
            id_top = id_top + 1;
            if (topMovieRepository.findOneTop(id_top) != null) {
                System.out.println("跳过flase");
                continue;
            } else {
                System.out.println("未跳过flase");
                Document document = Jsoup.connect(u).get(); /*页面html内容*/
                String title_top = document.getElementsByTag("h2").get(0).text();
                String detail_top = document.getElementsByTag("h3").get(0).text();
                if(detail_top.length()>1024){
                    detail_top=detail_top.substring(0,1024);
                }
                if(i<9){
                    Element head = document.getElementsByClass("top_headbg3").get(0);
                    image_top[i] = head.toString().substring(53, head.toString().length() - 10);
                }
                else if(i>8){
                    image_top[i]="http://pic1.cxtuku.com/00/10/32/b20002f1052e.jpg";
                }
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("id", id_top);
                data.put("title", title_top);
                data.put("detail", detail_top);
                data.put("image", image_top[i]);
                JSONObject obj = new JSONObject(data);
                Top model = new Top();
                try {
                    model = JSONObject.parseObject(obj.toString(), Top.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    topMovieRepository.save(model);
                } catch (DataException e) {
                    e.printStackTrace();
                }
            }
            i=i+1;
        }
    }
    @Override//存储各类电影信息//存储各类top对应的电影
    public void InsertTopMovie(Integer id_top) throws IOException {
        String URL = "http://movie.mtime.com/list/";
        //日本动画+热血电竞+IMDb剧集+时光网评分+动画电影
        String url[] = {//7,13,15,8,
                URL+"1709.html", URL+"1689.html", URL+"1682.html", URL+"1658.html", URL+"1437.html",
                URL+"1398.html", URL+"1392.html", URL+"1357.html", URL+"1328.html", URL+"1687.html",
                URL+"1470.html", URL+"1351.html", URL+"1526.html", URL+"1685.html", URL+"1410.html",
                URL+"1492.html", URL+"217.html", URL+"1493.html", URL+"1400.html", URL+"1505.html",
                URL+"255.html",//0
                URL+"1506.html", URL+"1498.html", URL+"1496.html", URL+"1495.html",
                URL+"1511.html", URL+"1453.html", URL+"1518.html"};
        int id=id_top-1;
        Document document = Jsoup.connect(url[id]).get(); /*页面html内容*/
        savetop(document,id_top);
    }
    public void savetop(Document document,Integer id) throws IOException {
        //电影信息
        Element topmovie = document.getElementsByClass("top_nlist").get(0);
        Elements dd = topmovie.getElementsByTag("dd");
        for (Element info : dd) {
            Element a = info.getElementsByTag("a").get(1);
            //id
            String url_movie = a.toString().substring(9, 39);
            String id_movie = url_movie.substring(23, 29);
            if (hotMovieRepository.findOneMovie(id_movie)!=null) {
                System.out.println("跳过flase");
                continue;
            }
            else {
                System.out.println("为跳过flase");
                String director_movie="";
                String bianju_movie="";
                String country_movie="";
                String company_movie="";
                String more_movie="";
                //title
                String title_movie = info.getElementsByTag("h3").get(0).text();
                //image
                Set<String> pics = new HashSet<String>();
                String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
                Pattern p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
                Matcher m_image = p_image.matcher(a.toString());
                while (m_image.find()) {
                    String img = m_image.group();
                    Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
                    while (m.find()) {
                        pics.add(m.group(1));
                    }
                }
                String pic_movie = pics.toString();//图片地址+[]
                String image_movie = pic_movie.substring(1, pic_movie.length() - 1);
                String detail_movie = info.getElementsByTag("p").get(2).text();
                System.out.println(id_movie);
                String URL="http://movie.mtime.com/"+id_movie+"/";
                Document document1 = Jsoup.connect(URL).get(); /*页面html内容*/
                String TAS_movie=document1.getElementsByClass("otherbox __r_c_").get(0).text();
                Element movie = document1.getElementsByClass("info_l").get(0);
                //System.out.println(movie);
                Elements dd0=movie.getElementsByTag("dd");
                for(Element m:dd0){
                    String some=m.text();
                    if(some.substring(0,2).equals("导演")){
                        director_movie=some;
                    }
                    else if(some.substring(0,2).equals("编剧")){
                        bianju_movie=some;
                    }
                    else if(some.substring(0,2).equals("国家")){
                        country_movie=some;
                    }
                    else if((some.substring(0,2).equals("发行"))||(some.substring(0,2).equals("制作"))){
                        company_movie=some;
                    }
                    else if(some.substring(0,2).equals("更多")){
                        more_movie=some;
                    }
                }
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("id", id_movie);
                data.put("title", title_movie);
                data.put("image", image_movie);
                data.put("TAS", TAS_movie);
                data.put("detail", detail_movie);
                data.put("date", "");
                data.put("status", 1);
                data.put("director",director_movie);
                data.put("bianju",bianju_movie);
                data.put("country", country_movie);
                data.put("company", company_movie);
                data.put("more", more_movie);
                data.put("type", id);
                JSONObject obj = new JSONObject(data);
                Hot model = new Hot();
                try {
                    model = JSONObject.parseObject(obj.toString(),Hot.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    hotMovieRepository.save(model);
                } catch (DataException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override//所有电影详情+包括按照id来获取和名字来获取
    public List<Movies> findMovieById(String id,String name) throws IOException {
        List<Movies> result = new ArrayList<>();
        String Title_name[]={
                "大圣娶亲","女儿国","借东西的小人","哈尔的移动城堡","你的名字", "萤火之森","蜘蛛侠","银河护卫队","欢乐喜剧人","复仇者联盟",
                "风之谷", "三体","狼图腾","生化危机","老师好","宠爱","沉睡魔咒","无主之城","小欢喜","仙3",
                "仙1","狮子王","姜子牙","封神传奇","哪吒","白蛇缘起", "夏洛特烦恼","美人鱼","守护甜心","捉1",
                "捉2","鬼吹灯","唐人街探案", "少年的你", "我和我的祖国","湄公河行动","战狼","小猪佩奇","勇敢的心","阿甘正传"};

        String id_name[]={
                "12175/","209205","122283","16909/","232556","150387","238037","216008","238020","218090",
                "10606/","264657","112443","194595","261858","263815","225794","257620","255410","87842/",
                "48870/","11810/","215121","209680","251525","260666","218763","209007","79695/","211794",
                "237182","260674","254785","259039","263505","228345","229733","259886","12493/","10054/"};
        int error=0;
        if((id==null) &&(name!=null)){
            String id_na[] = new String[500];
            for(int i=0;i<40;i++){
                if(Title_name[i].substring(0,2).equals(name.substring(0,2))){
                    id=id_name[i];
                    error=1;
                    break;
                }
            }
            if(error==0){
                List<String> flag=hotMovieRepository.findMovieByName();
                System.out.println(flag);
                int j=0;
                for(String n:flag){
                    id_na[j]=n.substring(0,6);
                    String na=n.substring(7,9);
                    System.out.println(na);
                    if(na.equals(name.substring(0,2))){
                        id=id_na[j];
                        System.out.println(id);
                        error=2;
                        break;
                    }
                    j=j+1;
                }
            }
        }
        else if((id!=null) &&(name==null)){
            error=3;
        }
        if(error==0){
            return null;
        }
        //从redis中获取
        String movie = redisUtils.hget("movie", id);
        if (Tool.getInstance().isNotNull(movie)) {
            JSONArray array = JSONArray.parseArray(movie);
            for (Object o : array) {
                try {
                    result.add(JSONUtil.toBean(o.toString(), Movies.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result;
        }
        String id_movie="";String title_movie="";String url_movie="";String image_movie="";
        String TAS_movie="";String detail_movie="";String director_movie="";String bianju_movie="";
        String country_movie="";String company_movie="";String more_movie="";String vedios_movie="";
        String images_movie="";String actors_movie="";String chats_movie="";String news_movie="";
        String actor_movie[]=new String[4];String actorimg_movie[][]=new String[4][2];
        String chatTitle_movie[]=new String[4];String chatAuthor_movie[]=new String[4];
        String chatImg_movie[]=new String[4];String chatContent_movie[]=new String[4];
        //redis 中没有，从豆瓣获取
        String URL="http://movie.mtime.com/"+id+"/";
        Document document = Jsoup.connect(URL).get(); /*页面html内容*/
        //System.out.println(document); System.out.println("\n\n");
        Element moviehead = document.getElementsByClass("db_head").get(0);
        id_movie=id;
        title_movie=moviehead.getElementsByTag("h1").get(0).text();
        url_movie=select(title_movie);
        Element image=document.getElementsByTag("img").get(0);
        Set<String> pics = new HashSet<String>();
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        Pattern p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        Matcher m_image = p_image.matcher(image.toString());
        while (m_image.find()) {
            String img = m_image.group();
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        String pic_movie = pics.toString();//图片地址+[]
        image_movie = pic_movie.substring(1, pic_movie.length() - 1);
        TAS_movie=moviehead.getElementsByClass("otherbox __r_c_").get(0).text();
        Element movieother = document.getElementsByClass("db_nav").get(0);
        //System.out.println(movieother); System.out.println("\n\n");
        vedios_movie=movieother.getElementsByTag("dd").get(0).text();
        images_movie=movieother.getElementsByTag("dd").get(1).text();
        actors_movie=movieother.getElementsByTag("dd").get(2).text();
        chats_movie=movieother.getElementsByTag("dd").get(3).text();
        news_movie=movieother.getElementsByTag("dd").get(4).text();
        Element moviebody = document.getElementsByClass("base_r").get(0);
        //System.out.println(moviebody); System.out.println("\n\n");
        Element moviebody_1=moviebody.getElementsByClass("info_l").get(0);
        Elements dd=moviebody_1.getElementsByClass("__r_c_");
        for(Element m:dd){
            String some=m.text();
            if(some.substring(0,2).equals("导演")){
                director_movie=some;
            }
            else if(some.substring(0,2).equals("编剧")){
                bianju_movie=some;
            }
            else if(some.substring(0,2).equals("国家")){
                country_movie=some;
            }
            else if((some.substring(0,2).equals("发行"))||(some.substring(0,2).equals("制作"))){
                company_movie=some;
            }
            else if(some.substring(0,2).equals("更多")){
                more_movie=some;
            }
        }
        String url_detail=URL+"plots.html";
        Document  docu= Jsoup.connect(url_detail).get(); /*页面html内容*/
        //System.out.println(document); System.out.println("\n\n");
        Elements detail = docu.getElementsByClass("plots_box");
        detail_movie="";
        for(Element info:detail){
            detail_movie=detail_movie+info.text();
        }
        System.out.println(detail_movie);
        Element moviebody_2=moviebody.getElementsByClass("info_r").get(0);
        Elements mainactor=moviebody_2.getElementsByClass("main_actor");
        int i=0;
        for(Element info:mainactor){
            int j=0;
            Elements imgmovie=info.getElementsByTag("img");
            actor_movie[i]=info.text();
            for(Element info1:imgmovie){
                pics = new HashSet<String>();
                regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
                p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
                Matcher n_image = p_image.matcher(info1.toString());
                while (n_image.find()) {
                    String img = n_image.group();
                    Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
                    while (m.find()) {
                        pics.add(m.group(1));
                    }
                }
                pic_movie = pics.toString();//图片地址+[]
                actorimg_movie[i][j] = pic_movie.substring(1, pic_movie.length() - 1);
                if(j==0){
                    actorimg_movie[i][j+1]="";
                }
                else{
                    break;
                }
                j=j+1;
            }
            i=i+1;
            if(i>4){
                break;
            }
        }
        Element moviechat = document.getElementsByClass("db_shortcomment").get(0);
        System.out.println(moviechat); System.out.println("\n\n");
        Elements dl=moviechat.getElementsByTag("dd");
        i=0;
        for(Element info:dl){
            chatTitle_movie[i]=title_movie;
            chatContent_movie[i]=info.getElementsByTag("h3").get(0).text();
            Element Author=info.getElementsByClass("px14").get(0);
            chatAuthor_movie[i]=Author.text();
            Element Img=info.getElementsByClass("pic_58").get(0).getElementsByTag("a").get(0);
            pics = new HashSet<String>();
            regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
            p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
            m_image = p_image.matcher(Img.toString());
            while (m_image.find()) {
                String img = m_image.group();
                Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
                while (m.find()) {
                    pics.add(m.group(1));
                }
            }
            pic_movie = pics.toString();//图片地址+[]
            chatImg_movie[i] = pic_movie.substring(1, pic_movie.length() - 1);
            i=i+1;
            if(i>3){
                break;
            }
        }
        //存储
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("id", id_movie);
        data.put("title", title_movie);
        data.put("url", url_movie);
        data.put("image", image_movie);
        data.put("TAS", TAS_movie);
        data.put("detail", detail_movie);
//        data.put("date", "");
//        data.put("status", 1);
        data.put("director",director_movie);
        data.put("bianju",bianju_movie);
        data.put("country", country_movie);
        data.put("company", company_movie);
        data.put("more", more_movie);
//        data.put("type", 0);

        data.put("vedios",vedios_movie);
        data.put("images",images_movie);
        data.put("actors",actors_movie);
        data.put("chats",chats_movie);
        data.put("news",news_movie);
        data.put("actor1",actor_movie[0]);
        data.put("actor1_img1",actorimg_movie[0][0]);
        data.put("actor1_img2",actorimg_movie[0][1]);
        data.put("actor2",actor_movie[1]);
        data.put("actor2_img1",actorimg_movie[1][0]);
        data.put("actor2_img2",actorimg_movie[1][1]);
        data.put("actor3",actor_movie[2]);
        data.put("actor3_img1",actorimg_movie[2][0]);
        data.put("actor3_img2",actorimg_movie[2][1]);
        data.put("actor4",actor_movie[3]);
        data.put("actor4_img1",actorimg_movie[3][0]);
        data.put("actor4_img2",actorimg_movie[3][1]);
        data.put("chatTitle1",chatTitle_movie[0]);
        data.put("chatAuthor1",chatAuthor_movie[0]);
        data.put("chatImg1",chatImg_movie[0]);
        data.put("chatContent1",chatContent_movie[0]);
        data.put("chatTitle2",chatTitle_movie[1]);
        data.put("chatAuthor2",chatAuthor_movie[1]);
        data.put("chatImg2",chatImg_movie[1]);
        data.put("chatContent2",chatContent_movie[1]);
        data.put("chatTitle3",chatTitle_movie[2]);
        data.put("chatAuthor3",chatAuthor_movie[2]);
        data.put("chatImg3",chatImg_movie[2]);
        data.put("chatContent3",chatContent_movie[2]);
        data.put("chatTitle4",chatTitle_movie[3]);
        data.put("chatAuthor4",chatAuthor_movie[3]);
        data.put("chatImg4",chatImg_movie[3]);
        data.put("chatContent4",chatContent_movie[3]);
        //将
        JSONObject obj = new JSONObject(data);
        Movies model = new Movies();
        try {
            model = JSONObject.parseObject(obj.toString(), Movies.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.add(model);
        System.out.println(result);
        //保存到缓存，
        /*
          类型为 hash,存储格式为 book   -    name    -    与之相关的书籍
          保存一天
         */
        redisUtils.hset("movie", id, JSONUtil.toJson(result), Time.ONE_DAY);
        return result;
    }
    public String select(String title) throws IOException {
        //小爱网站
        String URL="http://42mj.com/index.php/vod/search.html?wd="+title;
//        String URL="http://xa.360baidian.net/index.php/vod/search.html?wd="+title;
        Document document = Jsoup.connect(URL).get(); /*页面html内容*/
        Element eles = document.getElementsByClass("active clearfix").get(0);
        Element url_m=eles.getElementsByClass("title").get(0).getElementsByTag("a").get(0);
        String url_mo=url_m.toString().substring(34,39);
        String url_movie="http://xa.360baidian.net/index.php/vod/play/id/"+url_mo+"/sid/1/nid/1.html";
        return url_movie;
    }

    @Override//所有电影详情+包括按照id来获取和名字来获取
    public List<Movies> findMovieBynameid(String name) throws IOException {
        List<Movies> result = new ArrayList<>();
        //小爱网站
        String URL="http://42mj.com/index.php/vod/search.html?wd="+name;
//        String URL="http://xa.360baidian.net/index.php/vod/search.html?wd="+name;
        Document document = Jsoup.connect(URL).get(); /*页面html内容*/
        System.out.println(document+"\n\n");
        Element eles = document.getElementsByClass("active clearfix").get(0);
//        System.out.println(eles);
        Element url1=eles.getElementsByClass("title").get(0);
        Element url2=url1.getElementsByTag("a").get(0);
        String id_movie=url2.toString().substring(34,39);
        String title_movie=url2.text();
        String url_movie="http://42mj.com/index.php/vod/play/id/"+id_movie+"/sid/1/nid/1.html";
        //从redis中获取
        String movie = redisUtils.hget("movie", id_movie);
        if (Tool.getInstance().isNotNull(movie)) {
            JSONArray array = JSONArray.parseArray(movie);
            for (Object o : array) {
                try {
                    result.add(JSONUtil.toBean(o.toString(), Movies.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result;
        }
        String url="http://42mj.com/index.php/vod/detail/id/"+id_movie+".html";
        Document document1 = Jsoup.connect(url).get(); /*页面html内容*/
//        System.out.println(document1+"\n\n");
        Element eles1 = document1.getElementsByClass("col-pd clearfix").get(1);
//        System.out.println(eles1);
        Element image=eles1.getElementsByTag("a").get(0).getElementsByTag("img").get(0);
        Set<String> pics = new HashSet<String>();
        String regEx_img = "<img.*data-original\\s*=\\s*(.*?)[^>]*?>";
        Pattern p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        Matcher m_image = p_image.matcher(image.getElementsByTag("img").toString());
        while (m_image.find()) {
            String img = m_image.group();
            Matcher m = Pattern.compile("data-original\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        String pic_movie = pics.toString();//图片地址+[]
        String image_movie = pic_movie.substring(1, pic_movie.length() - 1);

        String TAS_movie = eles1.getElementsByTag("p").get(0).text().substring(0,5);
        String country_movie=eles1.getElementsByTag("p").get(0).text().substring(6,11);
        String bianju_movie=eles1.getElementsByTag("p").get(1).text();
        String director_movie=eles1.getElementsByTag("p").get(2).text();
        Element company=eles1.getElementsByTag("p").get(3);
        String company_movie=company.text();//+company.toString().substring(61,71);
        String detail_movie=eles1.getElementsByTag("p").get(4).text();

        String more_movie="";String actors_movie="";String vedios_movie="";
        String images_movie="";String chats_movie="";String news_movie="";
        String actorimg_movie[][]=new String[4][2]; String actor_movie[]=new String[4];
        String chatTitle_movie[]=new String[4];String chatAuthor_movie[]=new String[4];
        String chatImg_movie[]=new String[4];String chatContent_movie[]=new String[4];
        for(int i=0;i<4;i++){
            actor_movie[i]="";
            chatTitle_movie[i]="";
            chatAuthor_movie[i]="";
            chatImg_movie[i]="";
            chatContent_movie[i]="";
            for(int j=0;j<2;j++){
                actorimg_movie[i][j]="";
            }
        }
        //存储
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("id", id_movie);
        data.put("title", title_movie);
        data.put("url", url_movie);
        data.put("image", image_movie);
        data.put("TAS", TAS_movie);
        data.put("detail", detail_movie);
//        data.put("date", "");
//        data.put("status", 1);
        data.put("director",director_movie);
        data.put("bianju",bianju_movie);
        data.put("country", country_movie);
        data.put("company", company_movie);
        data.put("more", more_movie);
//        data.put("type", 0);

        data.put("vedios",vedios_movie);
        data.put("images",images_movie);
        data.put("actors",actors_movie);
        data.put("chats",chats_movie);
        data.put("news",news_movie);
        data.put("actor1",actor_movie[0]);
        data.put("actor1_img1",actorimg_movie[0][0]);
        data.put("actor1_img2",actorimg_movie[0][1]);
        data.put("actor2",actor_movie[1]);
        data.put("actor2_img1",actorimg_movie[1][0]);
        data.put("actor2_img2",actorimg_movie[1][1]);
        data.put("actor3",actor_movie[2]);
        data.put("actor3_img1",actorimg_movie[2][0]);
        data.put("actor3_img2",actorimg_movie[2][1]);
        data.put("actor4",actor_movie[3]);
        data.put("actor4_img1",actorimg_movie[3][0]);
        data.put("actor4_img2",actorimg_movie[3][1]);
        data.put("chatTitle1",chatTitle_movie[0]);
        data.put("chatAuthor1",chatAuthor_movie[0]);
        data.put("chatImg1",chatImg_movie[0]);
        data.put("chatContent1",chatContent_movie[0]);
        data.put("chatTitle2",chatTitle_movie[1]);
        data.put("chatAuthor2",chatAuthor_movie[1]);
        data.put("chatImg2",chatImg_movie[1]);
        data.put("chatContent2",chatContent_movie[1]);
        data.put("chatTitle3",chatTitle_movie[2]);
        data.put("chatAuthor3",chatAuthor_movie[2]);
        data.put("chatImg3",chatImg_movie[2]);
        data.put("chatContent3",chatContent_movie[2]);
        data.put("chatTitle4",chatTitle_movie[3]);
        data.put("chatAuthor4",chatAuthor_movie[3]);
        data.put("chatImg4",chatImg_movie[3]);
        data.put("chatContent4",chatContent_movie[3]);
        //将
        JSONObject obj = new JSONObject(data);
        Movies model = new Movies();
        try {
            model = JSONObject.parseObject(obj.toString(), Movies.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.add(model);
        System.out.println(result);
        //保存到缓存，
        /*
          类型为 hash,存储格式为 book   -    name    -    与之相关的书籍
          保存一天
         */
        redisUtils.hset("movie", id_movie, JSONUtil.toJson(result), Time.ONE_DAY);
        return result;

    }

    @Override//一个top榜单信息
    public Top findTopById(Integer id){ return topMovieRepository.findOneTop(id); }

    @Override//电影剧照
    public String[] findMovieImages(String id) throws IOException, ScriptException {
        String URL="http://movie.mtime.com/"+id+"/posters_and_images/";
        Document document = Jsoup.connect(URL).get(); /*页面html内容*/
        // 取得所有的script tag使用json來parse html
        Element eles = document.getElementsByTag("script").get(4);
        // 檢查是否有detailInfoObject字串
        String script = eles.childNode(0).toString();
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
        engine.eval(script);
        // 將obj轉成Json物件
        ScriptObjectMirror json=(ScriptObjectMirror) engine.get("imageList");
        String jsons=JSONObject.toJSONString(json.getSlot(0));
        JSONObject myJsonObject00 = JSONObject.parseObject(jsons);
        String value00=myJsonObject00.getString("stagepicture");
//        System.out.println(value00+"\n\n");
        JSONObject myJsonObject01 = JSONObject.parseObject(value00);
        String value01=myJsonObject01.getString("0");
//        System.out.println(value01+"\n\n");
        JSONObject myJsonObject02 = JSONObject.parseObject(value01);
        String value02=myJsonObject02.getString("officialstageimage");
//        System.out.println(value02+"\n\n");
        JSONObject myJsonObject03 = JSONObject.parseObject(value02);
        int flag=0;
        for(int i=0;i<myJsonObject03.size();i++) {
            flag = flag + 1;
        }
        String img_movie[]=new String[flag];
        for(int i=0;i<myJsonObject03.size();i++){
            JSONObject myJsonObject04 = JSONObject.parseObject(myJsonObject03.get(i).toString());
            img_movie[i]=myJsonObject04.get("img_220").toString();
            System.out.println(img_movie[i]);
//            System.out.println(i+" = "+myJsonObject04.get("img_220").toString());
        }
        System.out.println("\n\n\n");
        return img_movie;
    }
    @Override//电影海报
    public String[] findMoviePosters(String id) throws IOException, ScriptException {
        String URL="http://movie.mtime.com/"+id+"/posters_and_images/";
        Document document = Jsoup.connect(URL).get(); /*页面html内容*/
        // 取得所有的script tag使用json來parse html
        Element eles = document.getElementsByTag("script").get(4);
        // 檢查是否有detailInfoObject字串
        String script = eles.childNode(0).toString();
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
        engine.eval(script);
        // 將obj轉成Json物件
        ScriptObjectMirror json=(ScriptObjectMirror) engine.get("imageList");
        String jsons=JSONObject.toJSONString(json.getSlot(1));
        JSONObject myJsonObject00 = JSONObject.parseObject(jsons);
        String value00=myJsonObject00.getString("poster");
//        System.out.println(value00+"\n\n");
        JSONObject myJsonObject01 = JSONObject.parseObject(value00);
        String value01=myJsonObject01.getString("0");
//        System.out.println(value01+"\n\n");
        JSONObject myJsonObject02 = JSONObject.parseObject(value01);
        String value02=myJsonObject02.getString("generalposter");
//        System.out.println(value02+"\n\n");
        JSONObject myJsonObject03 = JSONObject.parseObject(value02);
        int flag=0;
        for(int i=0;i<myJsonObject03.size();i++) {
            flag = flag + 1;
        }
        String img_movie[]=new String[flag];
        for(int i=0;i<myJsonObject03.size();i++){
            JSONObject myJsonObject04 = JSONObject.parseObject(myJsonObject03.get(i).toString());
            img_movie[i]=myJsonObject04.get("img_220").toString();
//            System.out.println(i+" = "+myJsonObject04.get("img_220").toString());
        }
        System.out.println("\n\n\n");
        return img_movie;
    }
    @Override//电影预告片
    public List<Vedio> findMovies(String id) throws IOException, ScriptException {
        List<Vedio> result = new ArrayList<>();
        Vedio model = new Vedio();
        //从redis中获取
        String movie = redisUtils.hget("vedio", id);
        if (Tool.getInstance().isNotNull(movie)) {
            JSONArray array = JSONArray.parseArray(movie);
            for (Object o : array) {
                try {
                    result.add(JSONUtil.toBean(o.toString(), Vedio.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result;
        }
        //zaiwangzhanchaozhao
        String URL="http://movie.mtime.com/"+id+"/trailer.html";
        Document document = Jsoup.connect(URL).get();
        // 取得所有的script tag使用json來parse html
        Element eles = document.getElementsByTag("script").get(4);
        // 檢查是否有detailInfoObject字串
        String script = eles.childNode(0).toString();
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
        engine.eval(script);
        // 將obj轉成Json物件
//        System.out.println(script);
        ScriptObjectMirror json=(ScriptObjectMirror) engine.get("videos");
        String value=JSONObject.toJSONString(json.get("预告片"));
//        System.out.println(value+"\n\n");
        JSONObject myJsonObject = JSONObject.parseObject(value);
        for(int i=0;i<myJsonObject.size();i++) {
            JSONObject myJsonObject0 = JSONObject.parseObject(myJsonObject.get(i).toString());
            String idvedio=myJsonObject0.get("VideoID").toString();
            String url= myJsonObject0.get("Url").toString();
            String length=myJsonObject0.get("Length").toString();
            String img=myJsonObject0.get("ImagePath").toString();
            String vedioname=myJsonObject0.get("ShortTitle").toString();
            //存储
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("id", idvedio);
            data.put("url", url);
            data.put("title", vedioname);
            data.put("length", length);
            data.put("img", img);
            //将
            JSONObject obj = new JSONObject(data);
            try {
                model = JSONObject.parseObject(obj.toString(), Vedio.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            result.add(model);
            //保存到缓存，
            /*
           类型为 hash,存储格式为 book   -    name    -    与之相关的书籍
           保存一天
           */
            redisUtils.hset("vedio", id, JSONUtil.toJson(result), Time.ONE_DAY);
        }
        return result;
    }
    @Override//电影演员
    public List<Actor> findActors(String id) throws IOException, ScriptException{
        List<Actor> result = new ArrayList<>();
        Actor model = new Actor();
        //从redis中获取
        String movie = redisUtils.hget("actor", id);
        if (Tool.getInstance().isNotNull(movie)) {
            JSONArray array = JSONArray.parseArray(movie);
            for (Object o : array) {
                try {
                    result.add(JSONUtil.toBean(o.toString(), Actor.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result;
        }
        //congwangzhanpaqu
        String URL="http://movie.mtime.com/"+id+"/fullcredits.html";
        Document document = Jsoup.connect(URL).get(); /*页面html内容*/
//        System.out.println(document+"\n\n");
        Element eles = document.getElementsByClass("db_actor").get(0);
//        System.out.println(eles);
        Elements dd=eles.getElementsByTag("dd");
        int i=1;
        String title[]=new String[2];
        title[0]=" ";title[1]=" ";
        String image="";
        for(Element info:dd){
            Elements h3=info.getElementsByTag("h3");
            int flag=0;
            for(Element h:h3){
                title[flag]=h.text();
                flag=flag+1;
            }
            Element picture=info.getElementsByTag("a").get(0);
            Set<String> pics = new HashSet<String>();
            String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
            Pattern p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
            Matcher m_image = p_image.matcher(picture.toString());
            while (m_image.find()) {
                String img = m_image.group();
                Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
                while (m.find()) {
                    pics.add(m.group(1));
                }
            }
            String pic_movie = pics.toString();//图片地址+[]
            image= pic_movie.substring(1, pic_movie.length() - 1);
            System.out.println(i);System.out.println(title[0]);
            System.out.println(title[1]);System.out.println(image+"\n\n");
            //存储
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("id", i);
            data.put("title0", title[0]);
            data.put("title", title[1]);
            data.put("img", image);
            //将
            JSONObject obj = new JSONObject(data);
            try {
                model = JSONObject.parseObject(obj.toString(), Actor.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            result.add(model);
            System.out.println(result);
            //保存到缓存，
            /*
           类型为 hash,存储格式为 book   -    name    -    与之相关的书籍
           保存一天
           */
            redisUtils.hset("actor", id, JSONUtil.toJson(result), Time.ONE_DAY);
            i=i+1;
            if(i>30){
                break;
            }
        }
        System.out.println(result);
        return result;
    }
    @Override//电影评论
    public List<Comment> findComments(String id) throws IOException {
        List<Comment> result = new ArrayList<>();
        Comment model = new Comment();
        //从redis中获取
        String movie = redisUtils.hget("comment", id);
        if (Tool.getInstance().isNotNull(movie)) {
            JSONArray array = JSONArray.parseArray(movie);
            for (Object o : array) {
                try {
                    result.add(JSONUtil.toBean(o.toString(), Comment.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result;
        }
        //congwangzhanpaqu
        String URL="http://movie.mtime.com/"+id+"/reviews/short/new.html";
        Document document = Jsoup.connect(URL).get();
        System.out.println(document+"\n\n");
        Elements eles = document.getElementById("tweetRegion").getElementsByTag("dd");
        int i=1;
        for(Element info:eles){
            String detail=info.getElementsByTag("h3").get(0).text();
            Element image=info.getElementsByTag("a").get(0);
            Set<String> pics = new HashSet<String>();
            String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
            Pattern p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
            Matcher m_image = p_image.matcher(image.toString());
            while (m_image.find()) {
                String img = m_image.group();
                Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
                while (m.find()) {
                    pics.add(m.group(1));
                }
            }
            String pic_movie = pics.toString();//图片地址+[]
            String imagecomment= pic_movie.substring(1, pic_movie.length() - 1);
            String author=info.getElementsByTag("p").get(0).text();
            String credit=info.getElementsByTag("p").get(1).text();
            Element timecomment=info.getElementsByTag("a").get(2);//.toString(76,94);

            pics = new HashSet<String>();
            regEx_img = "<a.*entertime\\s*=\\s*(.*?)[^>]*?>";
            p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
            m_image = p_image.matcher(timecomment.toString());
            while (m_image.find()) {
                String img = m_image.group();
                Matcher m = Pattern.compile("entertime\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
                while (m.find()) {
                    pics.add(m.group(1));
                }
            }
            pic_movie = pics.toString();//图片地址+[]
            String TAS= pic_movie.substring(1, pic_movie.length() - 1);
//            System.out.println(i);
//            System.out.println(author);
//            System.out.println(credit);
//            System.out.println(detail);
//            System.out.println(imagecomment);
//            System.out.println(TAS);
            //存储
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("id", i);
            data.put("author", author);
            data.put("detail", detail);
            data.put("credit", credit);
            data.put("time", TAS);
            data.put("img", imagecomment);
            //将
            JSONObject obj = new JSONObject(data);
            try {
                model = JSONObject.parseObject(obj.toString(), Comment.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            result.add(model);
//            System.out.println(result);
            //保存到缓存，
            /*
           类型为 hash,存储格式为 book   -    name    -    与之相关的书籍
           保存一天
           */
            redisUtils.hset("comment", id, JSONUtil.toJson(result), Time.ONE_DAY);
            i=i+1;
        }
        return result;
    }
    public static void main(String[] args) {
    }
}
