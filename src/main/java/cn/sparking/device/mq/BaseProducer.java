package cn.sparking.device.mq;

import cn.sparking.device.exception.SparkingException;
import cn.sparking.device.adapter.factory.AdapterManager;
import cn.sparking.device.tools.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * MQ Basic Production.
 */
public class BaseProducer {

    protected static final Logger LOG = LoggerFactory.getLogger(BaseProducer.class);

    private static final RabbitTemplate RABBITTEMPLATE = AdapterManager.getBean("MQCloudTemplate", RabbitTemplate.class);

    /**
     * 指定 Head RPC 发送方法.
     * @param exchange exchange
     * @param topic topic
     * @param method method
     * @param from data from to
     * @param version data version
     * @param character data character default UTF-8
     * @param msg business data
     * @param headData head data
     * @param callback callback
     */
    public void send(final String exchange, final String topic, final String method, final String from,
                     final String version, final String character, final String msg,
                     final Map<String, String> headData, final PublishCallback callback) {
        try {
            String currentTime = DateTimeUtils.timestamp();
            LOG.info("#" + method + "==> " + msg + " to mq at " + currentTime + ", topic = " + topic);
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            messageProperties.setHeader("method", method);
            messageProperties.setHeader("timestamp", currentTime);
            messageProperties.setHeader("from", from);
            messageProperties.setHeader("character", character);
            messageProperties.setHeader("version", version);
            messageProperties.setHeader("adapter", "Sparking");
            messageProperties.setHeader("topic", topic);
            if (Optional.ofNullable(headData).isPresent()) {
                headData.forEach((key, value) -> {
                    LOG.info("#" + method + " <HEAD> ==> " + key + " : " + value);
                    messageProperties.setHeader(key, value);
                });
            }
            Message message = new Message(msg.getBytes(StandardCharsets.UTF_8), messageProperties);

            CorrelationData correlationData = new CorrelationData();
            RABBITTEMPLATE.send(exchange, topic, message, correlationData);
            correlationData.getFuture().addCallback(confirm -> {
                callback.handle(Objects.requireNonNull(confirm).isAck());
                if (!confirm.isAck()) {
                    LOG.warn("Message deliver to MQ failed, reason = " + confirm.getReason());
                }
            }, e -> {
                    LOG.warn("Message deliver to MQ exception, " + e);
                    callback.handle(false);
                });
        } catch (SparkingException e) {
            Arrays.stream(e.getStackTrace()).forEach(item -> LOG.error(item.toString()));
        }
    }
}
