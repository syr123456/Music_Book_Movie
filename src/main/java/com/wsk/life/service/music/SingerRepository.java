package com.wsk.life.service.music;

import com.wsk.life.bean.music.WangYiSingerEntity;
import com.wsk.life.bean.music.WangYiTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @DESCRIPTION :
 * @AUTHOR : WuShukai1103
 * @TIME : 2018/2/15  15:49
 */
public interface SingerRepository extends JpaRepository<WangYiSingerEntity, String>{

    @Query(value = "select b.* from music_singer b WHERE  b.id = :id ", nativeQuery = true)
    WangYiSingerEntity findSinger(@Param("id") String id);

    @Query(value = "select b.name from music_singer b WHERE  b.id = :id ", nativeQuery = true)
    String findNameById(@Param("id") String id);
}