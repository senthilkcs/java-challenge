package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.dto.EmployeeRequest;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.EmployeeCustomException;
import jp.co.axa.apidemo.exceptions.ResourceNotFoundException;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Sets the repository used to retrieve and store employee data.
     *
     * @param employeeRepository the EmployeeRepository object to use
     */
    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Retrieves all employees from the database.
     *
     * @return a List of Employee objects representing all employees in the database
     */
    public List<Employee> getAllEmployees() {
        try {
            return employeeRepository.findAll();
        } catch (Exception ex) {
            throw new EmployeeCustomException("Failed to retrieve employees from database", ex);
        }
    }

    /**
     * Retrieves a single employee from the database by their ID.
     *
     * @param employeeId the ID of the employee to retrieve
     * @return an Employee object representing the employee with the specified ID, or null if no such employee exists
     */
    public Employee getEmployeeById(Long employeeId) {
        try {
            Optional<Employee> optEmp = employeeRepository.findById(employeeId);
            return optEmp.orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + employeeId));
        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new EmployeeCustomException("Failed to retrieve employee with id " + employeeId + " from database", ex);
        }
    }

    /**
     * Saves a new employee to the database.
     *
     * @param employeeRequest the Employee object to save
     */
    public void saveEmployee(EmployeeRequest employeeRequest) {
        try {
            Employee employee = new Employee(
                    0L,
                    employeeRequest.getName(),
                    employeeRequest.getSalary(),
                    employeeRequest.getDepartment()
            );
            employeeRepository.save(employee);
        } catch (Exception ex) {
            throw new EmployeeCustomException("Failed to save employee to database", ex);
        }
    }

    /**
     * Deletes an employee from the database by their ID.
     *
     * @param employeeId the ID of the employee to delete
     */
    public void deleteEmployeeById(Long employeeId) {
        try {
            employeeRepository.deleteById(employeeId);
        } catch (Exception ex) {
            throw new EmployeeCustomException("Failed to delete employee with id " + employeeId + " from database", ex);
        }
    }

    /**
     * Updates an existing employee in the database.
     *
     * @param employee the Employee object to update
     */
    public void updateEmployee(Employee employee) {
        try {
            employeeRepository.save(employee);
        } catch (Exception ex) {
            throw new EmployeeCustomException("Failed to update employee with id " + employee.getId() + " in database", ex);
        }
    }
}