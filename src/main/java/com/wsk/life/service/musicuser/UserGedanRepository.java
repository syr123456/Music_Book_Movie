package com.wsk.life.service.musicuser;

import com.wsk.life.bean.musicuser.UserGedan;
import com.wsk.life.bean.musicuser.WangYiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @DESCRIPTION :
 * @AUTHOR : WuShukai1103
 * @TIME : 2018/2/15  15:49
 */
public interface UserGedanRepository extends JpaRepository<UserGedan, String>{

    //随机选择10条数据
    @Query(value = "select b.* from wangyi_user_gedan b WHERE  b.userid = :userid ", nativeQuery = true)
    List<UserGedan> randGedan(@Param("userid") String userid);

    @Query(value = "select b.* from wangyi_user_gedan b WHERE  b.id = :id ", nativeQuery = true)
    UserGedan findOneGedan(@Param("id") String id);
}