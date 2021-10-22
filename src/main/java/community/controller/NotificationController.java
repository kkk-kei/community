package community.controller;

import community.dto.ResultDTO;
import community.enums.NotificationTypeEnum;
import community.exception.CommonErrorCodeImp;
import community.mapper.NotificationMapper;
import community.model.Notification;
import community.model.User;
import community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {

    @Autowired
    NotificationService notificationService = null;

    @GetMapping("/notification/{id}")
    public String toNotification(HttpServletRequest request,
                                 @PathVariable(name = "id")Long id){

        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return "redirect:/";
        }
        Notification notification = notificationService.read(id, user);
        if(notification.getType()== NotificationTypeEnum.Reply_Question.getType()||
        notification.getType()==NotificationTypeEnum.Reply_Comment.getType())
        return "redirect:/question/"+notification.getOuterid();

        return "redirect:/";
    }
}
