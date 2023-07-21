package com.larramendiProject.RegisterLoginSystem.dto;

import jakarta.validation.constraints.NotEmpty;

public class UpdateUserNameDTO {

    @NotEmpty(message = "Campo obrigatorio!")
    private String name;

    @NotEmpty(message = "Campo obrigatorio!")
    private String password;

    public UpdateUserNameDTO() {
    }

    public UpdateUserNameDTO(Long id, String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
