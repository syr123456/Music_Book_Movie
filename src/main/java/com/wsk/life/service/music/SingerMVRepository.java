package com.wsk.life.service.music;

import com.wsk.life.bean.music.WangYiAlbumEntity;
import com.wsk.life.bean.music.WangYiMVEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @DESCRIPTION :
 * @AUTHOR : WuShukai1103
 * @TIME : 2018/2/15  15:49
 */
public interface SingerMVRepository extends JpaRepository<WangYiMVEntity, String>{

    @Query(value = "select b.* from music_singermv b WHERE  b.singerid = :id ", nativeQuery = true)
    List<WangYiMVEntity> randMvs(@Param("id") String id);

    @Query(value = "select b.* from music_singermv b WHERE  b.id = :id  AND b.singerid=:singerid", nativeQuery = true)
    WangYiMVEntity findMVById(@Param("id") String id, @Param("singerid") String singerid);
}