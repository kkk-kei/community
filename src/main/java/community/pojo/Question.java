package community.pojo;


import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("question")
public class Question {
    private Integer id;
    private String title;
    private String description;
    private Long gmtCreate;
    private Long gmtModify;
    private Integer creator;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
    private String tag;
    private String remark;
}
