package com.wsk.life.service.musicuser;

import com.wsk.life.bean.book.BookEntity;
import com.wsk.life.bean.book.BookNameEntity;
import com.wsk.life.bean.musicuser.WangYiUser;

import java.io.IOException;
import java.util.List;

/**
 * @author: wsk1103
 * @date: 18-1-14 下午7:37
 * @description: JAVA8
 */
public interface UserService {

    void addUser(String name) throws IOException;
    void addGedan(String userid) throws IOException;

    List<WangYiUser> findUser(String name);

}
