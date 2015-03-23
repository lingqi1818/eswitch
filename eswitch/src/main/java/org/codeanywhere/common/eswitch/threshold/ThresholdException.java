package org.codeanywhere.common.eswitch.threshold;

/**
 * 超过阀值上限,则触发 ThresholdException异常.
 * 
 * @author chenke
 */
public class ThresholdException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Type              type;

    public ThresholdException(Type type){
        super();
        this.type = type;
    }

    public ThresholdException(Type type, String message){
        super(message);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public static enum Type {
        /** 达到上限,被拒绝 */
        Reject,
        /** 阀值配置没找到 */
        ThresholdNotFound;
    }

}
