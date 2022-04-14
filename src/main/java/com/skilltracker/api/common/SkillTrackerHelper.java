package com.skilltracker.api.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skilltracker.api.entity.Employee;
import com.skilltracker.api.entity.EmployeeReadDataBean;
import com.skilltracker.api.exeption.InvalidSkillEntryException;
import com.skilltracker.api.model.EmployeeBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;


@Component
public class SkillTrackerHelper {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    List<String> skills = new ArrayList<String>(List.of(" HTML-CSS-JAVASCRIPT", "ANGULAR", "REACT", "HIBERNATE", "GIT", "DOCKER", "AWS", "SPOKEN", "COMMUNICATION", "APTITUDE"));
    @Autowired
    ObjectMapper objectMapper;


    public Employee populateEmployee(EmployeeBean employeeBean) throws Exception {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeBean, employee);

        try {

            LinkedHashMap<String, String> skill = objectMapper.convertValue(employeeBean.getSkill(), LinkedHashMap.class);
            int size = skill.size();
            Set<String> keys = skill.keySet();

            for (String key : keys) {
                if (!skills.contains(key)) {
                    throw new InvalidSkillEntryException("ErrorCode 3", "Skill  is  Not Valid");
                }
                String skillLevel = skill.get(key);
                if (skillLevel.isBlank()) {
                    throw new InvalidSkillEntryException("ErrorCode 1", "Skill Levelel is Blank");
                }
                int number = Integer.parseInt(skillLevel);

                if (number > 20) {
                    throw new InvalidSkillEntryException("ErrorCode 2", "Invalid Skill level ");
                }
            }
            employee.setSkill(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(employeeBean.getSkill()));

        } catch (JsonProcessingException e) {
            logger.error("Unable to marshal skill", e);
        }
        return employee;
    }


    public boolean filterSkill(EmployeeBean employee, String criteria) {
        LinkedHashMap<String, String> employeeSkillList = objectMapper.convertValue(employee.getSkill(), LinkedHashMap.class);
        Set<String> keys = employeeSkillList.keySet();
        if (!keys.contains(criteria)) {
            return false;
        } else if (Integer.parseInt(employeeSkillList.get(criteria)) > 10) {
            return true;
        } else {
            return false;
        }
     /*   DocumentContext docCtx = JsonPath.parse(employee.getSkill().toPrettyString());
        JsonPath jsonPath = JsonPath.compile("$..[?(@." + criteria + " > 1)]");
        JSONArray response = docCtx.read(jsonPath);
        return !response.isEmpty();*/
    }

    public EmployeeBean populateEmployeeBean(Employee employee) {
        EmployeeBean employeeBean = new EmployeeBean();
        BeanUtils.copyProperties(employee, employeeBean);

        try {
            employeeBean.setSkill(objectMapper.readTree(employee.getSkill()));

        } catch (JsonProcessingException e) {
            logger.error("Unable to marshal skill", e);


        }
        return employeeBean;
    }

    public EmployeeBean populateEmployeeBean(EmployeeReadDataBean employee) {
        EmployeeBean employeeBean = new EmployeeBean();
        BeanUtils.copyProperties(employee, employeeBean);
        try {
            employeeBean.setSkill(objectMapper.readTree(employee.getSkill()));

        } catch (JsonProcessingException e) {
            logger.error("Unable to marshal skill", e);

        }
        return employeeBean;
    }


}
