package com.example.diabedible.di;

import com.example.diabedible.controller.LoginController;
import com.example.diabedible.service.LoginService;
import com.example.diabedible.utils.ViewManager;

/**
 * A very small hand-rolled dependency injector for the application.
 * It provides singleton services and simple factory methods for controllers.
 */
public class AppInjector {

    // Singleton services
    private final LoginService loginService = new LoginService();

    public LoginService getLoginService() {
        return loginService;
    }

    // Controller factories
    public LoginController createLoginController(ViewManager viewManager) {
        return new LoginController(getLoginService(), viewManager);
    }
}
