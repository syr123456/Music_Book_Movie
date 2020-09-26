package com.wsk.life.config.webSocket;

import com.wsk.life.config.tool.SaveSession;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wsk1103 on 2017/5/22.
 */
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final static List<Object> SESSIONS = Collections.synchronizedList(new ArrayList<>());

    //接收文本消息，并发送出去
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {

//        System.out.println(session.getId() + ":send....");
//        chatTextMessageHandler(message.getPayload());
        try {
//            super.handleTextMessage(session, message);
//            System.out.println(session.getId() + " :" + message.getPayload() + "   " + new Date());
            String m = message.getPayload();
            String[] wsk = m.split(",");
            String phone = wsk[0];
            long time = Long.parseLong(wsk[1]);
            String action = wsk[2];
            if (action.equals("start")) {
                session.sendMessage(new TextMessage("success"));
                SaveSession.getInstance().save(phone, time);
                return;
            }
            boolean b = SaveSession.getInstance().isHave(phone, time);
            if (b) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage("error"));
                }
            } else {
                if (session.isOpen()) {

                    session.sendMessage(new TextMessage("success"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                session.sendMessage(new TextMessage("error"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    //连接建立后处理
    @SuppressWarnings("unchecked")
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
//        System.out.println(session.getId() + ":start.....");
        SESSIONS.add(session);
        //处理离线消息
    }

    //抛出异常时处理
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
//        System.out.println(session.getId() + ":start error");
        if (session.isOpen()) {
            session.close();
        }
        SESSIONS.remove(session);
    }

    //连接关闭后处理
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
//        System.out.println(session.getId() + ":close......");
        SESSIONS.remove(session);
    }

}