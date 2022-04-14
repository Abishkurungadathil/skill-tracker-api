package com.skilltracker.api.service;

import com.skilltracker.api.entity.Employee;
import com.skilltracker.api.model.EmployeeBean;

import java.util.List;

public interface EmployeeQueryService {

    public List<EmployeeBean> findAllEmployee();

    public EmployeeBean findEmployeeById(String id);

    public List<EmployeeBean> filterEmployeeSkill(List<EmployeeBean> employees, String criteria);


}
