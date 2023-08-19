package proselyteapi.com.tradetrek.model.exception;


import lombok.Getter;

public class ApiException extends RuntimeException{

    @Getter
    protected final String errorCode;

    public ApiException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
