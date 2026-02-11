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
        if (INSTANCE == null) {
            throw new IllegalStateException("AppInjector non inizializzato");
        }
        return INSTANCE;
    }

    private final UserRepository userRepository = new InMemoryUserRepository();

    private static final SymptomRepository symptomRepository = new InMemorySymptomRepository();

    private static final TherapyRepository therapyRepository = new InMemoryTherapyRepository();


    private final AuthService authService = new LoginService(userRepository);

    private final LogoutService logoutService = new DefaultLogoutService(this);

    private static final SymptomService symptomService = new SymptomService();

    private static final TherapyService therapyService = new TherapyService(therapyRepository);

 
    private final PatientDirectoryService patientDirectoryService = new PatientDirectoryService(userRepository);

    private final ReadingService readingService = new ReadingService(patientDirectoryService);

    private final ReadingAlertService readingAlertService = new ReadingAlertService(readingService);

    private final IntakeService intakeService =  new IntakeService(therapyService);

    private final AdherenceAlertService adherenceAlertService = new AdherenceAlertService(intakeService, therapyService, patientDirectoryService);

    private final ReadingStatsService readingStatsService = new ReadingStatsService(readingService);

    private final AuditService auditService = new AuditService();

    private final MessageService messageService = new MessageService();

    private static PhysicalActivityService physicalActivityService;







   
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

    public ReadingService getReadingService() {
        return readingService;
    }

    public ReadingAlertService getReadingAlertService() {
        return readingAlertService;
    }

    public static PatientDirectoryService getPatientDirectoryServiceStatic() {
        return AppInjector.get().getPatientDirectoryService();
    }

    public static SymptomService getSymptomService() {
        return symptomService;
    }

    public static TherapyService getTherapyService() {
        return therapyService;
    }

    public static ReadingService getReadingServiceStatic() {
        return AppInjector.get().getReadingService();
    }

    public static ReadingAlertService getReadingAlertServiceStatic() {
        return AppInjector.get().getReadingAlertService();
    }

    public LoginController createLoginController(ViewManager viewManager) {
        return new LoginController(getAuthService(), viewManager);
    }

    public IntakeService getIntakeService() {
        return intakeService;
    }

    public static IntakeService getIntakeServiceStatic() {
        return AppInjector.get().getIntakeService();
    }

    public AdherenceAlertService getAdherenceAlertService() {
    return adherenceAlertService;
}

    public static AdherenceAlertService getAdherenceAlertServiceStatic() {
    return AppInjector.get().getAdherenceAlertService();
}

public ReadingStatsService getReadingStatsService() { 
    return readingStatsService; 
}

public static ReadingStatsService getReadingStatsServiceStatic() {
    return AppInjector.get().getReadingStatsService();
}

public AuditService getAuditService() {
    return auditService;
}

public static AuditService getAuditServiceStatic() {
    return AppInjector.get().getAuditService();
}


public MessageService getMessageService() {
    return messageService;
}

public static MessageService getMessageServiceStatic() {
    return AppInjector.get().getMessageService();
}

public static PhysicalActivityService getPhysicalActivityServiceStatic() {
    if (physicalActivityService == null) {
        physicalActivityService = new PhysicalActivityService();
    }
    return physicalActivityService;
}

}
