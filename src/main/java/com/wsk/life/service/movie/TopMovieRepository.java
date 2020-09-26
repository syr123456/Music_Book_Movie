package com.wsk.life.service.movie;

import com.wsk.life.bean.movie.maoyan.Hot;
import com.wsk.life.bean.movie.maoyan.Top;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @DESCRIPTION :
 * @AUTHOR : WuShukai1103
 * @TIME : 2018/2/15  15:49
 */
public interface TopMovieRepository extends JpaRepository<Top, String>{
    //全部top榜单
    @Query(value = "select b.* from movie_top b order by rand() limit 10 ", nativeQuery = true)
    List<Top> randTop();

    //一个电影……top榜单信息
    @Query(value = "select b.* from movie_top b WHERE  b.id = :id ", nativeQuery = true)
    Top findOneTop(@Param("id") Integer id);
}