package jp.co.axa.apidemo.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jp.co.axa.apidemo.dto.EmployeeRequest;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.EmployeeCustomException;
import jp.co.axa.apidemo.exceptions.ResourceNotFoundException;
import jp.co.axa.apidemo.services.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Controller class for managing employee-related CRUD operations
 */
@RestController
@RequestMapping("/api/v1")
@Api(tags = "Employee API")
@CrossOrigin(origins = "http://localhost:8080")
@EnableGlobalMethodSecurity(prePostEnabled = true)
@CacheConfig(cacheNames = {"employee"})
public class EmployeeController {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Endpoint to get a list of all employees
     */
    @ApiOperation(value = "Get all employees", notes = "Returns a list of all employees")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved employees list"),
            @ApiResponse(code = 404, message = "Employees not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GetMapping("/employees")
    @PreAuthorize("hasRole('ADMIN') or hasRole('READ') or hasRole('WRITE')")
    public ResponseEntity<List<Employee>> getEmployees() {
        try {
            List<Employee> employees = employeeService.getAllEmployees();
            if (employees.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception ex) {
            throw new EmployeeCustomException("Error occurred while getting all employees", ex);
        }
    }

    /**
     * Endpoint to get a specific employee by ID
     */
    @ApiOperation(value = "Get employee by ID", notes = "Get the details of a specific employee by their ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Employee details retrieved successfully", response = Employee.class),
            @ApiResponse(code = 403, message = "Access Denied. You do not have permission to access this resource."),
            @ApiResponse(code = 404, message = "Employee not found"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @GetMapping("/employees/{employeeId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('READ') or hasRole('WRITE')")
    @Cacheable(key = "#employeeId")
    public ResponseEntity<Employee> getEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        try {
            Employee employee = employeeService.getEmployeeById(employeeId);
            if (employee == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(employee, HttpStatus.OK);
        } catch (Exception ex) {
            throw new EmployeeCustomException("Error occurred while getting employee with ID: " + employeeId, ex);
        }
    }

    /**
     * Endpoint to add a new employee
     */
    @ApiOperation(value = "Add an employee", notes = "Add an employee to the system")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Employee created successfully"),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 403, message = "Access Denied. You do not have permission to access this resource."),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping("/employees")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WRITE')")
    public ResponseEntity<String> saveEmployee(@RequestBody @Valid EmployeeRequest employeeRequest) {
        try {
            employeeService.saveEmployee(employeeRequest);
            LOG.info("Employee Saved Successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body("Employee data has been created successfully");
        } catch (Exception ex) {
            throw new EmployeeCustomException("Error occurred while saving employee", ex);
        }
    }

    /**
     * Endpoint to delete an employee
     */
    @ApiOperation(value = "Delete an employee", notes = "Delete an employee from the system")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Employee deleted successfully"),
            @ApiResponse(code = 404, message = "Employee not found"),
            @ApiResponse(code = 403, message = "Access Denied. You do not have permission to access this resource."),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @DeleteMapping("/employees/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(key = "#employeeId")
    public ResponseEntity<String> deleteEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        try {
            employeeService.deleteEmployeeById(employeeId);
            LOG.info("Employee Deleted Successfully");
            return ResponseEntity.ok().body("Employee with ID : " + employeeId + " deleted with success!");
        } catch (Exception ex) {
            throw new EmployeeCustomException("Error occurred while deleting employee Id : " + employeeId, ex);
        }
    }

    /**
     * Endpoint to update an employee
     */
    @ApiOperation(value = "Update an employee", notes = "Update an existing employee in the system")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Employee updated successfully"),
            @ApiResponse(code = 404, message = "Employee not found"),
            @ApiResponse(code = 403, message = "Access Denied. You do not have permission to access this resource."),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PutMapping("/employees/{employeeId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WRITE')")
    @CachePut(key = "#employeeId")
    public ResponseEntity<Employee> updateEmployee(@RequestBody @Valid EmployeeRequest employeeRequest,
                                                   @PathVariable(name = "employeeId") Long employeeId) {

        try {
            Employee employee = employeeService.getEmployeeById(employeeId);
            if (employee == null) {
                throw new ResourceNotFoundException("Employee not found with id " + employeeId);
            }
            employee.setName(employeeRequest.getName());
            employee.setSalary(employeeRequest.getSalary());
            employee.setDepartment(employeeRequest.getDepartment());
            employeeService.updateEmployee(employee);
            return ResponseEntity.ok().body(employee);
        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new EmployeeCustomException("Error occurred while updating employee", ex);
        }
    }

}
