package org.codeanywhere.common.eswitch.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.codeanywhere.common.eswitch.Item;
import org.codeanywhere.common.eswitch.ItemListener;
import org.codeanywhere.common.eswitch.Item.ItemNotFoundException;
import org.codeanywhere.common.eswitch.server.DefaultActionServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;


/**
 * <pre>
 * SwitchEngine实现. 
 * Singleton, 一个应用只需要一份 SwitchEngine 实例.
 * 
 * @author chenke
 */
public class DefaultSwitchEngine implements InternalSwitchEngine, InitializingBean, DisposableBean {

    private static final Logger       LOGGER    = LoggerFactory
                                                        .getLogger(DefaultSwitchEngine.class);
    private String                    application;
    private Map<String, InternalItem> items     = new ConcurrentHashMap<String, InternalItem>();
    private List<ItemListener>        listeners = new ArrayList<ItemListener>();
    private DefaultActionServer       server;

    @Override
    public void afterPropertiesSet() throws Exception {
        // reload所有开关项
        reload();
        // 启动server
        if (server == null) {
            LOGGER.error("ActionServer is null.");
            return;
        }
        server.start();
        if (!server.isStartOk()) {
            LOGGER.error("ActionServer startup fail.");
            return;
        }
        register();
    }

    @Override
    public void destroy() throws Exception {
        if (server == null) {
            LOGGER.error("ActionServer is null.");
            return;
        }
        unregister();
        server.stop();
    }

    @Override
    public String getApplication() {
        return application;
    }

    @Override
    public void register() {

        if (server == null) {
            LOGGER.error("SwitchEngine#register fail. ActionServer is null.");
            return;
        }
        if (!server.isStartOk()) {
            LOGGER.error("SwitchEngine#register fail. ActionServer startup fail.");
            return;
        }
    }

    public void unregister() {
        if (server == null) {
            LOGGER.error("SwitchEngine#unregister fail. ActionServer is null.");
            return;
        }
        if (!server.isStartOk()) {
            LOGGER.error("SwitchEngine#unregister fail. ActionServer startup fail.");
            return;
        }
    }

    @Override
    public void reload() {
        //noting
    }

    @Override
    public Map<String, InternalItem> getItems() {
        return items;
    }

    public InternalItem getItem(String name) throws ItemNotFoundException {
        InternalItem item = items.get(name);
        if (item == null) {
            LOGGER.error("item named: [{}] not found.", new Object[] { name });
            throw new ItemNotFoundException("item named:[" + name + "] not found. ");
        }
        return item;
    }

    public InternalItem getItemDefaultNull(String name) {
        InternalItem item = items.get(name);
        return item;
    }

    @Override
    public boolean isOn(String name) throws ItemNotFoundException {
        return getItem(name).isOn();
    }

    @Override
    public boolean isOn(String name, boolean defaultValue) {
        InternalItem item = getItemDefaultNull(name);
        if (null == item) {
            return defaultValue;
        }
        return item.isOn();
    }

    @Override
    public long getThreshold(String name) throws ItemNotFoundException {
        return getItem(name).getThreshold();
    }

    @Override
    public long getThreshold(String name, long defaultValue) {
        try {
            return getThreshold(name);
        } catch (ItemNotFoundException e) {
            InternalItem item = new DefaultItem();
            item.setName(name);
            item.setOn(true); // 默认开关开启.
            item.setThreshold(defaultValue);
            items.put(name, item);
            return defaultValue;
        }
    }

    @Override
    public void setItem(String name, InternalItem item) throws ItemNotFoundException {
        InternalItem existingItem = items.get(name);
        item.setName(name);
        if (null == existingItem) {
            existingItem = item;
            items.put(name, existingItem);
        } else {
            existingItem.setOn(item.isOn());
            existingItem.setThreshold(item.getThreshold());
            existingItem.setDetail(item.getDetail());
            existingItem.clearUserAttributes();
        }
        notify(existingItem);
    }

    @Override
    public void setListeners(List<ItemListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void addListener(ItemListener listener) {
        List<ItemListener> newList = new ArrayList<ItemListener>();
        newList.addAll(this.listeners);
        newList.add(listener);
        this.listeners = newList;
    }

    @Override
    public void removeListener(ItemListener listener) {
        List<ItemListener> newList = new ArrayList<ItemListener>();
        newList.addAll(this.listeners);
        newList.remove(listener);
        this.listeners = newList;
    }

    protected void notify(Item item) {
        if (listeners != null && !listeners.isEmpty()) {
            for (ItemListener l : listeners) {
                l.onItemChanged(item);
            }
        }
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public void setServer(DefaultActionServer server) {
        this.server = server;
    }

    public static void main(String[] args) throws Exception {
        DefaultSwitchEngine switchEngine = new DefaultSwitchEngine();
        switchEngine.setApplication("TestApp");
        switchEngine.setServer(new DefaultActionServer());
        switchEngine.afterPropertiesSet();
        Thread.sleep(1000 * 60 * 7);
        System.out.println("stop");
        switchEngine.destroy();
    }

}
