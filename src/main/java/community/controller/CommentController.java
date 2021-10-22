package community.controller;

import community.dto.CommentDTO;
import community.dto.CommentRenderDTO;
import community.dto.ResultDTO;
import community.enums.CommentTypeEnum;
import community.exception.CommonErrorCodeImp;
import community.model.Comment;
import community.model.User;
import community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    CommentService commentService = null;

    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request){

        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return ResultDTO.errorOf(CommonErrorCodeImp.No_Login);
        }
        if(commentDTO == null || StringUtils.isBlank(commentDTO.getContent())){
            return ResultDTO.errorOf(CommonErrorCodeImp.Content_is_Empty);
        }
        Comment comment = new Comment();
        comment.setType(commentDTO.getType());
        comment.setParentId(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setCreator(user.getId());
        comment.setLikeCount(0L);
        comment.setCommentCount(0);
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModify(System.currentTimeMillis());
        commentService.insert(comment);
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public ResultDTO<List<CommentRenderDTO>> comments(@PathVariable(name="id") Long id){
        List<CommentRenderDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.Comment);
        return ResultDTO.okOf(commentDTOS);
    }
}
