package com.rainyalley.architecture.entity;

import com.rainyalley.architecture.Identical;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

/**
 * @author bin.zhang
 */
@Document
@Data
public class User implements Identical {

    @Id
    @NotBlank
    private Long id;

    @Indexed(unique = true)
    @NotBlank
    private String name;

    @NotBlank
    private String password;

}
