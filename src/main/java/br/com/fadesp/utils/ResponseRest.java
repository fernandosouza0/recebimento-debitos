package br.com.fadesp.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseRest<T> {

    @JsonProperty("dados")
    private T data;

    @JsonProperty("erro")
    private String exception;

    @JsonProperty("propriedades")
    private Properties properties;

    public ResponseRest(T data, Properties properties, String exception) {
        this.data = data;
        this.exception = exception;
        this.properties = properties;
    }

    public ResponseRest() {
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

}
