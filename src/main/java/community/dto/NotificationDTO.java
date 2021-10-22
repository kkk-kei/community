package community.dto;

import community.enums.NotificationTypeEnum;
import community.model.User;
import lombok.Data;

@Data
public class NotificationDTO {

    private Long id;
    private Long notifier;
    private String notifierName;
    private Long receiver;

    private Long outerid;
    private String outerTitle;

    private Long gmtCreate;
    private Integer status;
    private Integer type;
    private String typeName;
}
