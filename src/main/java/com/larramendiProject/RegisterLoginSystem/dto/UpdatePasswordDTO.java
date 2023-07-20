package com.larramendiProject.RegisterLoginSystem.dto;

public class UpdatePasswordDTO {
    private String oldPwd;
    private String newPwd;
    private String confirmNewPwd;

    public UpdatePasswordDTO() {
    }

    public UpdatePasswordDTO(String oldPwd, String newPwd, String confirmNewPwd) {
        this.oldPwd = oldPwd;
        this.newPwd = newPwd;
        this.confirmNewPwd = confirmNewPwd;
    }

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public String getConfirmNewPwd() {
        return confirmNewPwd;
    }

    public void setConfirmNewPwd(String confirmNewPwd) {
        this.confirmNewPwd = confirmNewPwd;
    }
}
