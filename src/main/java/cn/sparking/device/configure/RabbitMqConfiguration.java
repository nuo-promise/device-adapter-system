package cn.sparking.device.configure;

import cn.sparking.device.configure.properties.RabbitmqProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.retry.policy.AlwaysRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import javax.annotation.Resource;

@Configuration
public class RabbitMqConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMqConfiguration.class);

    @Resource
    private RabbitmqProperties rabbitmqProperties;

    /**
     * init mq connection factory.
     * @param host connect host
     * @param port connect port
     * @param userName connect username
     * @param password connect password
     * @return {@link ConnectionFactory}
     */
    @Primary
    @ConditionalOnMissingBean(value = RabbitmqProperties.class)
    @Bean(name = "DeviceCloudMQConnectionFactory")
    public ConnectionFactory cloudMqConnectionFactory(
            @Value("${spring.rabbitmq.host}") final String host,
            @Value("${spring.rabbitmq.port}") final int port,
            @Value("${spring.rabbitmq.username}") final String userName,
            @Value("${spring.rabbitmq.password}") final String password
    ) {
        LOG.info("===========CloudMQ: Device Cloud MQ ConnectionFactory 建立连接成功=============");
        CachingConnectionFactory connection = new CachingConnectionFactory();
        connection.setHost(host);
        connection.setPort(port);
        connection.setUsername(userName);
        connection.setPassword(password);
        return connection;
    }

    /**
     * init rabbitmq template.
     * @param connection {@link ConnectionFactory}
     * @return {@link RabbitTemplate}
     */
    @Primary
    @ConditionalOnMissingBean(value = RabbitmqProperties.class)
    @Bean(name = "DeviceCloudMQTemplate")
    public RabbitTemplate deviceRabbitTemplate(
            @Qualifier("DeviceCloudMQConnectionFactory") final ConnectionFactory connection
    ) {
        LOG.info("===========CloudMQ: Device Cloud MQ Template 建立连接成功=============");
        return new RabbitTemplate(connection);
    }


    /**
     * init rabbitmq topic exchange.
     * @return {@link TopicExchange}
     */
    @Primary
    @Bean(name = "DeviceCloudMQTopicExchange")
    @ConditionalOnMissingBean(value = RabbitmqProperties.class)
    public TopicExchange deviceExchange() {
        LOG.info("===========CloudMQ: Device Cloud MQ Exchange 建立连接成功=============");
        return new TopicExchange(rabbitmqProperties.getExchange());
    }

    /**
     * init rabbitmq admin.
     * @param connection {@link ConnectionFactory}
     * @return {@link RabbitAdmin}
     */
    @Primary
    @ConditionalOnMissingBean(value = RabbitmqProperties.class)
    @Bean(name = "DeviceRabbitAdmin")
    public RabbitAdmin deviceAdmin(@Qualifier("DeviceCloudMQConnectionFactory") final ConnectionFactory connection) {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(new AlwaysRetryPolicy());
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connection);
        rabbitAdmin.setRetryTemplate(retryTemplate);
        return rabbitAdmin;
    }
}
