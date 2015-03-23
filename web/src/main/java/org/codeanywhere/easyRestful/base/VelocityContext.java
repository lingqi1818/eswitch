package org.codeanywhere.easyRestful.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VelocityContext implements RequestContext {

    private org.apache.velocity.VelocityContext vc;
    private HttpServletRequest                  request;
    private HttpServletResponse                 response;

    public VelocityContext(org.apache.velocity.VelocityContext vc, HttpServletRequest request,
                           HttpServletResponse response){
        this.vc = vc;
        this.request = request;
        this.response = response;
    }

    public void put(String key, Object obj) {
        vc.put(key, obj);
    }

    public HttpServletRequest getHttpServletRequest() {
        return request;
    }

    public HttpServletResponse getHttpServletResponse() {
        return response;
    }

}
