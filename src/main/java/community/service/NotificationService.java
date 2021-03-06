package community.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import community.dto.NotificationDTO;
import community.dto.NotificationPageDTO;
import community.enums.NotificationStatusEnum;
import community.enums.NotificationTypeEnum;
import community.exception.CommonErrorCodeImp;
import community.exception.CommonException;
import community.mapper.CommentMapper;
import community.mapper.NotificationMapper;
import community.mapper.QuestionMapper;
import community.mapper.UserMapper;
import community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {

    @Autowired
    NotificationMapper notificationMapper = null;
    @Autowired
    UserMapper userMapper = null;
    @Autowired
    QuestionMapper questionMapper = null;
    @Autowired
    CommentMapper commentMapper = null;

    public NotificationPageDTO getNotificationList(Long receiver, Integer pageNum){

        PageHelper.startPage(pageNum,5);
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(receiver);
        notificationExample.setOrderByClause("gmt_create desc");
        List<Notification> notifications = notificationMapper.selectByExample(notificationExample);
        PageInfo<Notification> pageInfo = new PageInfo<>(notifications);
        NotificationPageDTO notificationPageDTO = new NotificationPageDTO();
        BeanUtils.copyProperties(pageInfo,notificationPageDTO);
        ArrayList<NotificationDTO> notificationDTOS = new ArrayList<>();
        for (Notification notification : notifications) {
            Long notifier = notification.getNotifier();
            Long outerid = notification.getOuterid();
            NotificationDTO item = new NotificationDTO();
            BeanUtils.copyProperties(notification,item);
            item.setOuterid(outerid);
            item.setNotifierName(userMapper.selectByPrimaryKey(notifier).getName());
            item.setOuterTitle(questionMapper.selectByPrimaryKey(outerid).getTitle());
            item.setTypeName(NotificationTypeEnum.Reply_Question.getName());
            notificationDTOS.add(item);
        }
        notificationPageDTO.setList(notificationDTOS);
        return notificationPageDTO;
    }

    public Notification read(Long id, User user){
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if(notification == null){
            throw new CommonException(CommonErrorCodeImp.Notification_Not_Fund);
        }
        if(!Objects.equals(notification.getReceiver(),user.getId())){
            throw new CommonException(CommonErrorCodeImp.Notification_Read_Error);
        }
        notification.setStatus(NotificationStatusEnum.Read.getStatus());
        notificationMapper.updateByPrimaryKey(notification);
        return notification;
    }

    public long getUnreadCount(Long id){
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(id)
                .andStatusEqualTo(NotificationStatusEnum.Unread.getStatus());
        long unreadCount = notificationMapper.countByExample(notificationExample);
        return unreadCount;
    }
}
