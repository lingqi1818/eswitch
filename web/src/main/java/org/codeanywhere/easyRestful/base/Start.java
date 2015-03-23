package org.codeanywhere.easyRestful.base;

import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.codeanywhere.easyRestful.base.config.Config;
import org.codeanywhere.easyRestful.base.constant.ERWebConstant;
import org.codeanywhere.easyRestful.base.context.ERContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 程序运行总入口
 * 
 * @author ke.chenk 2012-5-30 下午5:18:46
 * @mail lingqi1818@gmail.com
 */
public class Start {

    public static void stop() {
    }

    public static void start() {
        initResourcePath(Config.getConfig(ERWebConstant.RESOURCE_PATH, null));
        initVelocityEngine(Config.getConfig(ERWebConstant.RESOURCE_PATH, null));
        initActionPackage(Config.getConfig(ERWebConstant.ACTION_PACKAGE, null));
        initUrlMatchIndex(Integer.valueOf(Config.getConfig(ERWebConstant.URL_MATCH_INDEX, "0")));
        if (Config.getConfig(ERWebConstant.SPRING_IS_ENABLE, "").equalsIgnoreCase("true"))
            initSpring();
    }

    private static void initUrlMatchIndex(int i) {
        ERContext.setUrlMatchIndex(i);
    }

    private static void initSpring() {
        ERContext.setApplicationContext(new ClassPathXmlApplicationContext(new String[] { "applicationContext.xml" }));
    }

    private static void initResourcePath(String path) {
        ERContext.setResourcePath(path);
    }

    private static void initActionPackage(String pkg) {
        ERContext.setActionPackage(pkg);
    }

    private static void initVelocityEngine(String path) {
        VelocityEngine velocity = new VelocityEngine();
        Properties props = new Properties();
        props.put(RuntimeConstants.RESOURCE_LOADER, "file");
        props.put(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, path + "/vm/");
        props.put(RuntimeConstants.ENCODING_DEFAULT, "UTF-8");
        props.put(RuntimeConstants.INPUT_ENCODING, "UTF-8");
        props.put(RuntimeConstants.OUTPUT_ENCODING, "UTF-8");
        try {
            velocity.init(props);
        } catch (Exception e) {
            throw new RuntimeException("init velocity engine error .", e);
        }
        ERContext.setVelocityEngine(velocity);
    }

}
