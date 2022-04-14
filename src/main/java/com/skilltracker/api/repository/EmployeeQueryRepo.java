package com.skilltracker.api.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skilltracker.api.entity.Employee;
import com.skilltracker.api.entity.EmployeeReadDataBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeQueryRepo {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DynamoDBMapper mapper;

    @Autowired
    ObjectMapper objectMapper;


    public List<EmployeeReadDataBean> findAllEmployee() {
        return mapper.scan(EmployeeReadDataBean.class, new DynamoDBScanExpression());

    }

    public EmployeeReadDataBean findEmployeeById(String id) {
        return mapper.load(EmployeeReadDataBean.class, id);
    }

    public PaginatedScanList<Employee> findEmployeeByName(String name) {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.addFilterCondition("name", new Condition().withComparisonOperator(ComparisonOperator.BEGINS_WITH)
                .withAttributeValueList(new AttributeValue().withS(name)));
        return mapper.scan(Employee.class, scanExpression);
    }

    public PaginatedScanList<Employee> findEmployeeByAssociateId(String associateid) {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.addFilterCondition("associateId", new Condition().withComparisonOperator(ComparisonOperator.EQ)
                .withAttributeValueList(new AttributeValue().withS(associateid)));
        return mapper.scan(Employee.class, scanExpression);

    }

    public PaginatedScanList<Employee> findEmployeeBySkill(String skillName) {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.addFilterCondition("skill", new Condition().withComparisonOperator(ComparisonOperator.GT)
                .withAttributeValueList(new AttributeValue().withS(skillName)));
        return mapper.scan(Employee.class, scanExpression);

    }
   

	/*
    private DynamoDBSaveExpression buildExpression(Employee employee) {
        DynamoDBSaveExpression dynamoDBSaveExpression = new DynamoDBSaveExpression();
        Map<String, ExpectedAttributeValue> expectedMap = new HashMap<>();
        expectedMap.put("id", new ExpectedAttributeValue(new AttributeValue().withS(employee.getId())));
        dynamoDBSaveExpression.setExpected(expectedMap);
        return dynamoDBSaveExpression;
    }
    */

    /*   @Scheduled(fixedRateString  = "${memcached.schedule.sec}" *//*, initialDelay=10*//*)
    public void scheduleFixedDelayTask() {
    	logger.debug(">> Scheduled cache reload : {}", Calendar.getInstance().getTime());
    	List<Employee> employee = findAllEmployee();
    	logger.debug("Flushing the cache");
    	memcachedClient.flush();
    	logger.debug("Adding {} of employees to cache", employee.size());
    	employee.parallelStream().forEach(x -> { memcachedClient.add(x.getId(),  Long.valueOf(timeout).intValue(), populateEmployee(x));
        });
    	logger.debug("<< Scheduled cache reload completed on  {} !", Calendar.getInstance().getTime());
    }
    */
  /*  private EmployeeBean populateEmployee(Employee employee) {
    	EmployeeBean employeeBean = new EmployeeBean();		
		BeanUtils.copyProperties(employee, employeeBean);
		
		try {
			employeeBean.setSkill(objectMapper.readTree(employee.getSkill()));
		} catch (JsonProcessingException e) {
			logger.error("Unable to marshal skill", e);
		}
		return employeeBean;
	}*/


}
