package com.wsk.life.service.music;

import com.wsk.life.bean.music.WangYiAlbumEntity;
import com.wsk.life.bean.music.WangYiResponseEntity;
import com.wsk.life.bean.music.WangYiSingerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @DESCRIPTION :
 * @AUTHOR : WuShukai1103
 * @TIME : 2018/2/15  15:49
 */
public interface SingerAlbumRepository extends JpaRepository<WangYiAlbumEntity, String>{

    @Query(value = "select b.* from music_singeralbum b WHERE  b.singerid = :id ", nativeQuery = true)
    List<WangYiAlbumEntity> randAlbums(@Param("id") String id);

    @Query(value = "select b.* from music_singeralbum b WHERE  b.id = :id  AND b.singerid=:singerid", nativeQuery = true)
    WangYiAlbumEntity findAlbumById(@Param("id") String id,@Param("singerid") String singerid);
}