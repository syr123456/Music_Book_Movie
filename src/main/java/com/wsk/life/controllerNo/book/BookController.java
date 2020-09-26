package com.wsk.life.controllerNo.book;

import com.wsk.life.bean.book.BookEntity;
import com.wsk.life.service.book.SearchBookService;
import com.wsk.life.controllerNo.user.UserInformationController;
import com.wsk.life.domain.UserInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @DESCRIPTION : 图书控制中心
 * @AUTHOR : WuShukai1103
 * @TIME : 2018/2/26  23:02
 */
@Controller
@RequestMapping(value = "book")
public class BookController {

    private final UserInformationController userController;

    private final SearchBookService searchBookService;

    @Autowired
    public BookController(UserInformationController userInformationController, SearchBookService searchBookService) {
        this.userController = userInformationController;
        this.searchBookService = searchBookService;
    }

    // 初始化图书信息
   @RequestMapping(value = "init")
    public String bookInit(HttpServletRequest request, Model model){
       UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
       List<BookEntity> entities = searchBookService.randBook();
       for (BookEntity entity : entities) {
           entity.setTranslator(entity.getTranslator().replace("[\"", "").replace("\"]", "").replace("\"", ""));
           entity.setAuthor(entity.getAuthor().replace("[\"", "").replace("\"]", "").replace("\"", ""));
       }
       System.out.println(entities);
       model.addAttribute("entity", entities);
       model.addAttribute("myFriends", userController.getMyFriends(userInformation.getId()));
       model.addAttribute("userInformation", userInformation);
       model.addAttribute("username", userInformation.getName());
       model.addAttribute("autograph", userInformation.getAutograph());
       model.addAttribute("action", 7);
       userController.getUserCounts(model, userInformation.getId());
        return "book/init";
    }


    //图书详情
    @RequestMapping(value = "book_information/{bookId}")
    public String bookInformation(@PathVariable("bookId") long bookId, HttpServletRequest request, Model model) {
        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
        System.out.println("songyaru");
        BookEntity entity = searchBookService.findById(bookId);
        System.out.println(entity);
        if (null == entity) {
            model.addAttribute("result", "fail");
            entity = new BookEntity();
        } else {
            model.addAttribute("result", "success");
            entity.setTranslator(entity.getTranslator().replace("[\"", "").replace("\"]", "").replace("\"", ""));
            entity.setAuthor(entity.getAuthor().replace("[\"", "").replace("\"]", "").replace("\"", ""));
            entity.setSummary(entity.getSummary().replace("\n","<br/>"));
            entity.setCatalog(entity.getCatalog().replace("\n", "<br/>"));
            entity.setAuthorIntro(entity.getAuthorIntro().replace("\n","<br/>"));
        }
        model.addAttribute("entity", entity);
        model.addAttribute("myFriends", userController.getMyFriends(userInformation.getId()));
        model.addAttribute("userInformation", userInformation);
        model.addAttribute("username", userInformation.getName());
        model.addAttribute("autograph", userInformation.getAutograph());
        model.addAttribute("action", 7);
        userController.getUserCounts(model, userInformation.getId());

        return "book/book_information";
    }


}
