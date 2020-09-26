package com.wsk.life.service.book.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wsk.life.bean.book.BookEntity;
import com.wsk.life.bean.book.BookNameEntity;
import com.wsk.life.service.book.BookNameRepository;
import com.wsk.life.service.book.BookRepository;
import com.wsk.life.service.book.SearchBookService;
import com.wsk.life.config.http.HttpUnits;
import com.wsk.life.config.redis.IRedisUtils;
import com.wsk.life.config.tool.JSONUtil;
import com.wsk.life.config.tool.Time;
import com.wsk.life.config.tool.Tool;
import org.hibernate.exception.DataException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
public class SearchBookServiceImpl implements SearchBookService {
    //private static final String URL = "https://api.douban.com/v2/book/search?";
    private static final String URL="https://www.dushu.com/search.aspx?wd=";//读书网
    private static final String URLId="https://www.dushu.com/";//读书网
    private static BookRepository bookRepository;
    private static BookNameRepository booknameRepository;
    private static IRedisUtils redisUtils;

    @Autowired
    public SearchBookServiceImpl(BookRepository bookRepository, BookNameRepository booknameRepository, IRedisUtils redisUtils) {
        this.bookRepository = bookRepository;
        this.booknameRepository=booknameRepository;
        this.redisUtils = redisUtils;
    }
    @Override
    public List<BookNameEntity> searchBookByName(String name) throws IOException {
        if (Tool.getInstance().isNullOrEmpty(name)) {
            return null;
        }
        List<BookNameEntity> result2 = new ArrayList<>();
        List<BookNameEntity> result[] = new List[20];
        //从redis中获取
        int flag=1;
        while(redisUtils.hget(name, String.valueOf(flag))!=null){
            flag++;
        }
        flag=flag-1;
        System.out.println(name+"一共有……………………………………"+flag);
        if(flag==0){
            flag=1;
        }
        else{
            int f=(int)(1+Math.random()*(flag));
            System.out.println("f="+f);
            String books = redisUtils.hget(name, String.valueOf(f));
            System.out.println("book="+books);
            if (Tool.getInstance().isNotNull(books)) {
                JSONArray array = JSONArray.parseArray(books);
                for (Object o : array) {
                    try {
                        result2.add(JSONUtil.toBean(o.toString(), BookNameEntity.class));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return result2;
            }
        }
        //redis 中没有，从豆瓣获取
        String url = URL+name;
        int num=1;
        Document document = Jsoup.connect(url).get(); /*页面html内容*/
        Element searchlist = document.getElementsByClass("searchlist").get(0); /*找到第一个类名为nav_left的标签*/
        Elements li = searchlist.getElementsByTag("li");/*找到nav_left标签下的所有li标签*/
        for(Element info:li) { /*遍历每一个li标签*/
            Elements a = info.getElementsByTag("a"); /*找到第一个a标签,找到span标签，并将内容转换成字符串*/
            String id = a.toString().substring(10, 24);/*获取书名的ID号*/
            String id_book = a.toString().substring(15, 23);
            String myUrl = URLId + id;/*具体书籍的读书网网站信息*/
            document = Jsoup.connect(myUrl).get();
            //图书标题
            Elements title = document.getElementsByClass("book-title");
            String title_book = title.text();//=name
            //图书图片
            Element pic = document.getElementsByClass("pic").get(0);
            Set<String> pics = new HashSet<String>();
            String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
            Pattern p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
            Matcher m_image = p_image.matcher(pic.toString());
            while (m_image.find()) {
                String img = m_image.group();
                Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
                while (m.find()) {
                    pics.add(m.group(1));
                }
            }
            String image_book = pics.toString();//图片地址+[]
            String pic_book = image_book.substring(1, image_book.length() - 1);
            //图书价格+作者+出版社+标签
            Element price = document.getElementById("ctl00_c1_bookleft");
            String price_book = price.getElementsByClass("price").text().substring(4);
            Elements trElements = price.getElementsByTag("table").get(0).getElementsByTag("tr");
            Elements tds1 = trElements.get(0).getElementsByTag("td");
            String author_book = tds1.get(1).text();//作者
            Elements tds2 = trElements.get(1).getElementsByTag("td");
            String publisher_book = tds2.get(1).text();//出版社
            Elements tds3 = trElements.get(2).getElementsByTag("td");
            String congbian_book = tds3.get(1).text();//从编项
            Elements tds4 = trElements.get(3).getElementsByTag("td");
            String tag_book = tds4.get(1).text();//标签
            //去哪购买图书
            Element buy = document.getElementsByTag("ul").get(3);//图书去那里购买-----不是每一本书都有该信息
            String buy_book = buy.text();
            if (buy_book.length() > 100) {
                //设立一个阐述flag，当flag=1时可以对该项内容进行保存。
                buy_book = null;
            }
            //图书详细信息
            Elements details = document.getElementsByTag("table").get(1).getElementsByTag("tr");
            Elements td1 = details.get(0).getElementsByTag("td");
            String ISBN_book = td1.get(1).text();//ISBN
            String time_book = td1.get(3).text();//出版时间
            String bao_book = td1.get(5).text();//包装
            Elements td2 = details.get(1).getElementsByTag("td");
            String kaiben_book = td2.get(1).text();//开本
            String yeshu_book = td2.get(3).text();//页数
            String zishu_book = td2.get(5).text();//字数
            //内容简介
            Element content = document.getElementsByClass("text txtsummary").get(0);
            String content_book = content.text();//内容简介
            //作者简介
            Element acontent = document.getElementsByClass("text txtsummary").get(1);
            String acontent_book = acontent.text();//作者简介
            //图书目录
            Element log = document.getElementsByClass("text txtsummary").get(2);
            String log_book = log.text();//图书目录
            //String log_book=log.toString().substring(29);//图书目录
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("wherebuy", buy_book);
            data.put("id", id_book);
            data.put("title", title_book);
            data.put("image", pic_book);
            data.put("price", price_book);
            data.put("author", author_book);
            data.put("publisher", publisher_book);
            data.put("congbian", congbian_book);
            data.put("tag", tag_book);
            data.put("ISBN", ISBN_book);
            data.put("PublishTime", time_book);
            data.put("baozhuang", bao_book);
            data.put("kaiben", kaiben_book);
            data.put("yeshu", yeshu_book);
            data.put("zishu", zishu_book);
            data.put("content", content_book);
            data.put("acontent", acontent_book);
            data.put("log", log_book);
            data.put("num",num);
            JSONObject obj = new JSONObject(data);
            BookNameEntity model = new BookNameEntity();
            try {
                model = JSONObject.parseObject(obj.toString(), BookNameEntity.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //如果数据库没有，则保存到数据库6v33
            if (Tool.getInstance().isNullOrEmpty(bookRepository.findById(id_book))) {
                try {
                    booknameRepository.save(model);
                } catch (DataException e) {
                    e.printStackTrace();
                }
            }
            result[num] = new ArrayList<>();
            result[num].add(model);
            System.out.println("num="+num);
            redisUtils.hset(name ,String.valueOf(num), JSONUtil.toJson(result[num]), Time.ONE_DAY);
            num=num+1;
            if(num>5)
                break;
        }
        //保存到缓存，
        /*
          类型为 hash,存储格式为 book   -    name    -    与之相关的书籍
          保存一天
         */
        System.out.println("num="+num);
        System.out.println("flag="+flag);
        return result[flag];
    }

    @Override
    public List<BookEntity> randBook() {
        return bookRepository.randBook();
    }

    @Override
    public BookEntity findById(long id) {
        return bookRepository.findOneBook(id);
    }

    public static void main(String[] args) {
//        SearchBookService service = new SearchBookServiceImpl(bookRepository, redisUtils);
//        service.searchBookByName("小王子");
    }
}
