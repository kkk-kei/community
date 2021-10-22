package community.enums;

public enum NotificationTypeEnum {
    Reply_Question(1,"回复了你的问题"),
    Reply_Comment(2,"回复了你的评论");
    private int type;
    private String name;

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    NotificationTypeEnum(int status, String name) {
        this.type = status;
        this.name = name;
    }
}
