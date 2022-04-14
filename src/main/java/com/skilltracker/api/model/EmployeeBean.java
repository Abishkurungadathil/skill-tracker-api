package com.skilltracker.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import javax.validation.constraints.*;

@Getter
public class EmployeeBean {
    private String id;

    @NotEmpty(message = "Name may not be null")
    @Size(min = 5, max = 30, message = "validation error for Name length should be between 5 to 30")
    private String name;

    @NotEmpty(message = "associateid may not be null")
    @Pattern(regexp = "^CTS.*$", message = "ID must start with TX", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String associateId;

    @NotEmpty(message = "email may not be null")
    @Email(message = "Please provide a valid email address")
    @Pattern(regexp = ".+@.+\\..+", message = "Please provide a valid email address")
    private String email;

    @NotNull(message = "mobile may not be null")
    private Long mobile;

    @JsonProperty("skill")
    private JsonNode skill;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    private String timestamp;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAssociateId() {
        return associateId;
    }

    public void setAssociateId(String associateId) {
        this.associateId = associateId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getMobile() {
        return mobile;
    }

    public void setMobile(Long mobile) {
        this.mobile = mobile;
    }

    public JsonNode getSkill() {
        return skill;
    }

    public void setSkill(JsonNode skill) {
        this.skill = skill;
    }
}
