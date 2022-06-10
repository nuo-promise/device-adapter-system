package cn.sparking.device.mq;

import cn.sparking.device.exception.SparkingException;
import cn.sparking.device.adapter.factory.AdapterManager;
import cn.sparking.device.tools.DateTimeUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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

import static cn.sparking.device.exception.CtpErrorCode.REQUEST_TIMEOUT;
import static cn.sparking.device.exception.CtpErrorCode.SUCCESS;


/**
 * MQ Basic Production.
 */
public class BaseProducer {

    protected static final Logger LOG = LoggerFactory.getLogger(BaseProducer.class);

    private static final RabbitTemplate RABBITTEMPLATE = AdapterManager.getBean("MQCloudTemplate", RabbitTemplate.class);

    /**
     * 指定 Head 发送方法.
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
     */
    public int send(final String exchange, final String topic, final String method, final String from,
                     final String version, final String character, final String msg,
                     final Map<String, String> headData) {
        try {
            String currentTime = DateTimeUtils.timestamp();
            LOG.info("#" + method + "==> " + msg + " to mq rpc at " + currentTime + ", topic = " + topic);
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

            Object receiveMessage = RABBITTEMPLATE.convertSendAndReceive(exchange, topic, message);
            if (Objects.nonNull(receiveMessage)) {
                JSONObject retJson = JSON.parseObject(new String((byte[]) receiveMessage));
                LOG.info("接收 C 端 RPC 消费返回: " + retJson.toJSONString());
                if (!retJson.containsKey("code") || retJson.getInteger("code") != 200) {
                   LOG.warn("C 端消费 RPC 设备状态信息失败, 下面执行 HTTP 请求 " + msg);
                   return REQUEST_TIMEOUT;
                }
                return SUCCESS;
            }
        } catch (SparkingException e) {
            Arrays.stream(e.getStackTrace()).forEach(item -> LOG.error(item.toString()));
        }
        return REQUEST_TIMEOUT;
    }
}
