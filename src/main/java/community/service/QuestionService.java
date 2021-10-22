package community.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import community.dto.QuestionDTO;
import community.dto.QuestionPageDTO;
import community.exception.CommonErrorCodeImp;
import community.exception.CommonException;
import community.mapper.QuestionExtMapper;
import community.mapper.QuestionMapper;
import community.mapper.UserMapper;
import community.model.Question;
import community.model.QuestionExample;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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

    public QuestionPageDTO getQuestionList(String regexpSearch, Integer pageNum){
        PageHelper.startPage(pageNum,5);
        List<Question> questionList = questionExtMapper.selectBySearch(regexpSearch);
        PageInfo<Question> pageInfo = new PageInfo<>(questionList);
        QuestionPageDTO questionPageDTO = new QuestionPageDTO();
        BeanUtils.copyProperties(pageInfo,questionPageDTO);
        List<QuestionDTO> list = new ArrayList<>();
        for (Question question : questionList) {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(userMapper.selectByPrimaryKey(question.getCreator()));
            list.add(questionDTO);
        }
        questionPageDTO.setList(list);
        return questionPageDTO;
    }

    public QuestionPageDTO getQuestionList(Long id,Integer pageNum){

        PageHelper.startPage(pageNum,5);
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(id);
        questionExample.setOrderByClause("gmt_create desc");
        List<Question> questionList = questionMapper.selectByExample(questionExample);
        PageInfo<Question> pageInfo = new PageInfo<>(questionList);
        QuestionPageDTO questionPageDTO = new QuestionPageDTO();
        BeanUtils.copyProperties(pageInfo,questionPageDTO);
        List<QuestionDTO> list = new ArrayList<>();
        for (Question question : questionList) {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(userMapper.selectByPrimaryKey(question.getCreator()));
            list.add(questionDTO);
        }
        questionPageDTO.setList(list);
        return questionPageDTO;
    }

    public QuestionDTO getQuestionById(Long id){
        Question question = questionMapper.selectByPrimaryKey(id);
        if(question == null) throw new CommonException(CommonErrorCodeImp.Question_Not_Found);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        questionDTO.setUser(userMapper.selectByPrimaryKey(question.getCreator()));
        return questionDTO;
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
            if(updateResult != 1) throw new CommonException(CommonErrorCodeImp.Question_Not_Found);
        }
    }

    public List<QuestionDTO> selectRelated(QuestionDTO relatedQuestion) {
        if(StringUtils.isBlank(relatedQuestion.getTag())){
            return new ArrayList<>();
        }
        String[] tags = relatedQuestion.getTag().split(",");
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(relatedQuestion.getId());
        question.setTag(regexpTag);
        List<Question> questions = questionExtMapper.selectRelated(question);
        List<QuestionDTO> list = new ArrayList<>();
        for (Question item : questions) {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(item,questionDTO);
            questionDTO.setUser(userMapper.selectByPrimaryKey(item.getCreator()));
            list.add(questionDTO);
        }
        return list;
    }
}
