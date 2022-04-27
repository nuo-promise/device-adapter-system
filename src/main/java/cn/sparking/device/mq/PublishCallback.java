package cn.sparking.device.mq;

public interface PublishCallback {

    /**
     * MQ call back.
     * @param success the success
     */
    void handle(boolean success);
}
