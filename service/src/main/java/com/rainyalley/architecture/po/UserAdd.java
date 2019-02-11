package com.rainyalley.architecture.po;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserAdd {

    @NotBlank
    private String name;

    @NotBlank
    private String password;

}
