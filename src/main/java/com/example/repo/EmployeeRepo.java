package com.example.repo;

import com.example.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Integer> {

    @Query("SELECT u FROM Employee u where u.manager_id=:c")
    public List<Employee> findByManager__Id(@Param("c") int manager_id);

    public List<Employee> findBySalary(int salary);

    public List<Employee> findByFirstname(String firstname);

    public List<Employee> findByLastname(String lastname);

    public List<Employee> findByPosition(String position);

}
