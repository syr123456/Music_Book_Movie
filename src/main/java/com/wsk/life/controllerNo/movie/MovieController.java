package com.wsk.life.controllerNo.movie;

import com.alibaba.fastjson.JSON;
import com.wsk.life.bean.movie.*;
import com.wsk.life.bean.movie.maoyan.*;
import com.wsk.life.controllerNo.user.AsideController;
import com.wsk.life.domain.UserInformation;
import com.wsk.life.config.redis.IRedisUtils;
import com.wsk.life.config.tool.HttpUtils;
import com.wsk.life.config.tool.Tool;
import com.wsk.life.service.movie.SearchHotMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by wsk1103 on 2017/10/22.
 */
@Controller
public class MovieController extends AsideController {
    private static final String ENCODE = "UTF-8";
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String URL = "http://op.juhe.cn/onebox/movie/video";
    private static final String KEY = "e712295ae7ca460ec31624dd3dfe2094";
    private static final String DOUBAN_URL = "https://api.douban.com";
    @Resource
    private final SearchHotMovieService searchHotMovieService;

    @Autowired
    public MovieController(SearchHotMovieService searchHotMovieService, IRedisUtils redisUtils) {
        super(redisUtils);
        this.searchHotMovieService = searchHotMovieService;
    }


    //按照名字查询电影
    @RequestMapping(value = "search/movie/result")
    public String searchMovieResult(Model model, HttpServletRequest request, @ModelAttribute("name") String q) throws IOException {
        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
        if (Tool.getInstance().isNullOrEmpty(userInformation)) {
            return "redirect:/login";
        }
        if (Tool.getInstance().isNullOrEmpty(q)) {
            return "redirect:/login";
        }
        List<Movies> entity = searchHotMovieService.findMovieById(null,q);
        System.out.println(entity);
        if(entity==null){
            entity=searchHotMovieService.findMovieBynameid(q);
        }
        model.addAttribute("movie", entity);
        model.addAttribute("userInformation", userInformation);
        model.addAttribute("action", 3);
        model.addAttribute("movie_name", q + "搜索结果");
        getUserCounts(model, userInformation.getId());
        model.addAttribute("myFriends", getMyFriends(currentUserInfo().getId()));
        return "movie/hot_movie_information";
    }

/*
    //查看电影信息
    @RequestMapping(value = "search/movie/information")
    public String searchMovie(Model model, HttpServletRequest request, @RequestParam String id) {
        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
        if (Tool.getInstance().isNullOrEmpty(userInformation)) {
            return "redirect:/login";
        }
        model.addAttribute("userInformation", userInformation);
        String url = DOUBAN_URL + "/v2/movie/subject/" + id;
        System.out.println(url);
        Map map = new HashMap();
        String name = "";
        try {
            String result = HttpUtils.submitPostData(url, map, ENCODE, GET);
            System.out.println(result);
            OneSubject subject = JSON.parseObject(result, OneSubject.class);
            String down = "ftp://www.wsk1103.cc:8088/down/" + id + "/" + subject.getTitle() + ".mkv";
            model.addAttribute("down", down);
            model.addAttribute("subject", subject);
            name = subject.getTitle();
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("result", "error");
        }
        MovieBean movieBean;
        Map<String, String> params = new HashMap<>();
        params.put("key", KEY);
        params.put("q", name);
        params.put("dtype", "json");
        try {
//            String sr = HttpUtils.submitPostData(URL, params, ENCODE, POST);
            String sr = POSTtoJSON.getInstance().post(URL, params, POST);
            movieBean = JSON.parseObject(sr, MovieBean.class);
        } catch (Exception e) {
            e.printStackTrace();
            movieBean = new MovieBean();
        }
        model.addAttribute("movie", movieBean);
        model.addAttribute("result", "success");
        model.addAttribute("action", 3);
        getUserCounts(model, userInformation.getId());
        model.addAttribute("myFriends", getMyFriends(currentUserInfo().getId()));
        return "information/movieInformation";
    }
 */


