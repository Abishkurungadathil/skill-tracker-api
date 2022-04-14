package com.skilltracker.api.exeption;

public class InvalidSkillEntryException extends SkillTrackerException {

    public InvalidSkillEntryException(String errorCode, String message){ super(errorCode,message);

    }
}

