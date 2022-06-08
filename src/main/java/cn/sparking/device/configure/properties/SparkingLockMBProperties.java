package cn.sparking.device.configure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "sparking.lock.move-broad")
public class SparkingLockMBProperties {
    private Boolean active;

    private String url;

    private String appId;

    private String secret;
}
