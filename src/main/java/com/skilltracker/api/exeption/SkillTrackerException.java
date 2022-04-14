package com.skilltracker.api.exeption;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class SkillTrackerException extends Exception {

    private final String errorCode;

    public SkillTrackerException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
