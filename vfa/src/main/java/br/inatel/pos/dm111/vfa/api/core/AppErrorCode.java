package br.inatel.pos.dm111.vfa.api.core;

public enum AppErrorCode {

    INVALID_USER_CREDENTIALS("user.invalid.credentials", "Provided credentials are not valid!", 401),
    INTERNAL_DATABASE_COMMUNICATION_ERROR("internal.error", "Failure to communicate with repository.", 500);

    private final String code;
    private final String message;
    private final int status;

    AppErrorCode(String code, String message, int status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
