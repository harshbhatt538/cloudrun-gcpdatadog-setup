package com.example.orderservice;

public class ResponseWrapper<T> {
    private T data;
    private String environment;

    public ResponseWrapper(T data, String environment) {
        this.data = data;
        this.environment = environment;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}

