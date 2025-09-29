package com.example.diabedible.di;

import com.example.diabedible.controller.LoginController;
import com.example.diabedible.repository.InMemoryUserRepository;
import com.example.diabedible.repository.UserRepository;
import com.example.diabedible.service.*;
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
    private final LogoutService logoutService = new DefaultLogoutService(this);

    public AppInjector() {
        // Optionally seed demo data
        com.example.diabedible.utils.DemoDataProvider.seedUsers(userRepository);
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public LogoutService getLogoutService() {
        return logoutService;
    }

    // Controller factories
    public LoginController createLoginController(ViewManager viewManager) {
        return new LoginController(getAuthService(), viewManager);
    }
}
