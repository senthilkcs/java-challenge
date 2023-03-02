package jp.co.axa.apidemo.entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest {

    @ApiModelProperty(value = "Username for authentication", example = "test1", required = true)
    private String username;

    @ApiModelProperty(value = "Password for authentication", example = "test1", required = true)
    private String password;
}
