package community.controller;

import community.dto.AccessTokenDTO;
import community.dto.GiteeUser;
import community.provider.GiteeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {

    @Autowired private GiteeProvider provider  = null;
    @Value("${Gitee.Grant_type}") private String Grant_type;
    @Value("${Gitee.Client_id}") private String Client_id;
    @Value("${Gitee.Redirect_uri}") private String Redirect_uri;
    @Value("${Gitee.Client_secret}") private String Client_secret;
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code")String code){

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setGrant_type(Grant_type);
        accessTokenDTO.setClient_id(Client_id);
        accessTokenDTO.setRedirect_uri(Redirect_uri);
        accessTokenDTO.setClient_secret(Client_secret);

        String accessToken = provider.getAccessToken(accessTokenDTO);
//        System.out.println(accessToken);
        GiteeUser user = provider.getUser(accessToken);
        System.out.println(user.getName());
        return "index";
    }
}
