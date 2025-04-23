package com.example;

import com.example.controller.GetAllEmployeeDataController;
import com.example.repo.EmployeeRepo;
import com.example.model.Employee;
import com.example.service.EmployeeDataAccessService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(GetAllEmployeeDataController.class)
public class ControllerTest {

    @MockitoBean
    EmployeeRepo employeeRepo;

    @MockitoBean
    EmployeeDataAccessService employeeDataAccessService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void GetAllEmployeesJsonTest() throws Exception {
        List<Employee> employees = new ArrayList<>();
        when(employeeRepo.findAll()).thenReturn(employees);
        mockMvc.perform(MockMvcRequestBuilders.get("/employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void GetAllEmployeesJsonTest1() throws Exception {
        List<Employee> employees = new ArrayList<>();
        when(employeeRepo.findAll()).thenReturn(employees);
        mockMvc.perform(MockMvcRequestBuilders.get("/employees").with(user("user").password("pass").roles("USER","ADMIN"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    public void GetAllEmployeesJsonTest2() throws Exception {
        List<Employee> employees = Arrays.asList(new Employee(1,"Adalyn", "Woodard", "CEO", 15000, 0), new Employee(2,"Westley", "Mack", "CEO", 12000, 1));
        when(employeeRepo.findAll()).thenReturn(employees);
        mockMvc.perform(MockMvcRequestBuilders.get("/employees").with(user("user").password("pass").roles("USER","ADMIN"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$[0].firstname").value("Adalyn"))
                .andExpect((ResultMatcher) jsonPath("$[1].lastname").value("Mack"));
                //.andExpect((ResultMatcher) jsonPath("$.lastname").value("Mack")); //when json is one object and not a list of objects
    }

    @Test
    public void EmployeesTableSortedTest() throws Exception {
        List<Employee> employees = Arrays.asList(new Employee(1,"Westley", "Woodard", "CEO", 15000, 0), new Employee(2,"Adalyn", "Mack", "CEO", 12000, 1));
        List<Employee> employees2 = Arrays.asList(new Employee(3,"Westley", "Woodard", "CEO", 15000, 0), new Employee(2,"Adalyn", "Mack", "CEO", 12000, 1));
        //employees.sort(Comparator.comparing(Employee::getFirstname));
        when(employeeRepo.findAll()).thenReturn(employees);
        mockMvc.perform(MockMvcRequestBuilders.get("/employees_table_sorted").with(user("user").password("pass").roles("USER","ADMIN"))
                .param("sorted_by", "by_firstname"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("display.html"))
                .andExpect(MockMvcResultMatchers.forwardedUrl(null))
                .andExpect(MockMvcResultMatchers.request().attribute("message5","emp_table_by_firstname_ok"))
                .andExpect(MockMvcResultMatchers.request().attribute("message4","ok"))
                .andExpect(MockMvcResultMatchers.request().attribute("employees", employees)); // ???

    }

}
