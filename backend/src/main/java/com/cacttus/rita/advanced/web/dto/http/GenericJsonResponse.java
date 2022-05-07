package com.cacttus.rita.advanced.web.dto.http;

public class GenericJsonResponse<T> {
    private boolean success;
    private T data;

    public GenericJsonResponse(boolean success, T data){
        this.success = success;
        this.data = data;
    }

    public GenericJsonResponse(){}

    public boolean isSuccess() { return success;
    }

    public void setSuccess(boolean success) { this.success = success;
    }

    public T getData() { return data;
    }

    public void setData(T data) { this.data = data;
    }
}
