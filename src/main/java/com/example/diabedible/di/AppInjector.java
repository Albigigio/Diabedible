package com.example.diabedible.di;

import com.example.diabedible.controller.LoginController;
import com.example.diabedible.repository.InMemoryUserRepository;
import com.example.diabedible.repository.UserRepository;
import com.example.diabedible.service.AuthService;
import com.example.diabedible.service.LoginService;
import com.example.diabedible.utils.ViewManager;

/**
 * A very small hand-rolled dependency injector for the application.
 * It provides singleton services and simple factory methods for controllers.
 */
public class AppInjector {

    // Singleton repositories
    private final UserRepository userRepository = new InMemoryUserRepository();

    // Singleton services
    private final AuthService authService = new LoginService(userRepository);

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public AuthService getAuthService() {
        return authService;
    }

    // Controller factories
    public LoginController createLoginController(ViewManager viewManager) {
        return new LoginController(getAuthService(), viewManager);
    }
}
