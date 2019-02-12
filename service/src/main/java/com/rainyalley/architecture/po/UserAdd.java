package com.rainyalley.architecture.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserAdd {

    @NotBlank
    private String name;

    @NotBlank
    private String password;

}
