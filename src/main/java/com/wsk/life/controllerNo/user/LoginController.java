package com.wsk.life.controllerNo.user;

import com.wsk.life.config.token.TokenProccessor;
import com.wsk.life.controllerNo.user.UserInformationController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;

/**
 * 用戶登錄，不需要驗證信息
 */
@Controller
public class LoginController {

    private boolean hasCookie = true;

    @Autowired
    private UserInformationController userInformationController;

    @RequestMapping(value = {"/", "login"}, method = RequestMethod.GET)
    @GetMapping("login")
    public String login(Model model, HttpServletRequest request, HttpServletResponse response) {
//        System.out.println(new Date() + ",run login");
        String token = TokenProccessor.getInstance().makeToken();
        request.getSession().setAttribute("token", token);
        model.addAttribute("token", token);
        model.addAttribute("currentYear", Calendar.getInstance().get(Calendar.YEAR));
        Cookie[] cookies = request.getCookies();
        String username = "", password = "";
        if (cookies != null)
            for (Cookie c : cookies) {
                if (c.getName().equals("MovieWeb_username")) {
                    username = c.getValue();
                    System.out.println("username" + username);
                } else if (c.getName().equals("MovieWeb_password")) {
                    password = c.getValue();
                    System.out.println("password" + password);
                }
            }
        if (!username.equals("") && !password.equals("") && hasCookie) {
//            return home(username, password, token, model, request, response);
            return userInformationController.home(username, password, token, model, request, response);
        }
        return "login";
    }

    public boolean isHasCookie() {
        return hasCookie;
    }
    public void setHasCookie(boolean hasCookie) {
        this.hasCookie = hasCookie;
    }
}