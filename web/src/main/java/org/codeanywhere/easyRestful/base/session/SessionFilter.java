package org.codeanywhere.easyRestful.base.session;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codeanywhere.easyRestful.base.session.config.SessionConfig;
import org.codeanywhere.easyRestful.base.session.store.CookieSessionStore;
import org.codeanywhere.easyRestful.base.session.store.SessionStore;

/**
 * @author chenke
 * @date 2014-6-18 9:24:59
 */
public class SessionFilter implements Filter {
    private ServletContext            servletContext;
    private SessionConfig             config;
    private Map<String, SessionStore> storeMap = new HashMap<String, SessionStore>();
    private SessionStoreHolder        holder;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.servletContext = filterConfig.getServletContext();
        String file = filterConfig.getInitParameter("configFile");
        this.config = SessionConfig.getConfig(file);
        initSessionStore();
        initSessionStoreHolder();
    }

    private void initSessionStoreHolder() {
        SessionStoreHolder holder = new SessionStoreHolder();
        holder.setDefaultStoreName(config.getDefaultStoreName());
        holder.setKeyStoreMap(config.getKeyStoreMap());
        holder.setStoreMap(storeMap);
        holder.setSessionInvalidTime(config.getSessionInvalidTime());
        this.holder = holder;
    }

    private void initSessionStore() {
        Map<String, String> map = config.getStoreClassMap();
        Set<String> keys = map.keySet();
        if (keys != null) {
            try {
                for (String key : keys) {
                    Object inst = Class.forName(map.get(key)).newInstance();
                    Method init = inst.getClass().getDeclaredMethod("init");
                    init.invoke(inst);
                    if (inst instanceof CookieSessionStore) {
                        Method cinit = inst.getClass().getDeclaredMethod("init",
                                new Class[] { ServletContext.class });
                        cinit.invoke(inst, new Object[] { this.servletContext });
                    }
                    storeMap.put(key, (SessionStore) inst);

                }
            } catch (Exception ex) {
                throw new RuntimeException("init session store error:", ex);
            }
        }
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        this.servletContext.setAttribute("request", request);
        this.servletContext.setAttribute("response", response);
        HttpServletResponse resp = new SessionResponseWrapper((HttpServletResponse) response);
        HttpServletRequest req = new SessionRequestWrapper((HttpServletRequest) request, resp,
                servletContext, holder);
        chain.doFilter(req, resp);
    }

    public void destroy() {
        //noting
    }

}
