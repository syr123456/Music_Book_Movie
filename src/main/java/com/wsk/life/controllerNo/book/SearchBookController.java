package com.wsk.life.controllerNo.book;

import com.wsk.life.bean.book.BookEntity;
import com.wsk.life.bean.book.BookNameEntity;
import com.wsk.life.service.book.SearchBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @DESCRIPTION :
 * @AUTHOR : WuShukai1103
 * @TIME : 2018/2/15  16:03
 */
@RestController
@RequestMapping(value = "book/search")
public class SearchBookController {
    private final SearchBookService searchBookService;

    @Autowired
    public SearchBookController(SearchBookService searchBookService) {
        this.searchBookService = searchBookService;
    }

    @RequestMapping(value = "name/{name}")
    public List<BookNameEntity> searchByName(@PathVariable(value = "name") String name) throws IOException {
        return searchBookService.searchBookByName(name);
    }


    @RequestMapping(value = "init")
    public List<BookEntity> bookRand(){
        return searchBookService.randBook();
    }

}
