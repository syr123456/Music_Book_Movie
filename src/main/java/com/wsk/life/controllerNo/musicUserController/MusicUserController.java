package com.wsk.life.controllerNo.musicUserController;

import com.wsk.life.bean.book.BookEntity;
import com.wsk.life.bean.music.*;
import com.wsk.life.bean.musicuser.UserGedan;
import com.wsk.life.bean.musicuser.WangYiUser;
import com.wsk.life.config.error.LoginErrorException;
import com.wsk.life.config.tool.Tool;
import com.wsk.life.controllerNo.user.UserInformationController;
import com.wsk.life.domain.UserInformation;
import com.wsk.life.service.music.*;
import com.wsk.life.service.musicuser.UserGedanRepository;
import com.wsk.life.service.musicuser.UserRepository;
import com.wsk.life.service.musicuser.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * @DESCRIPTION :音乐控制中心
 * @AUTHOR : WuShukai1103
 * @TIME : 2018/2/10  17:30
 */
@Controller
@RequestMapping(value = "user")
public class MusicUserController {

    private final UserInformationController userController;
    private final UserRepository userRepository;
    private final UserGedanRepository userdanRepository;
    private final UserService userService;
    private final WangYiService service;
    @Autowired
    public MusicUserController(WangYiService service,UserGedanRepository userdanRepository,UserService userService,UserInformationController controller,UserRepository userRepository) {
        this.userController=controller;
        this.userRepository=userRepository;
        this.userService=userService;
        this.userdanRepository=userdanRepository;
        this.service=service;
    }

    // 初始化图书信息
    @RequestMapping(value = "init")
    public String userInit(HttpServletRequest request, Model model) throws IOException {
        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
        userService.addUser("Miss宋鲸落");
        List<WangYiUser> entities = userRepository.randUser();
        model.addAttribute("entity", entities);
        model.addAttribute("myFriends", userController.getMyFriends(userInformation.getId()));
        model.addAttribute("userInformation", userInformation);
        model.addAttribute("username", userInformation.getName());
        model.addAttribute("autograph", userInformation.getAutograph());
        model.addAttribute("action", 8);
//        model.addAttribute("title", wangyimusicEntity.getName());
        userController.getUserCounts(model, userInformation.getId());
        return "user/init";
    }

    //图书详情
    @RequestMapping(value = "information/{userId}")
    public String userInformation(@PathVariable("userId") String userId, HttpServletRequest request, Model model) throws IOException {
        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
        WangYiUser entity=userRepository.findOneUser(userId);
        userService.addGedan(userId);
        List<UserGedan> gedan=userdanRepository.randGedan(userId);
        model.addAttribute("entity", entity);
        model.addAttribute("gedan", gedan);
        model.addAttribute("myFriends", userController.getMyFriends(userInformation.getId()));
        model.addAttribute("userInformation", userInformation);
        model.addAttribute("username", userInformation.getName());
        model.addAttribute("autograph", userInformation.getAutograph());
        model.addAttribute("action", 8);
        userController.getUserCounts(model, userInformation.getId());
        return "user/user_information";
    }

    //歌手相关歌曲
    @RequestMapping(value = "gedan/information/{gedanid}/{userName}")
    public String GedanMusic(@PathVariable("gedanid")String gedanid ,@PathVariable("userName")String userName ,HttpServletRequest request, Model model) throws IOException {
        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
        if (Tool.getInstance().isNullOrEmpty(userInformation)) {
            return "redirect:/login";
        }
        UserGedan gedan=userdanRepository.findOneGedan(gedanid);
        service.findusermusic(gedanid);
        List<WangYiResponseEntity> entity=service.randMusics(gedanid);
        model.addAttribute("name",userName);
        model.addAttribute("gedan",gedan);
        model.addAttribute("music",entity);
        model.addAttribute("myFriends", userController.getMyFriends(userInformation.getId()));
        model.addAttribute("userInformation", userInformation);
        model.addAttribute("username", userInformation.getName());
        model.addAttribute("autograph", userInformation.getAutograph());
        model.addAttribute("action", 8);
//        model.addAttribute("title", wangyimusicEntity.getName());
        userController.getUserCounts(model, userInformation.getId());
        return "user/user_gedan";
    }

    @RequestMapping(value = "search/result")
    public String searchUser(@RequestParam String name, Model model, HttpServletRequest request, RedirectAttributes attributes) throws IOException {
        if (Tool.getInstance().isNullOrEmpty(name)) {
            return "redirect:/login";
        }
        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
        userService.addUser(name);
        List<WangYiUser> entities = userService.findUser(name);
        model.addAttribute("entity", entities);
        model.addAttribute("myFriends", userController.getMyFriends(userInformation.getId()));
        model.addAttribute("userInformation", userInformation);
        model.addAttribute("username", userInformation.getName());
        model.addAttribute("autograph", userInformation.getAutograph());
        model.addAttribute("action", 8);
        userController.getUserCounts(model, userInformation.getId());
        return "user/init";
    }
}
