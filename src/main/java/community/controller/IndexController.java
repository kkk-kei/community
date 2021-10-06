package community.controller;

import com.github.pagehelper.PageInfo;
import community.dto.QuestionDTO;
import community.mapper.UserMapper;
import community.pojo.User;
import community.service.QuestionService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    UserMapper userMapper = null;
    @Autowired
    QuestionService questionService = null;

    @GetMapping("/")
    public String toIndex(HttpServletRequest request,
                          @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                          Model model){
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if("token".equals(cookie.getName())){
                User user = userMapper.findByToken(cookie.getValue());
                request.getSession().setAttribute("user",user);
                break;
            }
        }

        PageInfo pageInfo = questionService.getQuestionDTOList(pageNum);
        model.addAttribute("pageInfo",pageInfo);
        return "index";
    }
}
