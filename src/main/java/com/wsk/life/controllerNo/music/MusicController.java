package com.wsk.life.controllerNo.music;

import com.wsk.life.bean.music.*;
import com.wsk.life.controllerNo.user.UserInformationController;
import com.wsk.life.config.error.LoginErrorException;
import com.wsk.life.service.music.*;
import com.wsk.life.domain.UserInformation;
import com.wsk.life.config.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * @DESCRIPTION :音乐控制中心
 * @AUTHOR : WuShukai1103
 * @TIME : 2018/2/10  17:30
 */
@Controller
@RequestMapping(value = "music")
public class MusicController {
    private final WangYiService service;
    private final UserInformationController userController;
    private final SingerRepository singerRepository;
    private final SingerAlbumRepository singerAlbumRepository;
    private final SingerMVRepository singerMVRepository;
    private final AlbumRepository albumRepository;
    private final AllMusicsRepository allMusicsRepository;
   /* @Autowired
    private WangYiMusicRepository musicRepository;
    @Autowired
    private WangYiAlbumRepository albumRepository;*/

    @Autowired
    public MusicController(AllMusicsRepository allMusicsRepository,AlbumRepository albumRepository,SingerMVRepository singerMVRepository,SingerAlbumRepository singerAlbumRepository,SingerRepository singerRepository,WangYiService service, UserInformationController controller) {
        this.service = service;
        this.userController = controller;
        this.singerRepository=singerRepository;
        this.singerAlbumRepository=singerAlbumRepository;
        this.singerMVRepository=singerMVRepository;
        this.albumRepository=albumRepository;
        this.allMusicsRepository=allMusicsRepository;
    }

