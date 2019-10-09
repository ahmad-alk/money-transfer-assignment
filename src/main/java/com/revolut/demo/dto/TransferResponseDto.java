package com.revolut.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransferResponseDto implements Serializable {
    private boolean success;
    private CustomErrorMessage error;


    public static class CustomErrorMessage implements Serializable {
        String id = UUID.randomUUID().toString();
        String code;
        String description;

        public CustomErrorMessage(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public CustomErrorMessage(String description) {
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public CustomErrorMessage getError() {
        return error;
    }

    public void setError(CustomErrorMessage error) {
        this.error = error;
    }
}
