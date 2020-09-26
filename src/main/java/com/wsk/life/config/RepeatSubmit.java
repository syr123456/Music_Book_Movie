package com.wsk.life.config;

import com.wsk.life.config.tool.Tool;

/**
 *UserInformationController+RegisterController+ForgetController
 */
public class RepeatSubmit {
    public static boolean isRepeatSubmit(String user_token,String client_token) {
//        String token = request.getParameter(user_token);
        if (Tool.getInstance().isNullOrEmpty(user_token)) {
            return true;
        }
//        client_token = (String) request.getSession().getAttribute(client_token);
        return Tool.getInstance().isNullOrEmpty(client_token) || !client_token.equals(user_token);
    }
}
