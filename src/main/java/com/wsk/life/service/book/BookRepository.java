package com.wsk.life.service.book;

import com.wsk.life.bean.book.BookEntity;
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
public interface BookRepository extends JpaRepository<BookEntity, String>{

    //随机选择10条数据
    @Query(value = "select b.* from book b WHERE  status = 1 order by rand() limit 10 ", nativeQuery = true)
    List<BookEntity> randBook();

    @Query(value = "update book b set b.status = :status where b.id = :id", nativeQuery = true)
    @Modifying
    @Transactional
    Integer changeBookStatus(@Param("id") String id, @Param("status") int status);


    @Query(value = "select b.* from book b WHERE  b.id = :id ", nativeQuery = true)
    BookEntity findOneBook(@Param("id") long id);
}