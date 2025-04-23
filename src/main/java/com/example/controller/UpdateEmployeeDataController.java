package com.example.controller;

import com.example.model.Employee;
import com.example.repo.EmployeeRepo;
import com.example.service.EmployeeDataAccessService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UpdateEmployeeDataController {

    @Autowired
    EmployeeRepo employeeRepo;

    @Autowired
    EmployeeDataAccessService employeeDataAccessService;

    @PostMapping("/employee_reg")
    public String emp_reg(HttpServletRequest request, Model model) {
        String id = request.getParameter("id");
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String position = request.getParameter("position");
        String salary = request.getParameter("salary");
        String manager_id = request.getParameter("manager_id");
        String message4 = employeeDataAccessService.employeeDataCheck(id, firstname, lastname, position, salary, manager_id);
        String message5 = "emp_reg_bad";
        if (message4.equals("ok")) {
            int int_id = Integer.parseInt(id);
            int int_salary = Integer.parseInt(salary);
            int int_manager_id = Integer.parseInt(manager_id);
            Employee employee = new Employee(int_id, firstname, lastname, position, int_salary, int_manager_id);
            employeeRepo.save(employee);
            List<Employee> employees = new ArrayList<>();
            employees.add(employee);
            model.addAttribute("employees", employees);
            message5 = "emp_reg_ok";
        }
        model.addAttribute("message4", message4);
        model.addAttribute("message5", message5);
        return "display.html";
    }

    @PostMapping("/employee_del")
    public String emp_del(HttpServletRequest request, Model model) {
        String id = request.getParameter("id");
        String message4 = "bad";
        String message5 = employeeDataAccessService.emp_del_check(id);
        if (!message5.equals("emp_del_ok")) {
            model.addAttribute("message4", message4);
            model.addAttribute("message5", message5);
            return "display.html";
        }
        int int_id = Integer.parseInt(id);
        List<Employee> employees = new ArrayList<>();
        employees.add(employeeRepo.findById(int_id).orElse(new Employee(0, "delete", "delete", "delete", 0, 0)));
        employeeRepo.deleteById(int_id);
        message4 = "ok";
        model.addAttribute("employees", employees);
        model.addAttribute("message4", message4);
        model.addAttribute("message5", message5);
        return "display.html";
    }
}
