package com.skilltracker.api.service;

import com.skilltracker.api.model.EmployeeBean;

public interface EmployeeCommandService {

    public EmployeeBean addEmployee(EmployeeBean employee)  throws Exception;

    public EmployeeBean editEmployee(EmployeeBean employee)  throws Exception;

    public String deleteEmployee(EmployeeBean employee) throws Exception;

}
