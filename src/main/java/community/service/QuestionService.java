package community.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import community.exception.CommonErrorCodeImp;
import community.exception.CommonException;
import community.mapper.QuestionMapper;
import community.mapper.UserMapper;
import community.model.Question;
import community.model.QuestionExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    UserMapper userMapper = null;
    @Autowired
    QuestionMapper questionMapper = null;

    public PageInfo getQuestionList(Integer pageNum){

        PageHelper.startPage(pageNum,5);
        List<Question> questionList = questionMapper.selectByExample(new QuestionExample());
//        List<Question> questionList = questionMapper.getAll();

        for (Question question : questionList) {
//            question.setRemark(userMapper.findByID(question.getCreator()).getAvatarUrl());
            question.setUser(userMapper.selectByPrimaryKey(question.getCreator()));
//            System.out.println(question);
        }
        PageInfo<Question> pageInfo = new PageInfo<>(questionList);
        return pageInfo;
    }

    public PageInfo getQuestionList(Integer id,Integer pageNum){

        PageHelper.startPage(pageNum,5);
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(id);
        final List<Question> questionList = questionMapper.selectByExample(questionExample);

        for (Question question : questionList) {
//            question.setRemark(userMapper.findByID(question.getCreator()).getAvatarUrl());
            question.setUser(userMapper.selectByPrimaryKey(question.getCreator()));
//            System.out.println(question);
        }
        PageInfo<Question> pageInfo = new PageInfo<>(questionList);
        return pageInfo;
    }

    public Question getQuestionById(Integer id){
        Question question = questionMapper.selectByPrimaryKey(id);
        if(question == null) throw new CommonException(CommonErrorCodeImp.Question_Not_Found);
//        Question question = questionMapper.getQuestionById(id);
        question.setUser(userMapper.selectByPrimaryKey(question.getCreator()));
        return question;
    }

    public void insertOrUpdateQuestion(Question question) {
        if(questionMapper.selectByPrimaryKey(question.getId()) == null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModify(System.currentTimeMillis());
            questionMapper.insertSelective(question);
        }else{
            question.setGmtModify(System.currentTimeMillis());
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria()
                    .andIdEqualTo(question.getId());
            int updateResult = questionMapper.updateByExampleSelective(question, questionExample);
//            questionMapper.updateQuestion(question);
            if(updateResult != 1) throw new CommonException(CommonErrorCodeImp.Question_Not_Found);
        }
    }
}
