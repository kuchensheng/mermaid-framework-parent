package com.mermaid.framework.mvc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @Desription:
 * @Author:Hui
 * @CreateDate:2018/7/9 22:44
 */
@Data
public class APIResponse<T> {

    public static final String NOT_INITIALIZED = "not-initialized";

    public static final String SUCCESS = "success";

    public static final String FAIL = "fail";

    public static final String UNAUTHORIZED_SERVICE_INVOKER = "unauthorized-invoker";

    public static final String VALIDATION_FAIL = "validation-fail";

    public static final String BAD_PARAMETER = "bad-parameter";

    public static final String UNAUTHORIZED = "unauthorized";

    public static final String USER_NOT_LOGIN = "user-not-login";

    public static final String RPC_FAIL = "rpc-fail";
    protected String code;
    protected T data;
    protected String message;

    public APIResponse() {
        this(NOT_INITIALIZED,null,null);
    }

    public APIResponse(String code) {
        this(code,null,null);
    }

    public APIResponse(String code, T data) {
        this(code,data,null);
    }

    public APIResponse(String code, T data, String message) {
        this.code = code;
        this.data = data;
        if(!StringUtils.hasText(message)){
            this.message = ErrorTable.convertCode2LocaleMessage(code);
        }else {
            this.message = message;
        }
    }

    @JsonIgnore
    public boolean isSuccess() {
        return SUCCESS.equals(this.getCode());
    }

    public static <T> APIResponse<T> success(T data) {
        return new APIResponse<>(SUCCESS,data);
    }

    public static APIResponse success() {
        return success(null);
    }

    public static boolean isSuccess(APIResponse apiResponse) {
        if(apiResponse == null) {
            return false;
        }
        return apiResponse.isSuccess();
    }

    public static APIResponse fail(String message) {
        return new APIResponse(FAIL,null,message);
    }

    public static APIResponse fail(Throwable t) {
        return fail(t.getMessage());
    }

    public static APIResponse withCode(String code) {
        return new APIResponse(code);
    }

    public static <T> APIResponse<T> withCode(String code,T data ) {
        return new APIResponse<>(code,data);
    }

    public static APIResponse withCodeAndMessageArgs(String code, String... args) {
        APIResponse ret = withCode(code, null);
        try {
            ret.setMessage(MessageFormat.format(ret.getMessage(), args));
        } catch (Exception e) {
        }
        return ret;
    }

    public static APIResponse<BindingResult> fail(BindingResult bindingResult) {
        Map<String, String> errorMap = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            errorMap.put(error.getField(), error.getDefaultMessage());
        }
        return new APIResponse(VALIDATION_FAIL, errorMap);
    }

    public static APIResponse response(String code) {
        return new APIResponse(code);
    }

    public static <T> APIResponse<T> response(String code, T data) {
        return new APIResponse(code, data);
    }

}
