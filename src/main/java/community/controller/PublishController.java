package community.controller;

import community.cache.TagCache;
import community.dto.QuestionDTO;
import community.dto.TagDTO;
import community.model.Question;
import community.model.User;
import community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class PublishController{

    @Autowired
    QuestionService questionService = null;

    @GetMapping("/publish")
    public String toPublish(Model model){
        List<TagDTO> tagDTOS = TagCache.get();
        model.addAttribute("tagDTOS",tagDTOS);
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(name = "title") String title,
            @RequestParam(name = "description") String description,
            @RequestParam(name="tag") String tag,
            @RequestParam(name="id",required = false) Long id,
            HttpServletRequest request,
            Model model){

        List<TagDTO> tagDTOS = TagCache.get();
        model.addAttribute("tagDTOS",tagDTOS);

        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        model.addAttribute("id",id);

        if (title==null||title==""){
            model.addAttribute("error","标题不能为空！");
            return "publish";
        }
        if (description==null||description==""){
            model.addAttribute("error","问题描述不能为空！");
            return "publish";
        }
        if (tag==null||tag==""){
            model.addAttribute("error","标签不能为空！");
            return "publish";
        }
        String invalid = TagCache.filterInvalid(tag);
        if(StringUtils.isNotBlank(invalid)){
            model.addAttribute("error","输入非法标签:"+invalid);
            return "publish";
        }
        User user = (User) request.getSession().getAttribute("user");

        Question question = new Question();
        question.setId(id);
        question.setTag(tag);
        question.setTitle(title);
        question.setCreator(user.getId());
        question.setDescription(description);
//        更新或者删除
//        从页面点进来时，问题已经存在了，此时在页面获取问题的id传到后端，
//        点击发布时，如果用id查询不到问题，则新增，如果查到，就修改
        questionService.insertOrUpdateQuestion(question);
        return "redirect:/";
    }

    @GetMapping("/publish/{id}")
    public String updateQuestion(@PathVariable(name = "id") Long id,
                                 Model model){
        List<TagDTO> tagDTOS = TagCache.get();
        model.addAttribute("tagDTOS",tagDTOS);

//        可以只找问题，不加user
//        Question question = questionService.getQuestionById(id);
        QuestionDTO questionDTO = questionService.getQuestionById(id);
        model.addAttribute("title",questionDTO.getTitle());
        model.addAttribute("description",questionDTO.getDescription());
        model.addAttribute("tag",questionDTO.getTag());
        model.addAttribute("id",questionDTO.getId());

        return "publish";
    }
}
