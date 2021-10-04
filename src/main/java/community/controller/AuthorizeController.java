package community.controller;

import community.dto.AccessTokenDTO;
import community.dto.GiteeUser;
import community.mapper.UserMapper;
import community.pojo.User;
import community.provider.GiteeProvider;
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
public class AuthorizeController {

    @Autowired private GiteeProvider provider  = null;
    @Value("${Gitee.Grant_type}") private String Grant_type;
    @Value("${Gitee.Client_id}") private String Client_id;
    @Value("${Gitee.Redirect_uri}") private String Redirect_uri;
    @Value("${Gitee.Client_secret}") private String Client_secret;

    @Autowired
//    private UserService userService = null;
    private UserMapper userMapper = null;
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code")String code,
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
            userDB.setGmtCreate(System.currentTimeMillis());
            userDB.setGmtModify(System.currentTimeMillis());
            userMapper.insertUser(userDB);
            response.addCookie(new Cookie("token",token));
//            request.getSession().setAttribute("user",user);
            return "redirect:/";
        }else{
            return "redirect:/";
        }
    }
}
