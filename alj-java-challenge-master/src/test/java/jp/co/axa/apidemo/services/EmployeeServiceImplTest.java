package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.dto.EmployeeRequest;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {EmployeeServiceImplTest.class})
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllEmployees() {

        List<Employee> employees = Arrays.asList(
                new Employee(1L, "user1", 10000, "IT"),
                new Employee(2L, "Jane", 20000, "HR"));

        when(employeeRepository.findAll()).thenReturn(employees);
        List<Employee> result = employeeService.getAllEmployees();
        assertEquals(employees, result);
    }

    @Test
    void testGetEmployeeById() {

        Employee employee = new Employee(1L, "user1", 10000, "IT");
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        Employee result = employeeService.getEmployeeById(1L);
        assertEquals(employee, result);
    }

    @Test
    void testSaveEmployee() {

        EmployeeRequest employeeRequest = new EmployeeRequest("user1", 10000, "IT");

        employeeService.saveEmployee(employeeRequest);
        Employee employee = new Employee(
                0L,
                employeeRequest.getName(),
                employeeRequest.getSalary(),
                employeeRequest.getDepartment()
        );
        verify(employeeRepository).save(employee);
    }

    @Test
    void testDeleteEmployeeById() {

        employeeService.deleteEmployeeById(1L);
        verify(employeeRepository).deleteById(1L);
    }

    @Test
    void testUpdateEmployee() {

        Employee employee = new Employee(1L, "user1", 10000, "IT");
        employeeService.updateEmployee(employee);
        verify(employeeRepository).save(employee);
    }
}

