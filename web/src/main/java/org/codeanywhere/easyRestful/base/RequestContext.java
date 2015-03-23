package org.codeanywhere.easyRestful.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 该接口的目的为方便迁移到任意的模板引擎
 * 
 * @author ke.chenk 2012-5-29 下午2:57:30
 * @mail lingqi1818@gmail.com
 */
public interface RequestContext {

    public void put(String key, Object obj);

    public HttpServletRequest getHttpServletRequest();

    public HttpServletResponse getHttpServletResponse();
}
