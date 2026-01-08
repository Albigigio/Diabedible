package com.example.diabedible.di;

import com.example.diabedible.controller.LoginController;
import com.example.diabedible.repository.InMemorySymptomRepository;
import com.example.diabedible.repository.InMemoryTherapyRepository;
import com.example.diabedible.repository.InMemoryUserRepository;
import com.example.diabedible.repository.SymptomRepository;
import com.example.diabedible.repository.TherapyRepository;
import com.example.diabedible.repository.UserRepository;
import com.example.diabedible.service.*;
import com.example.diabedible.utils.ViewManager;


public class AppInjector {

    
    private static AppInjector INSTANCE;

    public static AppInjector get() {
        if (INSTANCE == null) throw new IllegalStateException("AppInjector non inizializzato");
        return INSTANCE;
    }

 
    private final UserRepository userRepository = new InMemoryUserRepository();

   
    private final PatientDirectoryService patientDirectoryService = new PatientDirectoryService(userRepository);

    
    private final AuthService authService = new LoginService(userRepository);
    private final LogoutService logoutService = new DefaultLogoutService(this);

    private static final SymptomRepository symptomRepository = new InMemorySymptomRepository();
    private static final SymptomService symptomService = new SymptomService();

    private static final TherapyRepository therapyRepository = new InMemoryTherapyRepository();
    private static final TherapyService therapyService = new TherapyService(therapyRepository);

    public AppInjector() {
        INSTANCE = this;

        com.example.diabedible.utils.DemoDataProvider.seedUsers(userRepository);

        seedDemoPatientAssignments();
    }

    private void seedDemoPatientAssignments() {
        patientDirectoryService.ensureRecordExists("IDMario");
        patientDirectoryService.assignReferenceDoctor("IDMario", "DRGiulia");
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

    public PatientDirectoryService getPatientDirectoryService() {
        return patientDirectoryService;
    }

    public static PatientDirectoryService getPatientDirectoryServiceStatic() {
        return AppInjector.get().getPatientDirectoryService();
    }

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
