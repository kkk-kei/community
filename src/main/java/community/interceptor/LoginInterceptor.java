package community.interceptor;

import community.mapper.UserMapper;
import community.model.User;
import community.model.UserExample;
import community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class LoginInterceptor implements HandlerInterceptor{

    @Autowired
    private UserMapper userMapper = null;
    @Autowired
    private NotificationService notificationService = null;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if("token".equals(cookie.getName())){
                UserExample example = new UserExample();
                example.createCriteria()
                        .andTokenEqualTo(cookie.getValue());
                List<User> users = userMapper.selectByExample(example);
//                User user = userMapper.findByToken(cookie.getValue());
                if(users.size() != 0){
                    User user = users.get(0);
                    request.getSession().setAttribute("user", user);
                    long unreadCount = notificationService.getUnreadCount(user.getId());
                    request.getSession().setAttribute("unreadCount",unreadCount);
                }
                break;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
