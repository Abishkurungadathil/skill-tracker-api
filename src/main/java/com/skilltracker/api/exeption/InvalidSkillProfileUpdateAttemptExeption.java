package com.skilltracker.api.exeption;

public class InvalidSkillProfileUpdateAttemptExeption extends SkillTrackerException {

    public InvalidSkillProfileUpdateAttemptExeption(String errorCode, String message){ super(errorCode,message);

    }
}
