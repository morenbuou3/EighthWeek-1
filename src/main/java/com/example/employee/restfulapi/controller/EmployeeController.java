package com.example.employee.restfulapi.controller;

import com.example.employee.restfulapi.entity.Employee;
import com.example.employee.restfulapi.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/employees")
public class EmployeeController {
    //在此处完成Employee API
    @Autowired
    EmployeeRepository employeeRepository;

    //在此处完成Company API
    @GetMapping
    public List<Employee> getEmployeeList() {
        return employeeRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public Employee getEmployee(@PathVariable(value = "id") Long id) {
        return employeeRepository.findOne(id);
    }

    @GetMapping(value = "/page/{pageNo}/pageSize/{pageSize}")
    public Page<Employee> getEmployeePage(@PathVariable(value = "pageNo") int pageNo,
                                          @PathVariable(value = "pageSize") int pageSize) {
        Pageable pageable = new PageRequest(pageNo, pageSize);
        return employeeRepository.findAll(pageable);
    }

    @GetMapping(value = "/male")
    public List<Employee> getMaleEmployeeList() {
        return employeeRepository.findAll((Specification<Employee>) (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();

            list.add(cb.equal(root.get("gender").as(String.class), "male"));

            Predicate[] p = new Predicate[list.size()];
            query.where(cb.and(list.toArray(p)));
            return query.getRestriction();
        });
    }

    @PostMapping
    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @PutMapping(value = "/{id}")
    public Employee updateEmployee(@PathVariable(value = "id") Long id,
                                 @RequestParam(value = "name") String name,
                                 @RequestParam(value = "age") Integer age,
                                 @RequestParam(value = "gender") String gender,
                                 @RequestParam(value = "salary") Integer salary) {
        Employee employee = employeeRepository.findOne(id);
        employee.setName(name);
        employee.setAge(age);
        employee.setGender(gender);
        employee.setSalary(salary);
        return employeeRepository.save(employee);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteEmployee(@PathVariable(value = "id") Long id) {
        employeeRepository.delete(id);
    }
}
