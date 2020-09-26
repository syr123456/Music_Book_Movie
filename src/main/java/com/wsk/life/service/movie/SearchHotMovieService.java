package com.wsk.life.service.movie;

import com.wsk.life.bean.movie.maoyan.*;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.List;

/**
 * @DESCRIPTION :
 * @AUTHOR : WuShukai1103
 * @TIME : 2018/2/15  14:59
 */
public interface SearchHotMovieService {

    List<Hot>[] searchHotMovies(int number) throws IOException;

    List<Hot> randMovies(Integer id);//热映电影+即将上映+Top榜单
    void InsertMovie(String title) throws IOException;//存入热映电影+即将上映的电影

    void top() throws IOException;
    List<Top> ranktop();//top电影
    void InsertTopMovie(Integer id) throws IOException;//存储各类top对应的电影

    Top findTopById(Integer id) throws IOException;
    List<Movies> findMovieById(String id,String name) throws IOException;
    List<Movies> findMovieBynameid(String name) throws IOException;

    List<String> findMovieByName() throws IOException;
    String[] findMovieImages(String id) throws IOException, ScriptException;
    String[] findMoviePosters(String id) throws IOException, ScriptException;
    List<Vedio> findMovies(String id) throws IOException, ScriptException;
    List<Actor> findActors(String id) throws IOException, ScriptException;
    List<Comment> findComments(String id) throws IOException, ScriptException;

//    List<Vedio>[] findMovies(String id) throws IOException, ScriptException;
}
