package com.rainyalley.architecture.entity;

import com.rainyalley.architecture.Identical;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * @author bin.zhang
 */
@Data
@Entity
@Table(name="USER")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Identical {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(updatable=false)
    @NotBlank
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String password;

}
