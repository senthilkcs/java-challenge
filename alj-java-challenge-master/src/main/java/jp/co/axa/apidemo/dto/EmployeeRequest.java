package jp.co.axa.apidemo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequest {

    @NotBlank(message = "Name is required")
    @ApiModelProperty(value = "Employee name", example = "John")
    private String name;

    @NotNull(message = "Salary is required")
    @Min(value = 0, message = "Salary must be greater than or equal to 0")
    @ApiModelProperty(value = "Employee salary", example = "50000")
    private Integer salary;

    @NotBlank(message = "Department is required")
    @ApiModelProperty(value = "Employee department", example = "IT")
    private String department;
}
