package com.cleverua.bb.example;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

public class MultipartPostApplication extends UiApplication {
    private static final String NOT_IMPLEMENTED_MSG = "Not yet implemented";

    private static MultipartPostApplication application;

    public static void main(String[] args) {
        application = new MultipartPostApplication();
        application.pushScreen(new MultipartPostScreen());
        application.enterEventDispatcher();
    }

    public static MultipartPostApplication instance() {
        return application;
    }
    public static void exit() {
        System.exit(0);
    }

    public void showNotImplementedAlert() {
        invokeLater(new Runnable() {
            public void run() {
                Dialog.alert(NOT_IMPLEMENTED_MSG);
            }
        });
    }
}
