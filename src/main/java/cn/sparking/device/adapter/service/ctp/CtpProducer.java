package cn.sparking.device.adapter.service.ctp;

import cn.sparking.device.configure.properties.RabbitmqProperties;
import cn.sparking.device.constant.CtpConstants;
import cn.sparking.device.model.ctp.ParkStatusModel;
import cn.sparking.device.mq.BaseMQData;
import cn.sparking.device.mq.BaseProducer;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("CtpAdapter")
public class CtpProducer extends BaseProducer {

    private static final Logger LOG = LoggerFactory.getLogger(CtpProducer.class);

    private static final String TOPICPREFIX = "ctp.device.";

    private static final String TOPICLASTFIX = ".data";

    private static final String TOPICLASTEVENTFIX = ".event";

    private final RabbitmqProperties rabbitmqProperties;

    public CtpProducer(final RabbitmqProperties rabbitmqProperties) {
        this.rabbitmqProperties = rabbitmqProperties;
    }


    /**
     * producer device status.
     * @param parkStatusModel {@link ParkStatusModel}
     */
    public void publishLockStatus(final ParkStatusModel parkStatusModel) {
        send(rabbitmqProperties.getExchange(), TOPICPREFIX + "status" + TOPICLASTFIX,
                CtpConstants.CTP_REQUEST_PARK_STATUS, CtpConstants.CTP_FLAG,
                CtpConstants.CTP_VERSION, CtpConstants.CTP_CHARACTER,
                JSON.toJSONString(new CtpProducer.CtpData(JSON.toJSONString(parkStatusModel), "save")), null);
    }

    @Data
    public static class CtpData extends BaseMQData {

        private static final long serialVersionUID = 2545834301908607338L;

        private final Object data;

        CtpData(final Object data, final String type) {
            super("CTP", type);
            this.data = data;
        }

        /**
         * To String.
         * @return String
         */
        public String toString() {
            return "CtpData {" + "data = " + data + " } " + super.toString();
        }
    }
}
