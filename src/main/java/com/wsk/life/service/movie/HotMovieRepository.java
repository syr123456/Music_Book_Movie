package com.wsk.life.service.movie;

import com.wsk.life.bean.movie.maoyan.Hot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @DESCRIPTION :
 * @AUTHOR : WuShukai1103
 * @TIME : 2018/2/15  15:49
 */
public interface HotMovieRepository extends JpaRepository<Hot, String>{

    //按照id的不同，输出索要结果
    @Query(value = "select b.* from movie_hot b WHERE  b.type = :id AND b.status = 1", nativeQuery = true)
    List<Hot> randMovies(@Param("id") Integer id);

/*
    //全部即将上映电影
    @Query(value = "select b.* from movie_hot b WHERE  b.type = 101 AND b.status = 1", nativeQuery = true)
    List<Hot> randJiMovies();

    //全部Top电影
    @Query(value = "select b.* from movie_hot b WHERE  b.type = :id AND b.status = 1", nativeQuery = true)
    List<Hot> randTopMovies(@Param("id") Integer id);
*/

    //一个电影……热映电影+即将上映
    @Query(value = "select b.* from movie_hot b WHERE  b.id = :id ", nativeQuery = true)
    Hot findOneMovie(@Param("id") String id);

    //一个电影……热映电影+即将上映
    @Query(value = "select b.id,b.title from movie_hot b", nativeQuery = true)
    List<String> findMovieByName();
}