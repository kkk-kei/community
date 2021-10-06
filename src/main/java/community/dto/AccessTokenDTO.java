package community.dto;

import lombok.Data;

@Data
public class AccessTokenDTO {
    private String code;
    private String grant_type;
    private String client_id;
    private String client_secret;
    private String redirect_uri;
}
