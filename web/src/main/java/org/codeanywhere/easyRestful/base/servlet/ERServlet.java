package org.codeanywhere.easyRestful.base.servlet;

import java.io.BufferedWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.codeanywhere.easyRestful.base.VelocityContext;
import org.codeanywhere.easyRestful.base.annotation.Json;
import org.codeanywhere.easyRestful.base.annotation.Jsonp;
import org.codeanywhere.easyRestful.base.annotation.Request;
import org.codeanywhere.easyRestful.base.annotation.SpringBean;
import org.codeanywhere.easyRestful.base.constant.ERWebConstant;
import org.codeanywhere.easyRestful.base.context.ERContext;

import com.alibaba.fastjson.JSON;

/**
 * 网站mvc请求总入口
 * 
 * @author ke.chenk 2012-6-28 上午10:28:28
 * @mail lingqi1818@gmail.com
 */
public class ERServlet extends HttpServlet {

    private static Logger                   log              = Logger.getLogger(ERServlet.class);
    private static Map<String, Object>      actionMap        = new ConcurrentHashMap<String, Object>();
    private static Map<String, Method>      methodMap        = new ConcurrentHashMap<String, Method>();
    private static Map<String, List<Field>> fieldMap         = new ConcurrentHashMap<String, List<Field>>();
    private static Map<String, MethodType>  methodTypeMap    = new ConcurrentHashMap<String, MethodType>();
    private static Map<String, String>      jsonpMethodMap   = new ConcurrentHashMap<String, String>();
    /**
     * 
     */
    private static final long               serialVersionUID = 1L;

