package org.codeanywhere.easyRestful.base.config;

import java.util.HashMap;
import java.util.Map;

import org.codeanywhere.easyRestful.base.servlet.ERServlet;
import org.eclipse.jetty.servlet.ServletHolder;

public class ERWebConfig {

    public static Map<String, ServletHolder> servletConfig = new HashMap<String, ServletHolder>();
    public static String[]                   welcomeFiles  = { "index.html" };
    static {
        servletConfig.put("/*", new ServletHolder(ERServlet.class));
    }

}
