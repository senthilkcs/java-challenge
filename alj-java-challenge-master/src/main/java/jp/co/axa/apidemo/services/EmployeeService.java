package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.dto.EmployeeRequest;
import jp.co.axa.apidemo.entities.Employee;

import java.util.List;

public interface EmployeeService {

    /**
     * Retrieves all employees from the database.
     *
     * @return a List of Employee objects representing all employees in the database
     */
    List<Employee> getAllEmployees();

    /**
     * Retrieves a single employee from the database by their ID.
     *
     * @param employeeId the ID of the employee to retrieve
     * @return an Employee object representing the employee with the specified ID, or null if no such employee exists
     */
    Employee getEmployeeById(Long employeeId);

    /**
     * Saves a new employee to the database.
     *
     * @param employee the Employee object to save
     */
    void saveEmployee(EmployeeRequest employeeRequest);

    /**
     * Deletes an employee from the database by their ID.
     *
     * @param employeeId the ID of the employee to delete
     */
    void deleteEmployeeById(Long employeeId);

    /**
     * Updates an existing employee in the database.
     *
     * @param employee the Employee object to update
     */
    void updateEmployee(Employee employee);
}