package jp.co.axa.apidemo.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Table(name = "EMPLOYEE")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel(value = "Employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "EMPLOYEE_NAME")
    @ApiModelProperty(value = "Employee name", example = "John")
    private String name;

    @Column(name = "EMPLOYEE_SALARY")
    @ApiModelProperty(value = "Employee salary", example = "50000")
    private Integer salary;

    @Column(name = "DEPARTMENT")
    @ApiModelProperty(value = "Employee department", example = "IT")
    private String department;

}
