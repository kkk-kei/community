package community.controller;

import community.dto.CommentRenderDTO;
import community.dto.QuestionDTO;
import community.enums.CommentTypeEnum;
import community.mapper.QuestionExtMapper;
import community.model.Question;
import community.service.CommentService;
import community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController{

    @Autowired
    QuestionService questionService = null;
    @Autowired
    QuestionExtMapper questionExtMapper = null;
    @Autowired
    CommentService commentService = null;
    
    @GetMapping("/question/{id}")
    public String toQuestion(@PathVariable(name = "id")Long id,
                             Model model){
//        问题
//        Question question = questionService.getQuestionById(id);
        QuestionDTO questionDTO = questionService.getQuestionById(id);
        Question updateQuestion = new Question();
        updateQuestion.setId(questionDTO.getId());
        updateQuestion.setViewCount(1);
        questionExtMapper.increaseView(updateQuestion);
//        评论
        List<CommentRenderDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.Question);
//        相关问题
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);

        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",comments);
        model.addAttribute("relatedQuestions",relatedQuestions);
        return "question";
    }
}
