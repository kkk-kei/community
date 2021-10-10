package community.controller;

import com.github.pagehelper.PageInfo;
import community.model.User;
import community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController{

    @Autowired
    QuestionService questionService = null;

    @GetMapping("/profile/{action}")
    public String toProfile(HttpServletRequest request,
                            @PathVariable(name = "action") String action,
                            @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                            Model model){

        User user = (User) request.getSession().getAttribute("user");
        if(user == null) return "redirect:/";

        if ("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
        }else if ("center".equals(action)){
            model.addAttribute("section","center");
            model.addAttribute("sectionName","个人资料");
        }

        PageInfo pageInfo = questionService.getQuestionList(user.getId(), pageNum);
        model.addAttribute(pageInfo);
        return "profile";
    }
}
