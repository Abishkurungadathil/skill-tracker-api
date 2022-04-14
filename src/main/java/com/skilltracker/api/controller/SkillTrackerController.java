package com.skilltracker.api.controller;


import com.skilltracker.api.model.EmployeeBean;
import com.skilltracker.api.service.EmployeeCommandService;
import com.skilltracker.api.service.EmployeeQueryService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(path = "/")
public class SkillTrackerController {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	EmployeeCommandService employeeCommandService;
	
	@Autowired
	EmployeeQueryService employeeQueryService;


	@ApiOperation(value = "search  Employees")
	@GetMapping(path = "admin/{criteria}/{criteriaValue}", produces = "application/json")
	//@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<EmployeeBean> searchEmploye(@PathVariable("criteria") String criteria, @PathVariable("criteriaValue") String criteriaValue) {
		logger.debug(">>> SEARCH EMPLOYEE");
		List<EmployeeBean> allEmployees = employeeQueryService.findAllEmployee();
		switch (criteria) {
		  case "name":
			 return allEmployees.stream().
		         filter(x -> x.getName().trim().toUpperCase().
		         contains(criteriaValue.trim().toUpperCase())).
		         collect(Collectors.toList());
			  
		  case "associateid":
			  return allEmployees.stream().
				         filter(x -> x.getAssociateId().
				         equalsIgnoreCase(criteriaValue.trim())).
				         collect(Collectors.toList());
		  case "skill":			  
			   return employeeQueryService.filterEmployeeSkill(allEmployees, criteriaValue.trim());
		  default:
			  return allEmployees;	
		  } 
		
	}

	@ApiOperation(value = "ADD  Employee")
	@PostMapping(path = "engineer/add-profile",consumes = "application/json",produces = "application/json")
	public ResponseEntity<Object> addEmployee(@Valid @RequestBody EmployeeBean employee)  throws Exception{
		logger.debug(">>> addEmployee()");
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(1).toUri();
		EmployeeBean emp = employeeCommandService.addEmployee(employee);
		return ResponseEntity.created(location).build();
	}

	@ApiOperation(value = "updates  Employee")
	@PutMapping(path = "engineer/update-profile", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> updateEmployee(@RequestBody EmployeeBean employee) throws Exception {
		return updateProfile(employee);


	}

	@ApiOperation(value = "updates employee Employee")
	@PutMapping(path = "engineer/update-profile/{userId}", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> updateEmployee(@PathVariable("userId") String userId, @RequestBody EmployeeBean employee)  throws Exception{
		employee.setId(userId);
		return updateProfile(employee);
	}


	private ResponseEntity<Object> updateProfile(EmployeeBean employee)  throws Exception{
		logger.debug(">>> updateEmployee()");
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(1).toUri();
		EmployeeBean emp = employeeCommandService.editEmployee(employee);;
		return ResponseEntity.created(location).build();
	}

/*

	


*/
	
	
}
