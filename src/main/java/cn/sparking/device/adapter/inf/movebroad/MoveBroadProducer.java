package cn.sparking.device.adapter.inf.movebroad;

import cn.sparking.device.configure.properties.RabbitmqProperties;
import cn.sparking.device.constant.MoveBroadConstants;
import cn.sparking.device.model.movebroad.PublishLockInfoModel;
import cn.sparking.device.model.movebroad.PublishLockStatusModel;
import cn.sparking.device.mq.BaseMQData;
import cn.sparking.device.mq.BaseProducer;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoveBroadProducer extends BaseProducer {

    private static final Logger LOG = LoggerFactory.getLogger(MoveBroadProducer.class);

    private static final String TOPICPREFIX = "move-broad.device.";

    private static final String TOPICLASTFIX = ".data";

    private static final String TOPICLASTEVENTFIX = ".event";

    private final RabbitmqProperties rabbitmqProperties;

    public MoveBroadProducer(final RabbitmqProperties rabbitmqProperties) {
        this.rabbitmqProperties = rabbitmqProperties;
    }

    /**
     * producer device status.
     * @param publishLockStatusModel {@link PublishLockStatusModel}
     */
    public void publishLockStatus(final PublishLockStatusModel publishLockStatusModel) {
        send(rabbitmqProperties.getExchange(), TOPICPREFIX + "status" + TOPICLASTFIX,
                MoveBroadConstants.MOVE_BROAD_MQ_METHOD_LOCK_STATUS, MoveBroadConstants.MB_LOCK_FLAG,
                MoveBroadConstants.MB_VERSION, MoveBroadConstants.MB_CHARACTER,
                JSON.toJSONString(new MoveBroadData(JSON.toJSONString(publishLockStatusModel), "save")), null, success -> {
                if (success) {
                    LOG.info("Deliver MB Lock-Status message success, lockCode = " + publishLockStatusModel.getLockCode());
                } else {
                    LOG.error("Deliver MB Lock-Status message failed, lockCode = " + publishLockStatusModel.getLockCode());
                }
            });
    }

    /**
     * producer device info.
     * @param publishLockInfoModel {@link PublishLockInfoModel}
     */
    public void publishLockInfo(final PublishLockInfoModel publishLockInfoModel) {
        send(rabbitmqProperties.getExchange(), TOPICPREFIX + "info" + TOPICLASTFIX,
                MoveBroadConstants.MOVE_BROAD_MQ_METHOD_LOCK_INFO, MoveBroadConstants.MB_LOCK_FLAG,
                MoveBroadConstants.MB_VERSION, MoveBroadConstants.MB_CHARACTER,
                JSON.toJSONString(new MoveBroadData(publishLockInfoModel, "save")), null, success -> {
                if (success) {
                    LOG.info("Deliver MB Lock-Info message success, lockCode = " + publishLockInfoModel.getLockCode());
                } else {
                    LOG.error("Deliver MB Lock-Info message failed, lockCode = " + publishLockInfoModel.getLockCode());
                }
            });
    }

    @Data
    public static class MoveBroadData extends BaseMQData {

        private static final long serialVersionUID = 2545834301908607338L;

        private final Object data;

        MoveBroadData(final Object data, final String type) {
            super("MoveBroad", type);
            this.data = data;
        }

        /**
         * To String.
         * @return String
         */
        public String toString() {
            return "MoveBroadData {" + "data = " + data + " } " + super.toString();
        }
    }
}