    //即将上映的电影(最受期待)
    @RequestMapping(value = "coming/soon")
    public String comingSoon(Model model, HttpServletRequest request) throws IOException {
        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
        if (Tool.getInstance().isNullOrEmpty(userInformation)) {
            return "redirect:/login";
        }
        searchHotMovieService.InsertMovie("coming");
        System.out.println("songyaru\n\n\nsongyaaru\n\n\nsongyaru ");
        List<Hot> entities = searchHotMovieService.randMovies(101);
        model.addAttribute("movie", entities);
        model.addAttribute("userInformation", userInformation);
        model.addAttribute("movie_name", "即将上映");
        model.addAttribute("action", 3);
        getUserCounts(model, userInformation.getId());
        model.addAttribute("myFriends", getMyFriends(currentUserInfo().getId()));
        return "movie/coming_movie";
    }

    //top榜单
    @RequestMapping(value = "top")
    public String top(Model model, HttpServletRequest request) throws IOException {
        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
        if (Tool.getInstance().isNullOrEmpty(userInformation)) {
            return "redirect:/login";
        }
        searchHotMovieService.top();
        List<Top> entities = searchHotMovieService.ranktop();
        model.addAttribute("movie", entities);
        model.addAttribute("userInformation", userInformation);
        model.addAttribute("movie_name", "高分电影");
        model.addAttribute("action", 3);
        getUserCounts(model, userInformation.getId());
        model.addAttribute("myFriends", getMyFriends(currentUserInfo().getId()));
        return "movie/top_movie";
    }

    //top榜单详情
    @RequestMapping(value = "top/movie/result/{id}")
    public String topMovies(@PathVariable("id") Integer id, Model model, HttpServletRequest request) throws IOException {
        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
        if (Tool.getInstance().isNullOrEmpty(userInformation)) {
            return "redirect:/login";
        }
        searchHotMovieService.InsertTopMovie(id);
        List<Hot> entities = searchHotMovieService.randMovies(id);
        Top top = searchHotMovieService.findTopById(id);
        model.addAttribute("top",top);
        model.addAttribute("movie", entities);
        model.addAttribute("userInformation", userInformation);
        model.addAttribute("movie_name", "高分电影");
        model.addAttribute("action", 3);
        getUserCounts(model, userInformation.getId());
        model.addAttribute("myFriends", getMyFriends(currentUserInfo().getId()));
        return "movie/top_movie_result";
    }

    //热映电影（热映口碑榜）
    @RequestMapping(value = "coming")
    public String coming(Model model, HttpServletRequest request) throws IOException {
        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
        if (Tool.getInstance().isNullOrEmpty(userInformation)) {
            return "redirect:/login";
        }
        searchHotMovieService.InsertMovie("hot");
        System.out.println("songyaru\n\n\nsongyaaru\n\n\nsongyaru ");
        List<Hot> entities = searchHotMovieService.randMovies(100);
        model.addAttribute("movie", entities);
        model.addAttribute("userInformation", userInformation);
//        model.addAttribute("movie_name", "正在上映");
        model.addAttribute("action", 3);
        getUserCounts(model, userInformation.getId());
        model.addAttribute("myFriends", getMyFriends(currentUserInfo().getId()));
        return "movie/hot_movie";
    }


    //电影详情（电影详情）
    @RequestMapping(value = "hot/movie/information/{id}")
    public String hotMovieInformation(@PathVariable("id") String id, Model model, HttpServletRequest request) throws IOException {
        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
        if (Tool.getInstance().isNullOrEmpty(userInformation)) {
            return "redirect:../login";
        }
        List<Movies> entity = searchHotMovieService.findMovieById(id,null);
        model.addAttribute("userInformation", userInformation);
        model.addAttribute("movie", entity);
        model.addAttribute("action", 3);
        getUserCounts(model, userInformation.getId());
        model.addAttribute("myFriends", getMyFriends(currentUserInfo().getId()));
        return "movie/hot_movie_information";
    }

