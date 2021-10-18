package community.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import community.exception.CommonErrorCodeImp;
import community.exception.CommonException;
import community.mapper.QuestionExtMapper;
import community.mapper.QuestionMapper;
import community.mapper.UserMapper;
import community.model.Question;
import community.model.QuestionExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired
    UserMapper userMapper = null;
    @Autowired
    QuestionMapper questionMapper = null;
    @Autowired
    QuestionExtMapper questionExtMapper = null;

    public PageInfo getQuestionList(Integer pageNum){

        PageHelper.startPage(pageNum,5);
        QuestionExample questionExample = new QuestionExample();
        questionExample.setOrderByClause("gmt_create desc");
        List<Question> questionList = questionMapper.selectByExample(questionExample);
//        List<Question> questionList = questionMapper.getAll();

        for (Question question : questionList) {
//            question.setRemark(userMapper.findByID(question.getCreator()).getAvatarUrl());
            question.setUser(userMapper.selectByPrimaryKey(question.getCreator()));
//            System.out.println(question);
        }
        PageInfo<Question> pageInfo = new PageInfo<>(questionList);
        return pageInfo;
    }

    public PageInfo getQuestionList(Long id,Integer pageNum){

        PageHelper.startPage(pageNum,5);
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(id);
        questionExample.setOrderByClause("gmt_create desc");
        final List<Question> questionList = questionMapper.selectByExample(questionExample);

        for (Question question : questionList) {
//            question.setRemark(userMapper.findByID(question.getCreator()).getAvatarUrl());
            question.setUser(userMapper.selectByPrimaryKey(question.getCreator()));
//            System.out.println(question);
        }
        PageInfo<Question> pageInfo = new PageInfo<>(questionList);
        return pageInfo;
    }

    public Question getQuestionById(Long id){
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
            question.setCommentCount(0);
            question.setLikeCount(0);
            question.setViewCount(0);
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

    public List<Question> selectRelated(Question relatedQuestion) {
        if(StringUtils.isBlank(relatedQuestion.getTag())){
            return new ArrayList<>();
        }
        String[] tags = relatedQuestion.getTag().split(",");
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(relatedQuestion.getId());
        question.setTag(regexpTag);
        List<Question> questions = questionExtMapper.selectRelated(question);
        for (Question question1 : questions) {
            question1.setUser(userMapper.selectByPrimaryKey(question1.getCreator()));
        }
        return questions;
    }
}
