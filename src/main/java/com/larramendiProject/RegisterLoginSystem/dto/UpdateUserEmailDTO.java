package com.larramendiProject.RegisterLoginSystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class UpdateUserEmailDTO {
    @NotEmpty(message = "Campo obrigatorio!")
    @Email(message = "Insira um formato de e-mail valido!")
    private String email;

    @NotEmpty(message = "Campo obrigatorio!")
    private String password;

    public UpdateUserEmailDTO() {
    }

    public UpdateUserEmailDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
