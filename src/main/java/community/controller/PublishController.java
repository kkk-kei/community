package community.controller;

import community.mapper.QuestionMapper;
import community.mapper.UserMapper;
import community.pojo.Question;
import community.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    QuestionMapper questionMapper = null;
    @Autowired
    UserMapper userMapper = null;

    @GetMapping("/publish")
    public String toPublish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(name = "title") String title,
            @RequestParam(name = "description") String description,
            @RequestParam(name="tag") String tag,
            HttpServletRequest request,
            Model model){

        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);

        if (title==null||title==""){
            model.addAttribute("error","标题不能为空！");
            return "/publish";
        }
        if (description==null||description==""){
            model.addAttribute("error","问题描述不能为空！");
            return "/publish";
        }
        if (tag==null||tag==""){
            model.addAttribute("error","标签不能为空！");
            return "/publish";
        }
        User user = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if("token".equals(cookie.getName())){
                user = userMapper.findByToken(cookie.getValue());
                break;
            }
        }

        System.out.println(description+"---"+tag);
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModify(System.currentTimeMillis());
        questionMapper.insertQuestion(question);
        return "redirect:/";
    }
}
