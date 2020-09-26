package com.wsk.life.service.music;

import com.wsk.life.bean.music.WangYiResponseEntity;
import com.wsk.life.bean.music.WangYiTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @DESCRIPTION :
 * @AUTHOR : WuShukai1103
 * @TIME : 2018/2/15  15:49
 */
public interface AllMusicsRepository extends JpaRepository<WangYiResponseEntity, String>{

    //按照id的不同，输出索要结果
    @Query(value = "select b.* from music_all b WHERE  b.type = :id AND b.status = 1 order by rand() limit 30", nativeQuery = true)
    List<WangYiResponseEntity> randMusics(@Param("id") String id);

    //一个电影……热映电影+即将上映
    @Query(value = "select b.* from music_all b WHERE  b.id = :id AND b.type = :type", nativeQuery = true)
    WangYiResponseEntity findmusic(@Param("id") String id,@Param("type") String type);

    @Query(value = "select b.id,b.title from music_all b", nativeQuery = true)
    List<String> findMusics();

    @Query(value = "select b.* from music_all b WHERE  b.id = :id", nativeQuery = true)
    WangYiResponseEntity findmusicbyid(@Param("id") String id);

    @Query(value = "select b.title from music_all b WHERE  b.id = :id", nativeQuery = true)
    String findName(@Param("id") String id);
}