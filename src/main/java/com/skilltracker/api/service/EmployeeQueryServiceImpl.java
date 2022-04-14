package com.skilltracker.api.service;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.skilltracker.api.common.SkillTrackerHelper;
import com.skilltracker.api.entity.Employee;
import com.skilltracker.api.entity.EmployeeReadDataBean;
import com.skilltracker.api.exeption.InvalidSkillEntryException;
import com.skilltracker.api.model.EmployeeBean;
import com.skilltracker.api.repository.EmployeeQueryRepo;
import net.minidev.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeQueryServiceImpl implements EmployeeQueryService {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    EmployeeQueryRepo employeeQueryRepo;
    @Autowired
    SkillTrackerHelper skillTrackerHelper;

    public List<EmployeeBean> findAllEmployee() {
        List<EmployeeReadDataBean> employee = employeeQueryRepo.findAllEmployee();
        List<EmployeeBean> employeeBeans = new ArrayList<EmployeeBean>();
        logger.debug("Adding {} of employees to cache", employee.size());
        employee.parallelStream().forEach(x -> {
            employeeBeans.add(skillTrackerHelper.populateEmployeeBean((x)));
        });
        logger.debug("<< Scheduled cache reload completed on  {} !", Calendar.getInstance().getTime());
        return employeeBeans;
    }

    public EmployeeBean findEmployeeById(String id) {
        return skillTrackerHelper.populateEmployeeBean((employeeQueryRepo.findEmployeeById(id)));
    }

    public List<EmployeeBean> filterEmployeeSkill(List<EmployeeBean> employees, String criteria) {
        return employees.stream().filter(x -> skillTrackerHelper.filterSkill(x, criteria))
                .collect(Collectors.toList());
    }


}



