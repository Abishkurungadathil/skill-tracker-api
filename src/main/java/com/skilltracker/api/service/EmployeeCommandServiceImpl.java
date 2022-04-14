package com.skilltracker.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skilltracker.api.common.SkillTrackerHelper;
import com.skilltracker.api.entity.Employee;
import com.skilltracker.api.exeption.InvalidSkillEntryException;
import com.skilltracker.api.exeption.InvalidSkillProfileUpdateAttemptExeption;
import com.skilltracker.api.model.EmployeeBean;
import com.skilltracker.api.repository.EmployeeCommandRepo;
import com.skilltracker.api.repository.EmployeeQueryRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeCommandServiceImpl implements EmployeeCommandService{
    Logger logger = LoggerFactory.getLogger(this.getClass());
@Autowired
SkillTrackerHelper skillTrackerHelper;
    @Autowired
    EmployeeCommandRepo employeeCommandRepo;

    @Autowired
    EmployeeQueryRepo  employeeQueryRepo;
    @Autowired
    ObjectMapper objectMapper;
    List<String> skills = new ArrayList<String>(List.of(" HTML-CSS-JAVASCRIPT","ANGULAR","REACT","HIBERNATE","GIT","DOCKER","AWS","SPOKEN","COMMUNICATION","APTITUDE"));


    public EmployeeBean addEmployee(EmployeeBean employee) throws Exception{
       Employee employe= employeeCommandRepo.addEmployee(skillTrackerHelper.populateEmployee(employee));
       return skillTrackerHelper.populateEmployeeBean(employe);
    }
    public EmployeeBean editEmployee(EmployeeBean employee)  throws Exception{
        Employee currentEmp=validateUpdate(employee);
       // Employee employe= employeeCommandRepo.editEmployee(currentEmp);
        Employee employe= employeeCommandRepo.editEmployee(currentEmp.getId(),currentEmp);
        return skillTrackerHelper.populateEmployeeBean(employe);
    }

    public String deleteEmployee(EmployeeBean employee)  throws Exception{
       return employeeCommandRepo.deleteEmployee(skillTrackerHelper.populateEmployee(employee));

    }
    public Employee validateUpdate(EmployeeBean employeeBean)throws Exception {
        Employee employee = new Employee();
        Employee currentEmp= new Employee();
        try {
            valiDateSkill( employeeBean);
            //Employee currentEmp1 = employeeQueryRepo.findEmployeeById("bee06046-8986-4d63-b3f0-0690a3d841ce");
            List<Employee>    employees=employeeCommandRepo.findAllEmployee();
            List<Employee>  currentEmps=employees.stream().
                    filter(x -> x.getId().
                            equalsIgnoreCase(employeeBean.getId())).
                    collect(Collectors.toList());

            currentEmp =currentEmps.get(0);
            SimpleDateFormat sdf6 = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
            Date date = sdf6.parse(currentEmp.getTimestamp());
            LocalDate updatedDate = LocalDate.ofInstant(
                    date.toInstant(), ZoneId.systemDefault());
            LocalDate current  =LocalDate.now();
            if(!updatedDate.plusDays(9).isAfter(current)){
                throw new InvalidSkillProfileUpdateAttemptExeption("Error code 4 ","Updating a profile before 10 days");
            }else{
                currentEmp.setSkill(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(employeeBean.getSkill()));
            }
        }  catch (ParseException e) {
            logger.error("Unable to marshal skill", e);
        }
        return currentEmp;
    }

    private void valiDateSkill(EmployeeBean employeeBean) throws Exception{
        LinkedHashMap<String, String> skill = objectMapper.convertValue(employeeBean.getSkill(), LinkedHashMap.class);
        int size=skill.size();
        Set<String> keys = skill.keySet();
        for (String key : keys) {
            if(!skills.contains(key)){
                throw new InvalidSkillEntryException("ErrorCode 3","Skill  is  Not Valid");
            }
            String skillLevel =skill.get(key);
            if(skillLevel.isBlank()){
                throw new InvalidSkillEntryException("ErrorCode 1","Skill Levelel is Blank");
            }
            int number = Integer.parseInt(skillLevel);

            if(number >20  ){
                throw new InvalidSkillEntryException("ErrorCode 2", "Invalid Skill level ");
            }
        }
    }
}
