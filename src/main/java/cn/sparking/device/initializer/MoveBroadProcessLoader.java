package cn.sparking.device.initializer;

import cn.sparking.device.adapter.inf.movebroad.MoveBroadService;
import cn.sparking.device.configure.properties.SparkingLockProperties;
import cn.sparking.device.constant.MoveBroadConstants;
import cn.sparking.device.model.movebroad.LoginModel;
import cn.sparking.device.tools.MoveBroadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;

import static cn.sparking.device.constant.MoveBroadConstants.MB_SUCCESS;

@Component
public class MoveBroadProcessLoader implements ApplicationRunner {

    private static final Logger LOG = LoggerFactory.getLogger(MoveBroadProcessLoader.class);

    @Resource
    private SparkingLockProperties sparkingLockProperties;

    @Autowired
    private MoveBroadService moveBroadService;

    protected void init() {
        try {
            Integer result = moveBroadService.accessToken(LoginModel.builder()
                    .appId(sparkingLockProperties.getAppId())
                    .secret(sparkingLockProperties.getSecret())
                    .url(sparkingLockProperties.getUrl() + MoveBroadConstants.MB_LOGIN).build());
            if (MB_SUCCESS.equals(result)) {
                LOG.info("登录 MoveBroad Success. 访问参数:" + MoveBroadUtils.getValue());
            } else {
                LOG.error("登录 MoveBroad Error. " + result);
            }
        } catch (Exception ex) {
            Arrays.stream(ex.getStackTrace()).forEach(item -> LOG.error(item.toString()));
        }
    }

    @Override
    public void run(final ApplicationArguments args) throws Exception {
        if (sparkingLockProperties.getActive()) {
            this.init();
        }
    }
}
