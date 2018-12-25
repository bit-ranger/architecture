package com.rainyalley.architecture.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class Book extends BaseModel {

    private String title;

    private String isbn;

    private int pageCount;

    private long authorId;
}