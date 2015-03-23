package org.codeanywhere.easyRestful.base;

/**
 * mvc请求执行action类
 * 方法渲染的vm都和方法名一致，execute方法除外
 * execute方法渲染的vm名和类名一致
 * 
 * @author ke.chenk 2012-6-28 下午5:45:10
 * @mail lingqi1818@gmail.com
 */
public interface Action {

    /**
     * 假如URI为无method类型，此为默认被调用方法
     */
    public void execute();

    /**
     * list方法
     * 
     * @param start 记录开始ID
     * @param end 记录结束ID
     */
    public void list(String start, String end);

    /**
     * detail方法
     * 
     * @param id 记录ID
     */
    public void detail(String id);
}