    //TODO 需要添加白名单功能
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            ExecuteAction exeAction = parseURI(request.getRequestURI());
            org.apache.velocity.VelocityContext vc = new org.apache.velocity.VelocityContext();
            Object actionObj = getAction(exeAction);
            setRequestContext(actionObj, exeAction, new VelocityContext(vc, request, response));
            Method method = getActionMethod(exeAction, actionObj);
            Object result = invokeMethod(method, actionObj, exeAction);
            BufferedWriter bw = new BufferedWriter(response.getWriter());
            if (methodTypeMap.get(exeAction.getMethodName()) == MethodType.JSON) {
                String s = JSON.toJSONString(result);
                bw.write(s);
                bw.flush();
            } else if (methodTypeMap.get(exeAction.getMethodName()) == MethodType.JSONP) {
                StringBuffer sbf = new StringBuffer();
                sbf.append(jsonpMethodMap.get(exeAction.getMethodName()));
                sbf.append("=(");
                sbf.append(JSON.toJSONString(result));
                sbf.append(");");
                bw.write(sbf.toString());
                bw.flush();
            } else {
                mergeTemplate(exeAction, vc, response);
            }
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }

    private void mergeTemplate(ExecuteAction exeAction, org.apache.velocity.VelocityContext vc,
                               HttpServletResponse response) throws Exception {
        Template t = getTemplate(exeAction);
        if (t != null) {
            t.merge(vc, response.getWriter());
        }
    }

    private Template getTemplate(ExecuteAction exeAction) {
        String template = exeAction.getTemplatePath() + ERWebConstant.FILE_PATH_SPLIT_SYMBOL + exeAction.getTemplate();
        try {
            return ERContext.getVelocityEngine().getTemplate(template);
        } catch (Exception e) {
            log.warn("can't find the template ->" + template);
            return null;
        }
    }

    private void setRequestContext(Object actionObj, ExecuteAction exeAction, VelocityContext velocityContext) {
        Object obj = actionMap.get(exeAction.getActionClzz());
        if (obj != null) {
            Class<?> clzz = obj.getClass();
            while (clzz != null) {
                List<Field> flist = fieldMap.get(clzz.getName());
                if (flist != null) {
                    for (Field f : flist) {
                        f.setAccessible(true);
                        try {
                            f.set(actionObj, velocityContext);
                        } catch (Exception e) {
                            log.error(e);
                        }
                    }
                }
                clzz = clzz.getSuperclass();
            }
        }
    }

    private Object invokeMethod(Method method, Object actionObj, ExecuteAction exeAction) {
        try {
            return method.invoke(actionObj, (Object[]) exeAction.getParams());
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }

    private Method getActionMethod(ExecuteAction exeAction, Object actionObj) {
        if (actionObj == null) {
            return null;
        }
        String methodName = exeAction.getMethodName();
        Method method = methodMap.get(methodName);
        if (method == null) {
            Class<?>[] typeArray;
            String[] params = exeAction.getParams();
            if (params == null) {
                typeArray = new Class<?>[] {};
            } else {
                typeArray = new Class<?>[params.length];
                for (int i = 0; i < params.length; i++) {
                    typeArray[i] = String.class;
                }
            }
            Class<?> clzz = actionObj.getClass();
            while (clzz != null) {
                try {
                    method = clzz.getDeclaredMethod(exeAction.getMethod(), typeArray);
                    if (method != null) {
                        methodMap.put(methodName, method);
                        break;
                    }
                    clzz = clzz.getSuperclass();
                } catch (Exception e) {
                    clzz = clzz.getSuperclass();
                    continue;
                }
            }
        }
        if (method == null)
            log.error("no method exists,class:" + exeAction.getActionClzz() + " method:" + exeAction.getMethod());
        else {
            if (methodTypeMap.get(methodName) == null) {
                if (method.getAnnotation(Json.class) != null) {
                    methodTypeMap.put(methodName, MethodType.JSON);
                } else if (method.getAnnotation(Jsonp.class) != null) {
                    methodTypeMap.put(methodName, MethodType.JSONP);
                    jsonpMethodMap.put(methodName, method.getAnnotation(Jsonp.class).callbackMethodName());
                } else {
                    methodTypeMap.put(methodName, MethodType.GENERAL);
                }
            }
        }
        return method;
    }

    private Object getAction(ExecuteAction parseAction) {
        Object action = actionMap.get(parseAction.actionClzz);
        if (action == null) {
            try {
                Class<?> clzz = ERServlet.class.getClassLoader().loadClass(parseAction.actionClzz);
                action = clzz.newInstance();
                setSpringBeansAndGetRequestContext(action, clzz);
                actionMap.put(parseAction.actionClzz, action);
            } catch (Exception e) {
                log.error(e);
                return null;
            }
        }
        return action;
    }

    private void setSpringBeansAndGetRequestContext(Object action, Class<?> clzz) {
        while (clzz != null) {
            Field[] fields = clzz.getDeclaredFields();
            if (fields != null) {
                try {
                    for (Field field : fields) {
                        if (field.getAnnotation(SpringBean.class) != null && ERContext.getApplicationContext() != null) {

                            field.setAccessible(true);
                            field.set(action, ERContext.getBean(field.getName()));

                        }
                        if (field.getAnnotation(Request.class) != null) {
                            if (fieldMap.get(clzz.getName()) == null) {
                                fieldMap.put(clzz.getName(), new ArrayList<Field>());
                            }
                            fieldMap.get(clzz.getName()).add(field);
                        }
                    }
                } catch (Exception e) {
                    log.error(e);
                    continue;
                }
            }
            clzz = clzz.getSuperclass();
        }
    }

    /**
     * <pre>
     * 分析4中URI类型 
     * a)/xxx/xxx/xxx/resource/list/start/end 列出所有的resource start:开始条数 end:结束条数
     * b)/xxx/xxx/xxx/resource/id/detail 列出指定ID的resource
     * c)/xxx/xxx/xxx/action/method/attr/a1/a2/a3/a4 执行某个action的method参数为attr后面的部分 
     * d)/xxx/xxx/xxx/action/默认执行action下的execute方法
     * </pre>
     * 
     * @param requestURI
     */
    private ExecuteAction parseURI(String requestURI) {
        requestURI = requestURI.substring(1);
        for (int i = 1; i < ERContext.getUrlMatchIndex(); i++) {
            int cIndex = requestURI.indexOf(ERWebConstant.URL_SPLIT_SYMBOL);
            requestURI = requestURI.substring(cIndex + 1);
        }
        ExecuteAction ea = new ExecuteAction();
        String requestURIArray[] = requestURI.split(ERWebConstant.URL_SPLIT_SYMBOL);
        int attrIndex = getAttrIndex(requestURIArray);
        String actionName = "";
        if (requestURIArray.length >= 3
                && ERWebConstant.RESTFUL_URL_DETAIL.equalsIgnoreCase(requestURIArray[requestURIArray.length - 1])
                && attrIndex == 0) {
            ea.setMethod(ERWebConstant.RESTFUL_URL_DETAIL);
            ea.setParams(new String[] { requestURIArray[requestURIArray.length - 2] });
            String clzzName = ERContext.getActionPackage();
            String templatePath = "";
            String template = "";
            for (int i = 0; i < requestURIArray.length - 2; i++) {
                if (i == requestURIArray.length - 3) {
                    template = requestURIArray[i];
                    String classNameElement = requestURIArray[i];
                    requestURIArray[i] = classNameElement.substring(0, 1).toUpperCase()
                            + classNameElement.substring(1, classNameElement.length());

                } else {
                    templatePath = templatePath + ERWebConstant.FILE_PATH_SPLIT_SYMBOL + requestURIArray[i];

                }
                clzzName = clzzName + ERWebConstant.PACKAGE_SPLIT_SYMBOL + requestURIArray[i];
                actionName = requestURIArray[i];
            }
            ea.setActionClzz(clzzName);
            ea.setTemplate(template);
            ea.setTemplatePath(templatePath);
        } else if (requestURIArray.length >= 4
                && ERWebConstant.RESTFUL_URL_LIST.equalsIgnoreCase(requestURIArray[requestURIArray.length - 3])
                && attrIndex == 0) {
            ea.setMethod(ERWebConstant.RESTFUL_URL_LIST);
            ea.setParams(new String[] { requestURIArray[requestURIArray.length - 2],
                    requestURIArray[requestURIArray.length - 1] });
            String clzzName = ERContext.getActionPackage();
            String templatePath = "";
            String template = "";
            for (int i = 0; i < requestURIArray.length - 3; i++) {
                if (i == requestURIArray.length - 4) {
                    template = requestURIArray[i];
                    String classNameElement = requestURIArray[i];
                    requestURIArray[i] = classNameElement.substring(0, 1).toUpperCase()
                            + classNameElement.substring(1, classNameElement.length());
                } else {
                    templatePath = templatePath + ERWebConstant.FILE_PATH_SPLIT_SYMBOL + requestURIArray[i];

                }
                clzzName = clzzName + ERWebConstant.PACKAGE_SPLIT_SYMBOL + requestURIArray[i];
                actionName = requestURIArray[i];
            }
            ea.setActionClzz(clzzName);
            ea.setTemplate(template);
            ea.setTemplatePath(templatePath);
        } else if (attrIndex > 0 && attrIndex >= 2) {
            ea.setMethod(requestURIArray[attrIndex - 1]);
            int attrsLength = requestURIArray.length - (attrIndex + 1);
            String attrs[] = new String[attrsLength];
            System.arraycopy(requestURIArray, attrIndex + 1, attrs, 0, attrsLength);
            ea.setParams(attrs);
            String clzzName = ERContext.getActionPackage();
            String templatePath = "";
            String template = "";
            for (int i = 0; i < attrIndex - 1; i++) {
                if (i == attrIndex - 2) {
                    template = requestURIArray[i];
                    String classNameElement = requestURIArray[i];
                    requestURIArray[i] = classNameElement.substring(0, 1).toUpperCase()
                            + classNameElement.substring(1, classNameElement.length());
                } else {
                    templatePath = templatePath + ERWebConstant.FILE_PATH_SPLIT_SYMBOL + requestURIArray[i];

                }
                clzzName = clzzName + ERWebConstant.PACKAGE_SPLIT_SYMBOL + requestURIArray[i];
                actionName = requestURIArray[i];
            }
            ea.setActionClzz(clzzName);
            ea.setTemplate(template);
            ea.setTemplatePath(templatePath);
        } else {
            ea.setMethod(ERWebConstant.RESTFUL_DEFAULT_ACTION_METHOD);
            String clzzName = ERContext.getActionPackage();
            String templatePath = "";
            String template = "";
            for (int i = 0; i < requestURIArray.length; i++) {
                if (i == requestURIArray.length - 1) {
                    template = requestURIArray[i];
                    String classNameElement = requestURIArray[i];
                    requestURIArray[i] = classNameElement.substring(0, 1).toUpperCase()
                            + classNameElement.substring(1, classNameElement.length());
                } else {
                    templatePath = templatePath + ERWebConstant.FILE_PATH_SPLIT_SYMBOL + requestURIArray[i];

                }

                clzzName = clzzName + ERWebConstant.PACKAGE_SPLIT_SYMBOL + requestURIArray[i];
                actionName = requestURIArray[i];

            }
            ea.setActionClzz(clzzName);
            ea.setTemplate(template);
            ea.setTemplatePath(templatePath);
        }
        if (!ea.getMethod().equals(ERWebConstant.RESTFUL_DEFAULT_ACTION_METHOD)) {
            ea.setTemplate(actionName + ERWebConstant.FILE_PATH_SPLIT_SYMBOL + ea.getMethod());
        }
        ea.setTemplate(ea.getTemplate().toLowerCase() + ERWebConstant.VELOCITY_SUFFIX);
        ea.setMethodName(ea.getActionClzz() + ERWebConstant.METHOD_SPLIT_SYMBOL + ea.getMethod());
        return ea;
    }

    private int getAttrIndex(String requestURIArray[]) {
        int i = 0;
        int j = 0;
        for (String str : requestURIArray) {
            if (ERWebConstant.RESTFUL_URL_ATTR.equalsIgnoreCase(str)) {
                return j;
            }
            j++;
        }
        return i;

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        doGet(request, response);
    }

    /**
     * 请求URL解析后的对象
     * 
     * @author ke.chenk 2012-6-28 下午5:12:28
     * @mail lingqi1818@gmail.com
     */
    private class ExecuteAction {

        public ExecuteAction() {
        }

        private String   actionClzz;  // action执行类
        private String   method;      // action执行方法
        private String[] params;      // 方法传入参数
        private String   template;    // 模板名称
        private String   templatePath; // 模板路径
        private String   methodName;  //方法名称

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public String getTemplate() {
            return template;
        }

        public void setTemplate(String template) {
            this.template = template;
        }

        public String getTemplatePath() {
            return templatePath;
        }

        public void setTemplatePath(String templatePath) {
            this.templatePath = templatePath;
        }

        public String getActionClzz() {
            return actionClzz;
        }

        public void setActionClzz(String actionClzz) {
            this.actionClzz = actionClzz;
        }

        public String[] getParams() {
            return params;
        }

        public void setParams(String[] params) {
            this.params = params;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

    }

}
