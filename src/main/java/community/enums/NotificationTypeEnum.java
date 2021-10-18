package community.enums;

public enum NotificationTypeEnum {
    Reply_Question(1,"回复了问题"),
    Reply_Comment(2,"回复了评论");
    private int status;
    private String name;

    NotificationTypeEnum(int status, String name) {
        this.status = status;
        this.name = name;
    }
}
