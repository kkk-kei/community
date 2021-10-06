package community.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import community.dto.QuestionDTO;
import community.mapper.QuestionMapper;
import community.mapper.UserMapper;
import community.pojo.Question;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    UserMapper userMapper = null;
    @Autowired
    QuestionMapper questionMapper = null;

    public PageInfo getQuestionDTOList(Integer pageNum){

        PageHelper.startPage(pageNum,5);
        List<Question> questionList = questionMapper.getAll();

        for (Question question : questionList) {
            question.setRemark(userMapper.findByID(question.getCreator()).getAvatarUrl());
        }
        PageInfo<Question> pageInfo = new PageInfo<>(questionList);
        System.out.println(pageInfo);
        return pageInfo;
    }
}
