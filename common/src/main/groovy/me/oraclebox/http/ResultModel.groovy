package me.oraclebox.http

/**
 * Response model for all services http response.
 * 所有Http服務的返回格式
 * Created by oraclebox on 14/9/2016.
 */
class ResultModel<T> {
    int code;
    String message;
    String token;
    T data;

    public ResultModel() {}

    public ResultModel(ResultStatus status, String message, T data) {
        this.code = status.getCode();
        this.message = message != null ? message : status.getMessage();
        this.data = data;
    }

    public ResultModel(ResultStatus status, String message, T data, String token) {
        this.code = status.getCode();
        this.message = message != null ? message : status.getMessage();
        this.data = data;
        this.token = token;
    }


    public static ResultModel _200(String message, T data) {
        return new ResultModel(ResultStatus.SUCCESS, message, data);
    }

    public static ResultModel _200(String message, String token, T data) {
        return new ResultModel(ResultStatus.SUCCESS, message, data, token);
    }

    public static ResultModel _403(String message, T data) {
        return new ResultModel(ResultStatus.FORBIDDEN, message, data);
    }

    public static ResultModel _404(String message, T data) {
        return new ResultModel(ResultStatus.NOT_FOUND, message, data);
    }

    public static ResultModel _400(String message, T data) {
        return new ResultModel(ResultStatus.BAD_REQUEST, message, data);
    }

    public static ResultModel _500(String message, T data) {
        return new ResultModel(ResultStatus.INTERNAL_SERVER_ERROR, message, data);
    }

    public static ResultModel unauthorized(String message, T data) {
        return new ResultModel(ResultStatus.UNAUTHORIZED, message, data);
    }

}
