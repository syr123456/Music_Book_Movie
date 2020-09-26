package com.wsk.life.controllerNo.user;

import com.wsk.life.config.RepeatSubmit;
import com.wsk.life.config.token.TokenProccessor;
import com.wsk.life.config.tool.Tool;
import com.wsk.life.domain.UserInformation;
import com.wsk.life.domain.UserPassword;
import com.wsk.life.service.UserInformationService;
import com.wsk.life.service.UserPasswordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Controller
public class RegisterController {

    @Resource
    private UserInformationService userInformationService;

    @Resource
    private UserPasswordService userPasswordService;

    //the first step for register
    @RequestMapping(value = "register1",method = {RequestMethod.POST, RequestMethod.GET})
    //@GetMapping(value = "/register")
    public String register(Model model, HttpServletRequest request) {
        String token = TokenProccessor.getInstance().makeToken();
        request.getSession().setAttribute("register1_token", token);
        model.addAttribute("token", token);
        String result = Tool.getInstance().getProtocolTxt();
        model.addAttribute("txt", result);
        request.getSession().setAttribute("rand","7834");
        model.addAttribute("random", "7834");
        return "registered/register";
    }

    //the second step for the register
    @RequestMapping(value = "register2",method = {RequestMethod.POST, RequestMethod.GET})
    //@GetMapping(value = "/register2")
    public String register(@RequestParam String phone,@RequestParam String photo, @RequestParam String code_phone, @RequestParam String token,
                           Model model, HttpServletRequest request) {
        boolean isRepeatSubmit = RepeatSubmit.isRepeatSubmit(token, (String) request.getSession().getAttribute("register1_token"));
        if (isRepeatSubmit) {
            return register(model, request);
        } else {
            request.getSession().removeAttribute("register1_token");
        }
        //String phone = (String) request.getSession().getAttribute("phone");
        String register2_token = TokenProccessor.getInstance().makeToken();
        request.getSession().setAttribute("register2_token", register2_token);
        request.getSession().setAttribute("phone", phone);
        model.addAttribute("token", register2_token);
        model.addAttribute("phone", phone);
        model.addAttribute("photo", photo);
        model.addAttribute("code_phone", code_phone);
        return "registered/register2";
    }


    //the end step for the register
    @RequestMapping(value = "register3",method = {RequestMethod.POST, RequestMethod.GET})
    //@GetMapping(value = "/register3")
    public String register(@RequestParam String phone,@RequestParam String password,
                           HttpServletRequest request, Model model) {
        String token = TokenProccessor.getInstance().makeToken();
        //String phone = (String) request.getSession().getAttribute("phone");
        request.getSession().setAttribute("token", token);
        model.addAttribute("token", token);
        model.addAttribute("phone", phone);
        model.addAttribute("password", password);
        return "registered/register3";
    }


    //fill in the password
    @RequestMapping(value = "fillPassword",method = {RequestMethod.POST, RequestMethod.GET})
    //@GetMapping(value = "/fillPassword")
    public String fillPassword(@RequestParam String phone,@RequestParam String password, @RequestParam String token, @RequestParam String username,
                               Model model, HttpServletRequest request) {
        //String phone = (String) request.getSession().getAttribute("phone");
        boolean isRepeatSubmit = RepeatSubmit.isRepeatSubmit(token, (String) request.getSession().getAttribute("register2_token"));
        if (isRepeatSubmit) {
            return register(model, request);
        } else {
            request.getSession().removeAttribute("forget2_token");
        }
        try {
            UserInformation userInformation = new UserInformation();
            userInformation.setName(username);
            userInformation.setPhone(phone);
            userInformation.setModified(new Date());
            userInformation.setBuildtime(new Date());
            userInformation.setAvatar("/images/wuqiong.jpg");
            int id_userinformation = userInformationService.insertSelective(userInformation);
            int uid = getUserInformationId(phone);
            if (id_userinformation !=1){
                try {
                    userInformationService.deleteByPrimaryKey(uid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //String md5Password = Tool.getInstance().getMD5(password);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            UserPassword userPassword = new UserPassword();
            userPassword.setId(getUserInformationId(phone));
            userPassword.setPassword(password);
            userPassword.setAllow((short) 1);
            userPassword.setModified(new Date());
            int id = userPasswordService.insertUser(userPassword);
            if (id == 0 || id_userinformation == 0) {
                return register(phone, "", "", token, model, request);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return register(phone, password, request, model);
    }

    //get the wsk`s protocol(协议)
    //@RequestMapping(value = "getProtocol",method = {RequestMethod.POST,RequestMethod.GET})
    @GetMapping(value = "/getProtocol")
    @ResponseBody
    public Map getProtocol(){
        Map<String, String> map = new HashMap<>();
        Tool handleFile = null;
        String result = handleFile.readFile();
        System.out.println(map);

        map.put("result", result);

        return map;
    }

    private int getUserInformationId(String phone) {
        int id = 0;
        try {
            UserInformation userInformation = this.userInformationService.selectIdByPhone(phone);
            id = userInformation.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}