    @RequestMapping(value = "hot/{type}")
    public String hot(@PathVariable("type") Integer type, HttpServletRequest request, Model model) throws IOException {
        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
        if (Tool.getInstance().isNullOrEmpty(userInformation)) {
            return "redirect:/login";
        }
        WangYiTypeEntity typeEntity = null;
        List<WangYiResponseEntity> entity;
        String title;
        switch (type) {
            case 1:
                //云音乐热歌榜-1
                typeEntity = service.getType("3778678");
                service.getHot("3778678","1");
                entity = service.randMusics("1");
                title = "音乐热歌榜";
                break;
            case 2 ://云音乐飙升榜-2
                typeEntity = service.getType("19723756");
                service.getHot("19723756","2");
                entity =service.randMusics("2");
                title = "音乐飙升榜";
                break;
            case 3: //云音乐新歌榜-3
                typeEntity = service.getType("3779629");
                service.getHot("3779629","3");
                entity =service.randMusics("3");
                title = "音乐新歌榜";
                break;
            default://云音乐原创榜-1
                typeEntity = service.getType("2884035");
                service.getHot("2884035","4");
                entity =service.randMusics("4");
                title = "音乐原创榜";
                break;
        }
        model.addAttribute("typeint",type);
        model.addAttribute("type",typeEntity);
        model.addAttribute("entity", entity);
        model.addAttribute("myFriends", userController.getMyFriends(userInformation.getId()));
        model.addAttribute("userInformation", userInformation);
        model.addAttribute("username", userInformation.getName());
        model.addAttribute("autograph", userInformation.getAutograph());
        model.addAttribute("action", 6);
        model.addAttribute("title", title);
        userController.getUserCounts(model, userInformation.getId());
        return "music/hot";
    }
    //音乐详细信息+获取歌曲歌词
    @RequestMapping(value = "information/{typeid}/{id}")
    public String information(@PathVariable("typeid")String typeid ,@PathVariable("id")String id ,HttpServletRequest request, Model model) throws IOException {
        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
        if (Tool.getInstance().isNullOrEmpty(userInformation)) {
            return "redirect:/login";
        }
        //返回结果
        WangYiResponseEntity entity=allMusicsRepository.findmusicbyid(id);
        List<WangYiLyricEntity> lyric_music=service.findlyric(id);
        System.out.println(lyric_music);
//        String play="http://m10.music.126.net/20200816010420/00b21464d6687de60e6e2a0d13dd0e4e/ymusic/07fa/a2a1/35ea/732937117d6d0a8c13a81bb40184662e.mp3";
        model.addAttribute("entity", entity);
        model.addAttribute("lyric",lyric_music);
//        model.addAttribute("play",play);
//        model.addAttribute("id",Integer.valueOf(id));
        model.addAttribute("myFriends", userController.getMyFriends(userInformation.getId()));
        model.addAttribute("userInformation", userInformation);
        model.addAttribute("username", userInformation.getName());
        model.addAttribute("autograph", userInformation.getAutograph());
        model.addAttribute("action", 6);
//        model.addAttribute("title", wangyimusicEntity.getName());
        userController.getUserCounts(model, userInformation.getId());
        return "music/information";
    }
    //根据歌曲id获取歌曲热评
    @RequestMapping(value = "information/content/{id}")
    public String musiccontent(@PathVariable("id")String id ,HttpServletRequest request, Model model) throws IOException {
        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
        if (Tool.getInstance().isNullOrEmpty(userInformation)) {
            return "redirect:/login";
        }
        //返回结果
        List<WangYiContent> entity=service.findcontentbyid(id);
        String name=allMusicsRepository.findName(id);
        model.addAttribute("entity", entity);
        model.addAttribute("id", id);
        model.addAttribute("name",name);
        model.addAttribute("myFriends", userController.getMyFriends(userInformation.getId()));
        model.addAttribute("userInformation", userInformation);
        model.addAttribute("username", userInformation.getName());
        model.addAttribute("autograph", userInformation.getAutograph());
        model.addAttribute("action", 6);
//        model.addAttribute("title", wangyimusicEntity.getName());
        userController.getUserCounts(model, userInformation.getId());
        return "music/content";
    }
    //歌手详细信息
    @RequestMapping(value = "information/singer/{id}")
    public String singer(@PathVariable("id")String id ,HttpServletRequest request, Model model) throws IOException {
        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
        if (Tool.getInstance().isNullOrEmpty(userInformation)) {
            return "redirect:/login";
        }
        //返回结果
        int flag=0;
        WangYiSingerEntity singer=new WangYiSingerEntity();
        singer=singerRepository.findSinger(id);
        if (singer== null) {
            flag=1;
        }
        System.out.println(flag);
        //获取歌手详细信息
        List<WangYiSingerDetailEntity> detail=service.findsingerdetail(id,flag);
        if(flag==1){
            singer=singerRepository.findSinger(id);
        }
        model.addAttribute("singer", singer);
        model.addAttribute("detail",detail);
        model.addAttribute("myFriends", userController.getMyFriends(userInformation.getId()));
        model.addAttribute("userInformation", userInformation);
        model.addAttribute("username", userInformation.getName());
        model.addAttribute("autograph", userInformation.getAutograph());
        model.addAttribute("action", 6);
//        model.addAttribute("title", wangyimusicEntity.getName());
        userController.getUserCounts(model, userInformation.getId());
        return "music/singer";
    }
    //歌手相关歌曲
    @RequestMapping(value = "information/singer_music/{id}")
    public String singermusic(@PathVariable("id")String id ,HttpServletRequest request, Model model) throws IOException {
        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
        if (Tool.getInstance().isNullOrEmpty(userInformation)) {
            return "redirect:/login";
        }
        //返回结果
        WangYiSingerEntity singer=singerRepository.findSinger(id);
        //获取歌手的歌曲
        service.findsingermusic(id);
        List<WangYiResponseEntity> music=service.randMusics(id);
        model.addAttribute("singer", singer);
        model.addAttribute("music",music);
        model.addAttribute("myFriends", userController.getMyFriends(userInformation.getId()));
        model.addAttribute("userInformation", userInformation);
        model.addAttribute("username", userInformation.getName());
        model.addAttribute("autograph", userInformation.getAutograph());
        model.addAttribute("action", 6);
//        model.addAttribute("title", wangyimusicEntity.getName());
        userController.getUserCounts(model, userInformation.getId());
        return "music/singer_music";
    }
    //歌手相关专辑
    @RequestMapping(value = "information/singer_album/{id}")
    public String singeralbum(@PathVariable("id")String id ,HttpServletRequest request, Model model) throws IOException {
        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
        if (Tool.getInstance().isNullOrEmpty(userInformation)) {
            return "redirect:/login";
        }
        //返回结果
        int flag=0;
        WangYiSingerEntity singer=singerRepository.findSinger(id);
        //获取歌手相关专辑信息
        service.findsingeralbum(id);
        List<WangYiAlbumEntity> album=singerAlbumRepository.randAlbums(id);
        model.addAttribute("singer", singer);
        model.addAttribute("album",album);
        model.addAttribute("myFriends", userController.getMyFriends(userInformation.getId()));
        model.addAttribute("userInformation", userInformation);
        model.addAttribute("username", userInformation.getName());
        model.addAttribute("autograph", userInformation.getAutograph());
        model.addAttribute("action", 6);
//        model.addAttribute("title", wangyimusicEntity.getName());
        userController.getUserCounts(model, userInformation.getId());
        return "music/singer_album";
    }
    //歌手相关MV
    @RequestMapping(value = "information/singer_mv/{id}")
    public String singermv(@PathVariable("id")String id ,HttpServletRequest request, Model model) throws IOException {
        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
        if (Tool.getInstance().isNullOrEmpty(userInformation)) {
            return "redirect:/login";
        }
        //返回结果
        WangYiSingerEntity singer=singerRepository.findSinger(id);
        //获取歌手相关MV
        service.findsingerMV(id);
        List<WangYiMVEntity> mv=singerMVRepository.randMvs(id);
        model.addAttribute("singer", singer);
        model.addAttribute("mv",mv);
        model.addAttribute("myFriends", userController.getMyFriends(userInformation.getId()));
        model.addAttribute("userInformation", userInformation);
        model.addAttribute("username", userInformation.getName());
        model.addAttribute("autograph", userInformation.getAutograph());
        model.addAttribute("action", 6);
//        model.addAttribute("title", wangyimusicEntity.getName());
        userController.getUserCounts(model, userInformation.getId());
        return "music/singer_mv";
    }
    //歌手专辑相关信息
    @RequestMapping(value = "information/album/{id}")
    public String album(@PathVariable("id")String id ,HttpServletRequest request, Model model) throws IOException {
        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
        if (Tool.getInstance().isNullOrEmpty(userInformation)) {
            return "redirect:/login";
        }
        //返回结果
        WangYiAlbum album=albumRepository.randAlbums(id);
        if(album==null){
            service.findalbum(id);
        }
        //获取专辑的歌曲
        List<WangYiResponseEntity> music=service.randMusics(id);
        model.addAttribute("album", album);
        model.addAttribute("music",music);
        model.addAttribute("myFriends", userController.getMyFriends(userInformation.getId()));
        model.addAttribute("userInformation", userInformation);
        model.addAttribute("username", userInformation.getName());
        model.addAttribute("autograph", userInformation.getAutograph());
        model.addAttribute("action", 6);
//        model.addAttribute("title", wangyimusicEntity.getName());
        userController.getUserCounts(model, userInformation.getId());
        return "music/album_music";
    }

    //只有登录的用户才能使用音乐播放功能，防止音乐被盗用
    @RequestMapping(value = "{id}")
    public String checkIdentity(@PathVariable("id") String id, HttpServletRequest request) {
        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
        if (Tool.getInstance().isNullOrEmpty(userInformation)) {
            throw new LoginErrorException("用户未登录!");
        }
        System.out.println(userInformation.getPhone());
        System.out.println(id);
        return "redirect:/musics/" + id + ".mp3";
    }

}
