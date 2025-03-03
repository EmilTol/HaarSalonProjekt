package com.example.haarsalonprojekt.usecase;

import com.example.haarsalonprojekt.entity.Login;
import com.example.haarsalonprojekt.dbcontroller.LoginDBController;

public class LoginUseCase {
    private LoginDBController loginDBController;

    public LoginUseCase(LoginDBController loginDBController) {
        this.loginDBController = loginDBController;
    }

    public boolean execute(Login login) {
        return loginDBController.validateLogin(login);
    }
}
