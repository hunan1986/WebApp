package com.example.controller;

import com.example.repo.EmployeeRepo;
import com.example.model.Employee;
import com.example.service.EmployeeDataAccessService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
public class GetAllEmployeeDataController {

    @Autowired
    EmployeeRepo employeeRepo;

    @Autowired
    EmployeeDataAccessService employeeDataAccessService;

    @GetMapping("/employees")
    @ResponseBody
    public ResponseEntity<List<Employee>> getEmployees() {
        List<Employee> employees = employeeRepo.findAll();
        if (!employees.isEmpty()) {
            return new ResponseEntity<>(employees, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(employees, HttpStatus.valueOf(404));
        }
    }

    @GetMapping("/employees_table_sorted")
    public ModelAndView employees_table_sorted(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("display.html");
        String sorted_by = request.getParameter("sorted_by");
        String message4 = "bad";
        String message5 = "emp_table_bad";
        List<Employee> employees = employeeRepo.findAll();
        if (!employees.isEmpty() && sorted_by.equals("by_id")) {
            message4 = "ok";
            message5 = "emp_table_ok";
            //todo get already sorted data from db
            employees.sort(Comparator.comparingInt(Employee::getId));
            mv.addObject("employees", employees);
        }
        if (!employees.isEmpty() && sorted_by.equals("by_salary")) {
            message4 = "ok";
            message5 = "emp_table_by_salary_ok";
            employees.sort(Comparator.comparingInt(Employee::getSalary));
            mv.addObject("employees", employees);
        }
        if (!employees.isEmpty() && sorted_by.equals("by_firstname")) {
            message4 = "ok";
            message5 = "emp_table_by_firstname_ok";
            employees.sort(Comparator.comparing(Employee::getFirstname));
            mv.addObject("employees", employees);
        }
        if (!employees.isEmpty() && sorted_by.equals("by_lastname")) {
            message4 = "ok";
            message5 = "emp_table_by_lastname_ok";
            employees.sort(Comparator.comparing(Employee::getLastname));
            mv.addObject("employees", employees);
        }
        mv.addObject("message4", message4);
        mv.addObject("message5", message5);
        return mv;
    }

    @GetMapping("/employees_xml")
    public void employees_xml_data(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        List<Employee> employees = employeeRepo.findAll();
        //employees.sort(Comparator.comparingInt(Employee::getId));
        int num_of_emp = employees.size() -1;
        if(num_of_emp >=1 ) {
            employees.sort(Comparator.comparingInt(Employee::getManager_id));
            int max_manager_id = employees.getLast().getManager_id();
            try {
                //todo generate xml using jackson
                File xmlDoc = new File("src\\main\\resources\\templates\\emp_table.xml");
                DocumentBuilderFactory dbFact = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuild = dbFact.newDocumentBuilder();
                Document doc = dBuild.newDocument();
                List<Element> elements = new ArrayList<>();
                for (Employee employee : employees) {
                    elements.add(doc.createElement(employee.getPosition()));
                    (elements.getLast()).setAttribute("id", String.valueOf(employee.getId()));
                    (elements.getLast()).setAttribute("name", employee.getFirstname() + " " + employee.getLastname());
                    (elements.getLast()).setAttribute("salary", String.valueOf(employee.getSalary()));
                }
                for (int i = 1; i <= max_manager_id; i++) {
                    for (int j = 0; j <= num_of_emp; j++) {
                        if (employees.get(j).getManager_id() == i) {
                            for (int k = 0; k <= num_of_emp; k++) {
                                if (employees.get(k).getId() == i) {
                                    elements.get(k).appendChild(elements.get(j));
                                }
                            }
                        }
                    }
                }
                doc.appendChild(elements.getFirst());
                DOMSource source = new DOMSource(doc);
                Result result = new StreamResult(xmlDoc);
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.transform(source, result);
                //System.out.println("Data is written to file");
                try {
                    File file = new File("src\\main\\resources\\templates\\emp_table.xml");
                    FileReader fileReader = new FileReader(file);
                    BufferedReader buffReader = new BufferedReader(fileReader);
                    String buffer = "";
                    while( (buffer = buffReader.readLine() ) != null)
                        out.println(buffer);
                    buffReader.close();
                }
                catch (Exception e) {
                    //System.out.println(e);
                    //System.out.println("Error reading xml");
                    out.println("<error>");
                    out.println("<Error_reading_xml/>");
                    out.println("</error>");
                }
            } catch (Exception e) {
                //System.out.println(e);
                //System.out.println("Error creating xml");
                out.println("<error>");
                out.println("<Error_creating_xml/>");
                out.println("</error>");
            }

        }
        else {
            out.println("<error>");
            out.println("<The_number_of_employees_is_not_enough_to_create_xml_data/>");
            out.println("</error>");
        }
    }

    @GetMapping("/employees_without_manager")
    public ModelAndView employees_without_manager()  {
            ModelAndView mv = new ModelAndView();
            mv.setViewName("display.html");
            List<Employee> employees = employeeRepo.findAll();
            int num_of_emp = employees.size() -1;
            String message4 = "bad";
            String message5 = "emp_without_man_no";
            if(num_of_emp >=1 ) {
                List<Employee> employees2 = new ArrayList<>();
                employees.sort(Comparator.comparingInt(Employee::getManager_id));
                int max_manager_id = employees.getLast().getManager_id();
                for (int i = 1; i <= max_manager_id; i++) {
                    for (int j = 0; j <= num_of_emp; j++) {
                        if (employees.get(j).getManager_id() == i) {
                            int exist = 0;
                            for (int k = 0; k <= num_of_emp; k++) {
                                if (employees.get(k).getId() == i) {
                                    exist = 1;
                                    break;
                                }
                            }
                            if (exist == 0)  {
                                employees2.add(employees.get(j));
                            }
                        }
                    }
                }
                if (!employees2.isEmpty()) {
                    message4 = "ok";
                    message5 = "emp_without_man_yes";
                    mv.addObject("employees", employees2);
                }
            }
            mv.addObject("message4", message4);
            mv.addObject("message5", message5);
            return mv;
    }

}
