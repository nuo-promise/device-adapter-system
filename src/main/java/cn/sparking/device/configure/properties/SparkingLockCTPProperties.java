package cn.sparking.device.configure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "sparking.lock.ctp")
public class SparkingLockCTPProperties {

    private Boolean active;

    private String secret;

    private String factoryId;

    private String url;
}