    //电影照片
    @RequestMapping(value = "search/movie/images/{id}")
    public String searchMovieImages(@PathVariable("id") String id, Model model, HttpServletRequest request) throws IOException, ScriptException {
        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
        if (Tool.getInstance().isNullOrEmpty(userInformation)) {
            return "redirect:../login";
        }

        String[] entity = searchHotMovieService.findMovieImages(id);
        Integer length=entity.length;
        System.out.println(length);
        model.addAttribute("userInformation", userInformation);
        model.addAttribute("length", length);
        model.addAttribute("id", id);
        model.addAttribute("image", entity);
        model.addAttribute("action", 3);
        getUserCounts(model, userInformation.getId());
        model.addAttribute("myFriends", getMyFriends(currentUserInfo().getId()));
        return "movie/movie_images";
    }
    //电影海报
    @RequestMapping(value = "search/movie/images/posters/{id}")
    public String searchMoviePoster(@PathVariable("id") String id, Model model, HttpServletRequest request) throws IOException, ScriptException {
        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
        if (Tool.getInstance().isNullOrEmpty(userInformation)) {
            return "redirect:../login";
        }

        String[] entity = searchHotMovieService.findMoviePosters(id);
        Integer length=entity.length;
        System.out.println(length);
        model.addAttribute("userInformation", userInformation);
        model.addAttribute("length", length);
        model.addAttribute("id", id);
        model.addAttribute("image", entity);
        model.addAttribute("action", 3);
        getUserCounts(model, userInformation.getId());
        model.addAttribute("myFriends", getMyFriends(currentUserInfo().getId()));
        return "movie/movie_images_poster";
    }
    //电影视频
    @RequestMapping(value = "search/movie/videos/{id}")
    public String searchMovies(@PathVariable("id") String id, Model model, HttpServletRequest request) throws IOException, ScriptException {
        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
        if (Tool.getInstance().isNullOrEmpty(userInformation)) {
            return "redirect:../login";
        }
        List<Vedio> vedio = searchHotMovieService.findMovies(id);

        model.addAttribute("id", id);
        model.addAttribute("vedio", vedio);
        model.addAttribute("userInformation", userInformation);
        model.addAttribute("action", 3);
        getUserCounts(model, userInformation.getId());
        model.addAttribute("myFriends", getMyFriends(currentUserInfo().getId()));
        return "movie/movie_vedios";
    }

    //电影演员
    @RequestMapping(value = "search/movie/actors/{id}")
    public String searchActors(@PathVariable("id") String id, Model model, HttpServletRequest request) throws IOException, ScriptException {
        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
        if (Tool.getInstance().isNullOrEmpty(userInformation)) {
            return "redirect:../login";
        }
        List<Actor> actor=searchHotMovieService.findActors(id);
        model.addAttribute("id", id);
        model.addAttribute("actor", actor);
        model.addAttribute("userInformation", userInformation);
        model.addAttribute("action", 3);
        getUserCounts(model, userInformation.getId());
        model.addAttribute("myFriends", getMyFriends(currentUserInfo().getId()));
        return "movie/movie_actors";
    }

