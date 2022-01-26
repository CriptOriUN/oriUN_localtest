package com.oriun.oriun.Models;
import javax.persistence.*;


@Entity
@Table(name = "roles")
public class RolesModel {
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private String ROL_NAME;

    public RolesModel() {
    }

    public RolesModel(String name) {
        this.ROL_NAME = name;
    }

    public String getName() {
        return ROL_NAME;
    }

    public void setName(String name) {
        this.ROL_NAME = name;
    }
}
