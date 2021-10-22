package community.dto;

import community.model.User;
import lombok.Data;

@Data
public class CommentRenderDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long creator;
    private Long gmtCreate;
    private Long gmtModify;
    private Long likeCount;
    private String content;
    private User user;
    private Integer commentCount;
}
