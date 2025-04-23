package com.example.controller;

import com.example.repo.EmployeeRepo;
import com.example.model.Employee;
import com.example.service.EmployeeDataAccessService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class SearchEmployeeDataController {

    @Autowired
    EmployeeRepo employeeRepo;

    @Autowired
    EmployeeDataAccessService employeeDataAccessService;

    @PostMapping("/find_employee")
    public String add(HttpServletRequest request, Model model) {
        String field = request.getParameter("field");
        String find = request.getParameter("find");
        String message4 = "bad";
        String message5 = employeeDataAccessService.employeeDataSearchCheck(find,field);
        if (!message5.equals("find_ok")) {
            model.addAttribute("message4", message4);
            model.addAttribute("message5", message5);
            return "display.html";
        }
        List<Employee> employees = employeeDataAccessService.employeeSearch(find, field);
        if (employees.isEmpty()) {
            message5 = "not_found";
            model.addAttribute("message4", message4);
            model.addAttribute("message5", message5);
            return "display.html";
        }
        message4 = "ok";
        message5 = "is_found";
        model.addAttribute("employees", employees);
        model.addAttribute("message4", message4);
        model.addAttribute("message5", message5);
        return "display.html";
    }

}
