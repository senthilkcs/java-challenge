package jp.co.axa.apidemo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.axa.apidemo.dto.EmployeeRequest;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(classes = {EmployeeControllerTest.class})
class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testGetEmployees() throws Exception {
        List<Employee> employees = Arrays.asList(
                new Employee(1L, "user1", 10000, "IT"),
                new Employee(2L, "User2", 20000, "HR"));
        when(employeeService.getAllEmployees()).thenReturn(employees);

        ResponseEntity<List<Employee>> response = employeeController.getEmployees();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(employees);
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "READ"})
    void testGetEmployee() throws Exception {
        Employee employee = new Employee(1L, "user1", 10000, "IT");
        when(employeeService.getEmployeeById(1L)).thenReturn(employee);

        ResponseEntity<Employee> response = employeeController.getEmployee(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(employee);
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "WRITE"})
    void testSaveEmployee() throws Exception {
        EmployeeRequest employee = new EmployeeRequest("user1", 10000, "IT");

        ResponseEntity<?> response = employeeController.saveEmployee(employee);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testDeleteEmployee() throws Exception {
        ResponseEntity<String> response = employeeController.deleteEmployee(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Employee with ID : 1 deleted with success!");
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "WRITE"})
    void testUpdateEmployee() throws Exception {
        Employee employee = new Employee(1L, "user1", 10000, "IT");

        EmployeeRequest employeeRequest = new EmployeeRequest("user1", 20000, "IT");

        when(employeeService.getEmployeeById(1L)).thenReturn(employee);

        ResponseEntity<Employee> response = employeeController.updateEmployee(employeeRequest, 1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(employee);
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetEmployeesMockMvc() throws Exception {
        Employee employee = new Employee(1L, "user1", 10000, "IT");
        List<Employee> employees = Arrays.asList(employee);
        when(employeeService.getAllEmployees()).thenReturn(employees);

        this.mockMvc.perform(get("/api/v1/employees"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(".id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath(".name").value("user1"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetEmployeeByIdMockMvc() throws Exception {
        Employee employee = new Employee(1L, "user1", 10000, "IT");
        Mockito.when(employeeService.getEmployeeById(1L)).thenReturn(employee);

        this.mockMvc.perform(get("/api/v1/employees/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("name").value("user1"));
    }

    @Test
    @WithMockUser(roles = "WRITE")
    void testSaveEmployeeMockMvc() throws Exception {
        Employee employee = new Employee(1L, "user1", 10000, "IT");
        String json = new ObjectMapper().writeValueAsString(employee);

        this.mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteEmployeeByIdMockMvc() throws Exception {
        this.mockMvc.perform(delete("/api/v1/employees/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Employee with ID : 1 deleted with success!"));
    }

    @Test
    @WithMockUser(roles = "WRITE")
    void testUpdateEmployeeMockMvc() throws Exception {
        Employee employee = new Employee(1L, "user1", 10000, "IT");
        String json = new ObjectMapper().writeValueAsString(employee);
        Mockito.when(employeeService.getEmployeeById(1L)).thenReturn(employee);

        this.mockMvc.perform(put("/api/v1/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(".name").value("user1"));
    }
}
