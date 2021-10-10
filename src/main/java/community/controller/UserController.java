package community.controller;

import community.dto.AccessTokenDTO;
import community.dto.GiteeUser;
import community.model.User;
import community.provider.GiteeProvider;
import community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class UserController{

    @Autowired private GiteeProvider provider  = null;
    @Value("${Gitee.Grant_type}") private String Grant_type;
    @Value("${Gitee.Client_id}") private String Client_id;
    @Value("${Gitee.Redirect_uri}") private String Redirect_uri;
    @Value("${Gitee.Client_secret}") private String Client_secret;

    @Autowired
    private UserService userService = null;

    @GetMapping("/login")
    public String login(@RequestParam(name = "code")String code,
                           HttpServletRequest request,
                           HttpServletResponse response){

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setGrant_type(Grant_type);
        accessTokenDTO.setClient_id(Client_id);
        accessTokenDTO.setRedirect_uri(Redirect_uri);
        accessTokenDTO.setClient_secret(Client_secret);

        String accessToken = provider.getAccessToken(accessTokenDTO);
        GiteeUser user = provider.getUser(accessToken);

        if(user!=null){
            User userDB = new User();
            userDB.setAccountId(String.valueOf(user.getId()));
            userDB.setName(user.getName());
            String token = UUID.randomUUID().toString();
            userDB.setToken(token);
            userDB.setAvatarUrl(user.getAvatarUrl());
//          插入或者更新  accountId不变
            userService.insertOrUpdate(userDB);
            response.addCookie(new Cookie("token",token));
            return "redirect:/";
        }else{
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response){
        request.getSession().removeAttribute("user");

        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
