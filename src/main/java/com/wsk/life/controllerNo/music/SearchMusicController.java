///*
//package com.wsk.life.controllerNo.music;
//
//import com.wsk.life.bean.movie.maoyan.Movies;
//import com.wsk.life.bean.music.WangYiResponseEntity;
//import com.wsk.life.config.tool.Tool;
//import com.wsk.life.service.music.WangYiService;
//import com.wsk.life.domain.UserInformation;
//import org.apache.ibatis.annotations.Param;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.util.List;
//
//
//@Controller
//@RequestMapping(value = "/search/music")
//public class SearchMusicController {
//
//    private final WangYiService service;
//
//    @Autowired
//    public SearchMusicController(WangYiService service) {
//        this.service = service;
//    }
//
//    @RequestMapping(value = "/{name}")
//    @ResponseBody
//    public String searchMovieResult(Model model, HttpServletRequest request, @ModelAttribute("name") String q) throws IOException {
//        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
//        if (Tool.getInstance().isNullOrEmpty(userInformation)) {
//            return "redirect:/login";
//        }
//        if (Tool.getInstance().isNullOrEmpty(q)) {
//            return "redirect:/login";
//        }
//        List<Movies> entity = searchHotMovieService.findMovieById(null,q);
//        System.out.println(entity);
//        if(entity==null){
//            entity=searchHotMovieService.findMovieBynameid(q);
//        }
//        model.addAttribute("movie", entity);
//        model.addAttribute("userInformation", userInformation);
//        model.addAttribute("action", 3);
//        model.addAttribute("movie_name", q + "搜索结果");
//        getUserCounts(model, userInformation.getId());
//        model.addAttribute("myFriends", getMyFriends(currentUserInfo().getId()));
//        return "movie/hot_movie_information";
//    }
//*/
///*
//    @RequestMapping(value = "/delRedis")
//    @ResponseBody
//    public String delRedis(@Param("name") String name) {
//        return service.delRedis(name) ? "true" : "false";
//    }
//
//    @RequestMapping(value = "/delRedisUrl")
//    @ResponseBody
//    public String delRedisUrl(@Param("url") String url) {
//        return service.delRedisUrl(url) ? "true" : "false";
//    }
//
//
//    @RequestMapping(value = "/comments")
//    @ResponseBody
//    public BaseEntity getMusicComments(@Param("name") String name) {
//        return service.getMusicComments(name);
//    }
//
//    @RequestMapping(value = "/comments/id")
//    @ResponseBody
//    public BaseEntity getMusicComments(@Param("id") long id) {
//        return service.getMusicComments(id);
//    }
//
//    @RequestMapping(value = "/hot/{type}")
//    @ResponseBody
//    public BaseEntity getHotMusic(@PathVariable("type") int type) {
//        switch (type) {
//            case 1:
//                //云音乐热歌榜
//                return service.getHotMusic();
//            case 2:
//                //云音乐飙升榜
//                return service.getHottingMusic();
//            case 3:
//                //云音乐新歌榜
//                return service.getNewMusic();
//            default:
//                throw new RuntimeException("?");
//        }
//    }
//
//    @RequestMapping(value = "/url/{id}")
//    @ResponseBody
//    public BaseEntity getMusicUrlById(@PathVariable("id") long id) {
//        return service.getMusicUrlById(id);
//    }
//
//    @RequestMapping(value = "/free/hot/{type}")
//    public String hot(@PathVariable("type") String type, Model model) {
//        WangYiResponseEntity entity;
//        String title;
//        switch (type) {
//            case "2":
//                //云音乐飙升榜-2
//                entity = (WangYiResponseEntity) service.getHot();
//                title = "音乐飙升榜";
//                break;
//            case "1":
//                //云音乐热歌榜-1
//                entity = (WangYiResponseEntity) service.getHotMusic();
//                title = "音乐热歌榜";
//                break;
//            default: // 云音乐新歌榜-3
//                entity = (WangYiResponseEntity) service.getNewMusic();
//                title = "音乐新歌榜";
//                break;
//        }
//        model.addAttribute("entity", entity.getData());
//        model.addAttribute("action", 6);
//        model.addAttribute("userInformation", new UserInformation());
//        model.addAttribute("username", "");
//        model.addAttribute("autograph", "");
//        model.addAttribute("title", title);
//        return "music/freeHot";
//    }*//*
//
//}
//*/
