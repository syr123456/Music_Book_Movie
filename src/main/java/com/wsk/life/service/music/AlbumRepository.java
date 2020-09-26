package com.wsk.life.service.music;

import com.wsk.life.bean.music.WangYiAlbum;
import com.wsk.life.bean.music.WangYiAlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @DESCRIPTION :
 * @AUTHOR : WuShukai1103
 * @TIME : 2018/2/15  15:49
 */
public interface AlbumRepository extends JpaRepository<WangYiAlbum, String>{

    @Query(value = "select b.* from music_album b WHERE  b.id = :id ", nativeQuery = true)
    WangYiAlbum randAlbums(@Param("id") String id);
}