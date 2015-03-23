package org.codeanywhere.easyRestful.base.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.codeanywhere.easyRestful.base.Start;

public class ERStartListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        Start.start();
    }

    public void contextDestroyed(ServletContextEvent sce) {
        Start.stop();
    }

}
