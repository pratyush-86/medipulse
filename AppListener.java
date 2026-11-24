package com.medipulse.servlet;

import com.medipulse.service.NotificationService;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/** Starts the background worker pool with the app and stops it on shutdown. */
@WebListener
public class AppListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent e) {
        NotificationService.getInstance().notifyAsync("MediPulse started");
    }

    @Override
    public void contextDestroyed(ServletContextEvent e) {
        NotificationService.getInstance().shutdown();
    }
}
