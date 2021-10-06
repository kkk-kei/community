package community.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import community.mapper.QuestionMapper;
import community.mapper.UserMapper;
import community.pojo.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class TestController {

    @Autowired
    QuestionMapper questionMapper = null;

    @Autowired
    UserMapper userMapper = null;

    @RequestMapping("/test")
    @ResponseBody
    public String test(){
        PageHelper.startPage(1,5);
        List<Question> questionList = questionMapper.getAll();

        for (Question question : questionList) {
            question.setRemark(userMapper.findByID(question.getCreator()).getAvatarUrl());
        }
        System.out.println(questionList);

        PageInfo<Question> pageInfo = new PageInfo<>(questionList);
        System.out.println(pageInfo);
        return "ok";
    }
}
