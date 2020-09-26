package com.wsk.life.controllerNo.user;

import com.wsk.life.config.RepeatSubmit;
import com.wsk.life.config.mail.Send;
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
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by wsk1103 on 2017/4/29.
 */
@Controller
public class ForgetController {

    @Resource
    private UserInformationService userInformationService;

    @Resource
    private UserPasswordService userPasswordService;

    //the first step for forget password
    @RequestMapping(value = "forget1", method = {RequestMethod.POST, RequestMethod.GET})
    //@GetMapping(value = "/forget1")
    public String forget(Model model, HttpServletRequest request) {
        String token = TokenProccessor.getInstance().makeToken();
        request.getSession().setAttribute("forget1_token", token);
        model.addAttribute("token", token);
        return "forget/forgetPassword";
    }

    //the second step for the forget password
    @RequestMapping(value = "forget2", method = {RequestMethod.POST, RequestMethod.GET})
    //@GetMapping(value = "/forget2")
    public String forget(@RequestParam String phone,@RequestParam String photo, @RequestParam String code_phone, @RequestParam String token,
                         HttpServletRequest request, Model model) {
        boolean isRepeatSubmit = RepeatSubmit.isRepeatSubmit(token, (String) request.getSession().getAttribute("forget1_token"));
        if (isRepeatSubmit) {
            return forget(model, request);
        } else {
            request.getSession().removeAttribute("forget1_token");
        }
        String forget2_token = TokenProccessor.getInstance().makeToken();
        //String phone = (String) request.getSession().getAttribute("phone");
        request.getSession().setAttribute("forget2_token", forget2_token);
        model.addAttribute("token", forget2_token);
        model.addAttribute("phone", phone);
        model.addAttribute("photo", photo);
        model.addAttribute("code_phone", code_phone);
        return "forget/forgetPassword2";
    }

    //the end step for forget password
    @RequestMapping(value = "forget3", method = {RequestMethod.POST, RequestMethod.GET})
    //@GetMapping(value = "/forget3")
    public String forget(@RequestParam String phone,@RequestParam String password,
                         HttpServletRequest request, Model model) {
//        boolean isRepeatSubmit = RepeatSubmit.isRepeatSubmit(token, (String) request.getSession().getAttribute("forget2_token"));
        String token = TokenProccessor.getInstance().makeToken();
        request.getSession().setAttribute("token", token);
        //String phone = (String) request.getSession().getAttribute("phone");
        System.out.println(phone);
        model.addAttribute("token", token);
        model.addAttribute("phone", phone);
        model.addAttribute("password", password);
//        model.addAttribute("token", token);
        return "forget/forgetPassword3";
    }

    //modify the password
    @RequestMapping(value = "modify", method = {RequestMethod.POST, RequestMethod.GET})
    //@GetMapping(value = "/mpdify")
    public String modifyPassword(@RequestParam String phone,@RequestParam String password, @RequestParam String token,
                                 HttpServletRequest request, Model model) {
        boolean isRepeatSubmit = RepeatSubmit.isRepeatSubmit(token, (String) request.getSession().getAttribute("forget2_token"));
//        phone = (String) request.getSession().getAttribute("phone");
        System.out.println(phone);
        if (isRepeatSubmit) {
            return forget(model, request);
        } else {
            request.getSession().removeAttribute("forget2_token");
        }
        //String md5Password = Tool.getInstance().getMD5(password);
        UserPassword userPassword = new UserPassword();
        userPassword.setId(getUserInformationId(phone));
        userPassword.setPassword(password);
        userPassword.setAllow((short) 1);
        userPassword.setModified(new Date());
        try {
            int id = userPasswordService.updatePassword(userPassword);
            if (id == 0) {
                return forget(phone,"", "", token, request, model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("foeget3");
        return forget(phone,password, request, model);
    }

    //when the user click next button,check the information
    @RequestMapping(value = "checkPhone", method = {RequestMethod.POST, RequestMethod.GET})
    //@GetMapping(value = "/checkPhone")
    @ResponseBody
    public Map checkPhone(HttpServletRequest request, @RequestParam String photo,
                          @RequestParam String code_phone,String action) {
        Map<String, String> map = new HashMap<>();
        map.put("result", "1");//true result;
        if (checkPhoto(photo, request)!=true) {
            map.put("result", "2");//图片验证码错误
            return map;
        } else {
            if (checkCodePhone(code_phone, request)!=true) {
                //check the phone`s code,and it is false;
                map.put("result", "3");
                return map;
            }
        }
        System.out.println(map);
        return map;
    }

    //检验验证码
    static boolean checkPhoto(String photo, HttpServletRequest request) {
        photo = photo.toLowerCase();
        //String true_photo = (String) request.getSession().getAttribute("rand");
        String true_photo = "xcsk";
        return true_photo.equals(photo);
    }

    //check the phone`s code
    static boolean checkCodePhone(String code_phone, HttpServletRequest request) {
        //String true_code_phone = (String) request.getSession().getAttribute("codePhone");
        String true_code_phone = "12251103";
        return code_phone.equals(true_code_phone);
    }

    /*//send the Email to the phone
    @RequestMapping(value = "sendEmail", method = {RequestMethod.POST, RequestMethod.GET})
    //@GetMapping(value = "/sendEmail")
    //@ResponseBody
    public Map sendEmail(HttpServletRequest req, HttpServletResponse res, String phone, String photo, String action) {
        Map<String, String> map = new HashMap<>();
        map.put("result", "-1");
        res.setContentType("text/html;charset=UTF-8");//编码
        if (!checkPhoto(photo, req)) {
            map.put("result", "0");
            return map;
        }
        if (action.equals("forget")) {
            if (!isUserPhoneExists(phone)) {
//            map.put("result", "1");
                return map;
            }
        } else if (action.equals("register")) {
            if (isUserPhoneExists(phone)) {
                return map;
            }
        }
        //get the random num to phone which should check the phone to judge the phone is belong user
        getRandomForCodePhone(req);
        try {
            Send.sendEmail(phone, req, res);
            map.put("result", "1");
        } catch (MessagingException me) {
            me.printStackTrace();
            map.put("result", "0");
        }
        return map;
    }

    //To determine whether the user's mobile phone number exists
    private boolean isUserPhoneExists(String phone) {
        UserInformation userInformation = userInformationService.selectIdByPhone(phone);
        if (Tool.getInstance().isNullOrEmpty(userInformation)) {
            return false;
        }
        String userPhone = userInformation.getPhone();
        return !userPhone.equals("");
    }

    // get the random phone`s code
    private void getRandomForCodePhone(HttpServletRequest req) {
        Random random = new Random();
        String r = "";
        for (int i = 0; i < 4; i++) {
            r += random.nextInt(10);
        }
        System.out.println(r);
        req.getSession().setAttribute("codePhone", r);
    }
*/

    private int getUserInformationId(String phone) {
        int id = 0;
        try {
            UserInformation userInformation = this.userInformationService.selectIdByPhone(phone);
            id = userInformation.getId();
            System.out.println(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}
