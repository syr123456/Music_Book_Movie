package com.wsk.life.service.musicuser;

import com.wsk.life.bean.book.BookEntity;
import com.wsk.life.bean.musicuser.WangYiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @DESCRIPTION :
 * @AUTHOR : WuShukai1103
 * @TIME : 2018/2/15  15:49
 */
public interface UserRepository extends JpaRepository<WangYiUser, String>{

    //随机选择10条数据
    @Query(value = "select b.* from wangyi_user b  order by rand() limit 30 ", nativeQuery = true)
    List<WangYiUser> randUser();

    @Query(value = "select b.* from wangyi_user b WHERE  b.id = :id ", nativeQuery = true)
    WangYiUser findOneUser(@Param("id") String id);

    @Query(value = "select b.id,b.name from wangyi_user b", nativeQuery = true)
    List<String> findUser();

    @Query(value = "select b.name from wangyi_user b", nativeQuery = true)
    String findname(@Param("id") String id);
}