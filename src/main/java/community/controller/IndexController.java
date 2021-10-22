package community.controller;

import com.github.pagehelper.PageInfo;
import community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@Controller
public class IndexController{

    @Autowired
    QuestionService questionService = null;

    @GetMapping("/")
    public String toIndex (@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                          @RequestParam(name = "search",required = false) String search,
                          Model model) {
        String regexpSearch = null;
        if(search!=null){
            String[] searchs =search.split(",");
            regexpSearch = Arrays.stream(searchs).collect(Collectors.joining("|"));
        }
        model.addAttribute("search",regexpSearch);
        PageInfo pageInfo = questionService.getQuestionList(regexpSearch,pageNum);
        model.addAttribute("pageInfo",pageInfo);
        return "index";
    }
}
