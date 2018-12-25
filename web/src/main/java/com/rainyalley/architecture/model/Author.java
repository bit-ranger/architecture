package com.rainyalley.architecture.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class Author extends BaseModel {

    private String firstName;

    private String lastName;
}
