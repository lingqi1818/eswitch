package org.codeanywhere.easyRestful.base.session.store;

/**
 * @author chenke
 * @date 2014-6-17 1:19:40
 */
public interface SessionStore {
    /**
     */
    public void init();

    /**
     * @param sessionId
     * @param key
     * @param value
     */
    public void put(String sessionId, String key, Object value);

    /**
     * @param sessionId
     * @param key
     * @return
     */
    public Object get(String sessionId, String key);

    /**
     * @param sessionId
     * @return
     */
    public String[] getAllKeys(String sessionId);

    /**
     * @param sessionId
     * @param key
     */
    public void delete(String sessionId, String key);

    /**
     * @param sessionId
     */
    public void clean(String sessionId);

}
