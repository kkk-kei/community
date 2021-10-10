package community.exception;

public enum CommonErrorCodeImp implements CommonErrorCode {

    Question_Not_Found("你要找的问题不存在~");
    private String message;

    CommonErrorCodeImp(String message) {
        this.message = message;
    }

    @Override
    public String getMeesage() {
        return message;
    }
}
