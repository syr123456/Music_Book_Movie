package com.wsk.life.service.music;

import com.wsk.life.bean.movie.maoyan.Hot;
import com.wsk.life.bean.music.*;

import java.io.IOException;
import java.util.List;

/**
 * @author: wsk1103
 * @date: 18-1-14 下午7:37
 * @description: JAVA8
 */
public interface WangYiService {
    List<WangYiResponseEntity> randMusics(String type);//热映电影+即将上映+Top榜单

    WangYiTypeEntity getType(String id_type) throws IOException;
    void getHot(String id_type,String type) throws IOException;

    List<WangYiContent> findcontentbyid(String id) throws IOException;
    WangYiResponseEntity findmusicByName(String name);

//    WangYiResponseEntity findmusic(String typeid,String id);

    List<WangYiLyricEntity> findlyric(String id) throws IOException;

    List<WangYiSingerDetailEntity> findsingerdetail(String id,Integer flag) throws IOException;
    void findsingermusic(String id) throws IOException;
    void findsingeralbum(String id) throws IOException;
    void findsingerMV(String id) throws IOException;
    void findalbum(String id) throws IOException;

    void findusermusic(String gedanid) throws IOException;
}
