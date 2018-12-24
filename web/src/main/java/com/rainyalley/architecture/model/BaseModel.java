package com.rainyalley.architecture.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;

@Data
public abstract class BaseModel implements Serializable {

    protected Long id;

    protected Date createdTime;

    protected Date updatedTime;

    public BaseModel() {
        createdTime = new Date();
        updatedTime = new Date();
        id = new Random().nextLong();
    }
}
