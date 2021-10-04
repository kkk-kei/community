package community.controller;

import community.mapper.UserMapper;
import community.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    UserMapper userMapper = null;

    @GetMapping("/")
    public String toIndex(HttpServletRequest request){

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if("token".equals(cookie.getName())){
                User user = userMapper.findByToken(cookie.getValue());
                request.getSession().setAttribute("user",user);
                break;
            }
        }
        return "index";
    }
}
