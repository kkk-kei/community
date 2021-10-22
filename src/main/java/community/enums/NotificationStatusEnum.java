package community.enums;

public enum NotificationStatusEnum {
    Unread(0),
    Read(1);
    private int status;

    NotificationStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
