package community.service;

import community.dto.CommentReturnDTO;
import community.enums.CommentTypeEnum;
import community.enums.NotificationStatusEnum;
import community.enums.NotificationTypeEnum;
import community.exception.CommonErrorCodeImp;
import community.exception.CommonException;
import community.mapper.*;
import community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private UserMapper userMapper = null;
    @Autowired
    private CommentMapper commentMapper = null;
    @Autowired
    private QuestionMapper questionMapper = null;
    @Autowired
    private QuestionExtMapper questionExtMapper = null;
    @Autowired
    private CommentExtMapper commentExtMapper = null;
    @Autowired
    private NotificationMapper notificationMapper = null;
    @Autowired
    private NotificationService notificationService = null;


    public void insert(Comment comment){
        if(comment.getParentId()==null||comment.getParentId()==0){
            throw new CommonException(CommonErrorCodeImp.Comment_Missing);
        }
        if(comment.getType()==null || !CommentTypeEnum.isExit(comment.getType())){
            throw new CommonException(CommonErrorCodeImp.Type_Error);
        }
        if(comment.getType() == CommentTypeEnum.Comment.getType()){
            //回复评论
            Comment commentDB = commentMapper.selectByPrimaryKey(comment.getParentId());
            if(commentDB==null) throw new CommonException(CommonErrorCodeImp.Comment_Not_Found);
            commentDB.setCommentCount(1);
            commentMapper.insert(comment);
            commentExtMapper.increaseComment(commentDB);
//          通知 (commentDB是父评论)
            createNotify(comment, commentDB.getCreator(), NotificationTypeEnum.Reply_Comment);
        }else{
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if(question==null) throw new CommonException(CommonErrorCodeImp.Question_Not_Found);
            question.setCommentCount(1);
            commentMapper.insert(comment);
            questionExtMapper.increaseComment(question);
//            通知
            createNotify(comment,question.getCreator(),NotificationTypeEnum.Reply_Question);

        }
    }

    private void createNotify(Comment comment, Long receiver, NotificationTypeEnum notificationTypeEnum){
//        发送者和接受者为同一人时，不生成通知
        Notification notification = new Notification();
        if(comment.getType()==CommentTypeEnum.Question.getType()){
            notification.setOuterid(comment.getParentId());
        }
        if(comment.getType()==CommentTypeEnum.Comment.getType()){
//            CommentExample commentExample = new CommentExample();
//            commentExample.createCriteria()
//                    .andIdEqualTo(comment.getParentId());
//            List<Comment> comments = commentMapper.selectByExample(commentExample);
            Comment newComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            notification.setOuterid(newComment.getParentId());
        }
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setNotifier(comment.getCreator());
        notification.setReceiver(receiver);
        notification.setType(notificationTypeEnum.getType());
        notification.setStatus(NotificationStatusEnum.Unread.getStatus());
        notificationMapper.insert(notification);
    }

    public List<CommentReturnDTO> listByTargetId(Long id, CommentTypeEnum type) {
//        找到问题对应的所有评论
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if(comments.size()==0){
            return new ArrayList<>();
        }
//        找到评论里所有的评论者
        Set<Long> commentCreaters = comments.stream().map(comment -> comment.getCreator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(commentCreaters);
//        找到评论者对应的用户所有信息
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
//        将评论者与用户匹配
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
//      将评论转化类型
        List<CommentReturnDTO> commentRetunDTOs = comments.stream().map(comment -> {
            CommentReturnDTO commentReturnDTO = new CommentReturnDTO();
            BeanUtils.copyProperties(comment,commentReturnDTO);
            commentReturnDTO.setUser(userMap.get(comment.getCreator()));
            return commentReturnDTO;
        }).collect(Collectors.toList());
        return commentRetunDTOs;
    }
}
