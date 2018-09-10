package com.rainyalley.architecture.po;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class UserPo {

    @ApiModelProperty(value = "姓名", required = true)
    private String name;

    @ApiModelProperty(value = "密码", required = true)
    private String password;

    @ApiModelProperty(value = "邮箱", required = false)
    private String email;

    @ApiModelProperty(value = "年龄", required = false)
    private int age;
}
