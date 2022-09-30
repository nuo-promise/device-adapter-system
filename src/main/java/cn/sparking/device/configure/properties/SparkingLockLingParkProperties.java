package cn.sparking.device.configure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "sparking.lock.lingpark")
public class SparkingLockLingParkProperties {

    private Boolean active;

    private String appId;

    private String comId;

    private String secret;

}
