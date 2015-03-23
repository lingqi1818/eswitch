package org.codeanywhere.easyRestful.base.main;

import java.util.Map;
import java.util.Set;

import org.codeanywhere.easyRestful.base.Start;
import org.codeanywhere.easyRestful.base.config.ERWebConfig;
import org.codeanywhere.easyRestful.base.context.ERContext;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

/**
 * 程序运行总入口
 * 
 * @author ke.chenk 2012-6-26 下午9:36:36
 * @mail lingqi1818@gmail.com
 */
public class EasyRestfulMain {

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("the cmd format:org.codeanywhere.easyRestful.base.main.EasyRestfulMain port domainName");
            return;
        }
        initPort(args[0]);
        initDomianName(args[1]);
        Start.start();
        Server server = createJettyServer(ERContext.getPort());
        initServer(server);
        useWebConfig(server, ERContext.getResourcePath());
        startServer(server);
    }

    private static Server initServer(Server server) {
        SelectChannelConnector sc = (SelectChannelConnector) server.getConnectors()[0];
        sc.setAcceptors(1);
        sc.setMaxIdleTime(100);
        int maxThreads = 100;
        int minThreads = maxThreads;
        QueuedThreadPool pool = new QueuedThreadPool(maxThreads);
        pool.setMinThreads(minThreads);
        server.setThreadPool(pool);
        return server;
    }

    private static void useWebConfig(Server server, String resourceBase) {
        HandlerCollection hc = new HandlerCollection();
        ServletHandler sh = new ServletHandler();
        ResourceHandler rh = new ResourceHandler();
        rh.setDirectoriesListed(false);
        rh.setWelcomeFiles(new String[] { "index.html" });
        rh.setResourceBase(resourceBase);
        Map<String, ServletHolder> servletConfig = ERWebConfig.servletConfig;
        Set<Map.Entry<String, ServletHolder>> set = servletConfig.entrySet();
        for (Map.Entry<String, ServletHolder> e : set) {
            sh.addServletWithMapping(e.getValue(), e.getKey());
        }
        // FilterMapping fm = new FilterMapping();
        // fm.setFilterName("permissionFilter");
        // fm.setPathSpec("/admin/*");
        // FilterHolder fh = new FilterHolder();
        // fh.setClassName(PermissionFilter.class.getName());
        // fh.setName("permissionFilter");
        // FilterMapping fm1 = new FilterMapping();
        // fm1.setFilterName("ssoFilter");
        // fm1.setPathSpec("/*");
        // FilterHolder fh1 = new FilterHolder();
        // fh1.setClassName(SSOFilter.class.getName());
        // fh1.setName("ssoFilter");
        // sh.addFilter(fh);
        // sh.addFilterMapping(fm);
        // sh.addFilter(fh1);
        // sh.addFilterMapping(fm1);

        hc.addHandler(sh);
        hc.addHandler(rh);
        server.setHandler(hc);
    }

    private static Server createJettyServer(String port) {
        return new Server(Integer.valueOf(port));
    }

    private static void startServer(Server server) throws Exception {
        server.start();
        server.join();

    }

    private static void initPort(String port) {
        ERContext.setPort(port);

    }

    private static void initDomianName(String domainName) {
        ERContext.setDomainName(domainName);
    }

}
