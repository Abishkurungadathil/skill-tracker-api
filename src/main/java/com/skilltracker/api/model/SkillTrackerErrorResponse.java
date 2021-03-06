package com.skilltracker.api.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SkillTrackerErrorResponse {

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String errorCode;
    private String errorMessage;
    private String description;
}
