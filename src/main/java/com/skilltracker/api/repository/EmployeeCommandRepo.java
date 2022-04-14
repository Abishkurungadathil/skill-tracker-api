package com.skilltracker.api.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.skilltracker.api.entity.Employee;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EmployeeCommandRepo {

    @Autowired
    private DynamoDBMapper mapper;


    public Employee addEmployee(Employee employee) {
        employee.setId(null);// auto generate
        employee.setTimestamp(new Date(System.currentTimeMillis()).toString());
        mapper.save(employee);
        return employee;
    }

    public String deleteEmployee(Employee employee) {
        mapper.delete(employee);
        return "employee removed !!";
    }

    public String deleteEmployeeById(String id) {
        mapper.delete(mapper.load(Employee.class, id));
        return "Customer Id : " + id + " Deleted!";
    }

    public Employee editEmployee(String id, Employee employee) {
        employee.setTimestamp(new Date(System.currentTimeMillis()).toString());
        mapper.save(employee,
                new DynamoDBSaveExpression()
                        .withExpectedEntry("id",
                                new ExpectedAttributeValue(
                                        new AttributeValue().withS(id)
                                )));
        return employee;
    }

    public Employee editEmployee(Employee employee) {
        employee.setTimestamp(new Date(System.currentTimeMillis()).toString());
        mapper.save(employee);
        return employee;
    }


    private DynamoDBSaveExpression buildExpression(Employee employee) {
        DynamoDBSaveExpression dynamoDBSaveExpression = new DynamoDBSaveExpression();
        Map<String, ExpectedAttributeValue> expectedMap = new HashMap<>();
        expectedMap.put("id", new ExpectedAttributeValue(new AttributeValue().withS(employee.getId())));
        //expectedMap.put("name", new ExpectedAttributeValue(new AttributeValue().withS(employee.getName())));
        dynamoDBSaveExpression.setExpected(expectedMap);
        return dynamoDBSaveExpression;
    }

    public List<Employee> findAllEmployee() {
        return mapper.scan(Employee.class, new DynamoDBScanExpression());

    }

}
