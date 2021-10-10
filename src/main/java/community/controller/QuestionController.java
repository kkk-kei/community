package community.controller;

import community.mapper.QuestionExtMapper;
import community.mapper.QuestionMapper;
import community.model.Question;
import community.model.QuestionExample;
import community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController{

    @Autowired
    QuestionService questionService = null;
    @Autowired
    QuestionExtMapper questionExtMapper = null;
    
    @GetMapping("/question/{id}")
    public String toQuestion(@PathVariable(name = "id")Integer id,
                             Model model){
        Question question = questionService.getQuestionById(id);

        Question updateQuestion = new Question();
        updateQuestion.setId(question.getId());
        updateQuestion.setViewCount(1);
        questionExtMapper.increaseView(updateQuestion);
//        question = questionService.getQuestionById(id);
        model.addAttribute("question",question);
        return "question";
    }
}
