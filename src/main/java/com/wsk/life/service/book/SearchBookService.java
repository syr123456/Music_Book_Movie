package com.wsk.life.service.book;

import com.wsk.life.bean.book.BookEntity;
import com.wsk.life.bean.book.BookNameEntity;

import java.io.IOException;
import java.util.List;

/**
 * @DESCRIPTION :
 * @AUTHOR : WuShukai1103
 * @TIME : 2018/2/15  14:59
 */
public interface SearchBookService {
    List<BookNameEntity> searchBookByName(String name) throws IOException;

    List<BookEntity> randBook();

    BookEntity findById(long id);
}
