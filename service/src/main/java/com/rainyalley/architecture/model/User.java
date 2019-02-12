package com.rainyalley.architecture.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.rainyalley.architecture.Identical;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author bin.zhang
 */
@JsonDeserialize(builder = User.UserBuilder.class)
@Builder
@Data
public class User implements Identical {

    private static final long serialVersionUID = -1327607787471771423L;

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String password;

    @JsonPOJOBuilder(withPrefix = "")
    public static class UserBuilder {}
}
