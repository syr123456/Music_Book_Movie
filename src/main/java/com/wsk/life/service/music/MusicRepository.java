package com.wsk.life.service.music;

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
public interface MusicRepository extends JpaRepository<WangYiTypeEntity, String>{

/*
    //按照id的不同，输出索要结果
    @Query(value = "select b.* from musictype b WHERE  b.type = :id AND b.status = 1", nativeQuery = true)
    List<WangYiResponseEntity> randMusics(@Param("id") Integer id);
*/

    //一个电影……热映电影+即将上映
    @Query(value = "select b.* from music_type b WHERE  b.id = :id ", nativeQuery = true)
    WangYiTypeEntity findType(@Param("id") String id);
}