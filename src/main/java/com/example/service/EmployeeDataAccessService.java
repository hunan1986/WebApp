package com.example.service;

import com.example.repo.EmployeeRepo;
import com.example.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeDataAccessService {

    @Autowired
    EmployeeRepo employeeRepo;

    public String employeeDataCheck(String id, String firstname, String lastname, String position, String salary, String manager_id) {
        if(id.isEmpty() || firstname.isEmpty() || lastname.isEmpty() || position.isEmpty() || salary.isEmpty() || manager_id.isEmpty()) {
            return "bad";
        }
        try {
            int int_id = Integer.parseInt(id);
            int int_salary = Integer.parseInt(salary);
            int int_manager_id = Integer.parseInt(manager_id);
        } catch (Exception e) {
            return "bad";
        }
        if(!firstname.matches("[a-zA-Z]*")) {
            return "bad";
        }
        if(!lastname.matches("[a-zA-Z]*")) {
            return "bad";
        }
        if(!position.matches("[a-zA-Z]*")) {
            return "bad";
        }
        return "ok";
    }


    public String employeeDataSearchCheck(String find, String field) {
        if (find.isEmpty()) {
            return "find_empty";
        }
        if (field.equals("id") || field.equals("salary") || field.equals("manager_id")){
            try {
                int int_id = Integer.parseInt(find);
            } catch (Exception e) {
                return "not_integer";
            }
        }
        if (field.equals("firstname") || field.equals("lastname") || field.equals("position")) {
            if (!find.matches("[a-zA-Z]*")) {
                return "not_letters";
            }
        }
        return "find_ok";
    }

    public List<Employee> employeeSearch(String find, String field) {
        List<Employee> employees = new ArrayList<>();
        switch (field) {
            case "id" -> {
                int int_id = Integer.parseInt(find);
                employees.add(employeeRepo.findById(int_id).orElse(new Employee(0, "delete", "delete", "delete", 0,0 )));
                if(employees.getFirst().getId() == 0 ){
                    employees.clear();
                }
                return employees;
            }
            case "manager_id" -> {
                int int_manager_id = Integer.parseInt(find);
                employees.addAll(employeeRepo.findByManager__Id(int_manager_id));
                return employees;
            }
            case "salary" -> {
                int int_manager_id = Integer.parseInt(find);
                employees.addAll(employeeRepo.findBySalary(int_manager_id));
                return employees;
            }
            case "firstname" -> {
                employees.addAll(employeeRepo.findByFirstname(find));
                return employees;
            }
            case "lastname" -> {
                employees.addAll(employeeRepo.findByLastname(find));
                return employees;
            }
            case "position" -> {
                employees.addAll(employeeRepo.findByPosition(find));
                return employees;
            }
        }
        return employees;
    }

    public String emp_del_check(String id) {
        if(id.isEmpty()) {
            return "emp_del_empty";
        }
        try {
            int int_id = Integer.parseInt(id);
            Employee employee = employeeRepo.findById(int_id).orElse(new Employee(0, "delete", "delete", "delete", 0,0 ));
            if(employee.getId() == 0 ){
                return "emp_del_not_found";
            }
        } catch (Exception e) {
            return "emp_del_not_int";
        }
        return "emp_del_ok";
    }
}
