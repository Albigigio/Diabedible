package com.example.diabedible.service;

import com.example.diabedible.controller.LoginController;
import com.example.diabedible.di.AppInjector;
import com.example.diabedible.utils.AppSession;
import com.example.diabedible.utils.Config;
import com.example.diabedible.utils.FXMLPaths;
import com.example.diabedible.utils.ViewManager;

/**
 * Default implementation of LogoutService.
 */
public class DefaultLogoutService implements LogoutService {

    private final AppInjector injector;

    public DefaultLogoutService(AppInjector injector) {
        this.injector = injector;
    }

    @Override
    public void logout(ViewManager viewManager) {
        // clear session
        AppSession.clear();
        // navigate to login
        LoginController loginController = injector.createLoginController(viewManager);
        viewManager.switchSceneWithController(
                FXMLPaths.LOGIN,
                loginController,
                Config.loginTitle(),
                Config.windowWidth(),
                Config.windowHeight(),
                Config.windowMaximized()
        );
    }
}