    //电影评论
    @RequestMapping(value = "search/movie/chats/{id}/{title}")
    public String searchChats(@PathVariable("id") String id,@PathVariable("title") String title, Model model, HttpServletRequest request) throws IOException, ScriptException {
        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
        if (Tool.getInstance().isNullOrEmpty(userInformation)) {
            return "redirect:../login";
        }
        List<Comment> chat=searchHotMovieService.findComments(id);
        model.addAttribute("id", id);
        model.addAttribute("chat", chat);
        model.addAttribute("title",title);
        model.addAttribute("userInformation", userInformation);
        model.addAttribute("action", 3);
        getUserCounts(model, userInformation.getId());
        model.addAttribute("myFriends", getMyFriends(currentUserInfo().getId()));
        return "movie/movie_chats";
    }
//
//
//    //北美票房榜
//    @RequestMapping(value = "us/box")
//    public String usBox(Model model, HttpServletRequest request) {
//        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
//        if (Tool.getInstance().isNullOrEmpty(userInformation)) {
//            return "redirect:/login";
//        }
//        model.addAttribute("userInformation", userInformation);
//        String url = DOUBAN_URL + "/v2/movie/us_box";
//        HashMap<String, String> params = new HashMap<>();
//        USbox allInformation = getUsBox(url, params, ENCODE, GET);
//        model.addAttribute("movie", allInformation);
//        model.addAttribute("movie_name", "北美票房");
//        model.addAttribute("action", 3);
//        getUserCounts(model, userInformation.getId());
//        model.addAttribute("myFriends", getMyFriends(currentUserInfo().getId()));
//        return "movie/us_box";
//    }



/*    //查看影人条目信息
    @RequestMapping(value = "celebrity/{id}")
    public String celebrity(@PathVariable String id, Model model, HttpServletRequest request) {
        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
        if (Tool.getInstance().isNullOrEmpty(userInformation)) {
            return "redirect:/login";
        }
        model.addAttribute("userInformation", userInformation);
        String url = DOUBAN_URL + "/v2/movie/celebrity/" + id;
        System.out.println(url);
        Map<String, String> params = new HashMap<>();
        String result = HttpUtils.submitPostData(url, params, ENCODE, GET);
        System.out.println(result);
        model.addAttribute("result", "success");
        CelebrityBean celebrityBean;
        try {
            celebrityBean = JSON.parseObject(result, CelebrityBean.class);
        } catch (Exception e) {
            e.printStackTrace();
            celebrityBean = new CelebrityBean();
            model.addAttribute("result", "error");
        }
        model.addAttribute("celebrity", celebrityBean);
        model.addAttribute("action", 3);
        getUserCounts(model, userInformation.getId());
        model.addAttribute("myFriends", getMyFriends(currentUserInfo().getId()));
        return "movie/celebrity";
    }
    //附件电影院
    @RequestMapping(value = "cinema")
    public String cinemas(Model model, HttpServletRequest request, @PathVariable String id) {
        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
        if (Tool.getInstance().isNullOrEmpty(userInformation)) {
            return "redirect:../login";
        }
        model.addAttribute("userInformation", userInformation);
        model.addAttribute("action", 3);
        getUserCounts(model, userInformation.getId());
        model.addAttribute("myFriends", getMyFriends(currentUserInfo().getId()));
        return "movie/cinemas";
    }
    //单个影院
    @RequestMapping(value = "cinema/{id}")
    public String cinema(@PathVariable String id, Model model, HttpServletRequest request) {
        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
        if (Tool.getInstance().isNullOrEmpty(userInformation)) {
            return "redirect:../login";
        }
        model.addAttribute("userInformation", userInformation);
        String url = "http://m.maoyan.com/showtime/wrap.json?cinemaid=" + id;
        String result = HttpUtils.maoyan(url);
        Cinemas cinema = JSON.parseObject(result, Cinemas.class);

        url = "http://m.maoyan.com/movie/"+id+".json";
        result = HttpUtils.maoyan(url);
        MovieInformation information = JSON.parseObject(result, MovieInformation.class);
        String img = information.getData().getMovieDetailModel().getImg();
        model.addAttribute("img", img);
        model.addAttribute("movie", cinema);
        model.addAttribute("action", 3);
        getUserCounts(model, userInformation.getId());
        model.addAttribute("myFriends", getMyFriends(currentUserInfo().getId()));
        return "movie/cinema";
    }*/

    //获得点赞数量，收藏数量，评论数量
    public void getUserCounts(Model model, int uid) {
        model.addAttribute("comments", commentCriticService.getUserCounts(uid));
        model.addAttribute("critics", publishCriticService.getUserCounts(uid));
        model.addAttribute("goods", goodCriticService.getUserCounts(uid));
        model.addAttribute("collections", collectionCriticService.getUserCounts(uid));
    }

    private AllInformation getMovieInformation(String url, Map params, String encode, String action) {
        AllInformation information;
        try {
            information = JSON.parseObject(HttpUtils.submitPostData(url, params, encode, action), AllInformation.class);
        } catch (Exception e) {
            e.printStackTrace();
            information = new AllInformation();
        }
        return information;
    }

    private USbox getUsBox(String url, Map params, String encode, String action) {
        USbox information;
        try {
            information = JSON.parseObject(HttpUtils.submitPostData(url, params, encode, action), USbox.class);
        } catch (Exception e) {
            e.printStackTrace();
            information = new USbox();
        }
        return information;
    }

   /* private void getFriend(Model model, int uid) {
        List<MyFriends> list = myFriendsService.getFid(uid);
        List<MyFriendsBean> ids = new ArrayList<>();
        for (MyFriends myFriends : list) {
            UserInformation userInformation = userInformationService.selectByPrimaryKey(myFriends.getFid());
            MyFriendsBean myFriendsBean = new MyFriendsBean();
            myFriendsBean.setAvatar(userInformation.getAvatar());
            myFriendsBean.setFid(myFriends.getFid());
            myFriendsBean.setId(myFriends.getId());
            myFriendsBean.setUid(myFriends.getUid());
            myFriendsBean.setName(userInformation.getName());
            ids.add(myFriendsBean);
        }
        model.addAttribute("myFriends", ids);
    }*/
}
