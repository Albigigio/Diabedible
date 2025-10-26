package com.example.diabedible.di;

import com.example.diabedible.controller.LoginController;
import com.example.diabedible.repository.InMemorySymptomRepository;
import com.example.diabedible.repository.InMemoryUserRepository;
import com.example.diabedible.repository.SymptomRepository;
import com.example.diabedible.repository.UserRepository;
import com.example.diabedible.service.*;
import com.example.diabedible.utils.ViewManager;
import com.example.diabedible.repository.TherapyRepository;
import com.example.diabedible.repository.InMemoryTherapyRepository;

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

   private static final SymptomRepository symptomRepository = new InMemorySymptomRepository();
   private static final SymptomService symptomService = new SymptomService();
 
    private static final TherapyRepository therapyRepository = new InMemoryTherapyRepository();
    
    private static final TherapyService therapyService = new TherapyService(therapyRepository);


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

    public static SymptomService getSymptomService() {
    return symptomService;
}

    public static TherapyService getTherapyService() {
    return therapyService;
}

}
