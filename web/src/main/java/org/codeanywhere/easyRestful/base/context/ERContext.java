package org.codeanywhere.easyRestful.base.context;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.context.ApplicationContext;

public class ERContext {

    private static VelocityEngine     ve;
    private static String             actionPackage;
    private static ApplicationContext ac;
    private static String             resourcePath;
    private static int                urlMatchIndex;
    private static String             domainName;
    private static String             port;

    public static String getResourcePath() {
        if (resourcePath == null) {
            throw new RuntimeException("the resourcePath is null !");
        }
        return resourcePath;
    }

    public static void setUrlMatchIndex(int urlMatchIndex) {
        ERContext.urlMatchIndex = urlMatchIndex;
    }

    public static int getUrlMatchIndex() {
        return urlMatchIndex;
    }

    public static void setResourcePath(String resourcePath) {
        ERContext.resourcePath = resourcePath;
    }

    public static String getActionPackage() {
        if (actionPackage == null) {
            throw new RuntimeException("the servletPackage is null !");
        }
        return actionPackage;
    }

    public static void setActionPackage(String actionPackage) {
        ERContext.actionPackage = actionPackage;
    }

    public static void setVelocityEngine(VelocityEngine ve) {
        ERContext.ve = ve;
    }

    public static void setApplicationContext(ApplicationContext ac) {
        ERContext.ac = ac;
    }

    public static ApplicationContext getApplicationContext() {
        return ac;
    }

    public static VelocityEngine getVelocityEngine() {
        if (ve == null) {
            throw new RuntimeException("the VelocityEngine is not inited !");
        }
        return ve;
    }

    public static Object getBean(String name) {

        if (ac == null) {
            throw new RuntimeException("the ApplicationContext is not inited !");
        }

        return ac.getBean(name);
    }

    public static String getPort() {
        if (port == null) {
            throw new RuntimeException("the port is null !");
        }
        return port;
    }

    public static void setPort(String port) {
        ERContext.port = port;
    }

    public static String getDomainName() {
        if (domainName == null) {
            throw new RuntimeException("the domainName is null !");
        }
        return domainName;
    }

    public static void setDomainName(String domainName) {
        ERContext.domainName = domainName;
    }
}
