package community.controller;

import com.github.pagehelper.PageInfo;
import community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController{

    @Autowired
    QuestionService questionService = null;

    @GetMapping("/")
    public String toIndex (@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                          Model model) {
        PageInfo pageInfo = questionService.getQuestionList(pageNum);
        model.addAttribute("pageInfo",pageInfo);
        return "index";
    }
}
