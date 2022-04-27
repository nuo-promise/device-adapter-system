package cn.sparking.device.initializer;

import cn.sparking.device.configure.properties.SparkingLockProperties;
import cn.sparking.device.tools.ReactiveRedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;

@Component
public class MoveBroadProcessLoader implements ApplicationRunner {

    private static final Logger LOG = LoggerFactory.getLogger(MoveBroadProcessLoader.class);

    @Resource
    private SparkingLockProperties sparkingLockProperties;

    protected void init() {
        try {
            //登录目博请求秘钥
            LOG.info("request movebroad secret!");

//            ReactiveRedisUtils.putValue("test","1").subscribe(b -> LOG.info("redis result: " + b), e -> LOG.error("redis result: " + e));
//            ReactiveRedisUtils.getData("test").subscribe(b -> LOG.info("redis: " + b));
        } catch (Exception ex) {
            Arrays.stream(ex.getStackTrace()).forEach(item -> LOG.error(item.toString()));
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (sparkingLockProperties.getActive()) {
            this.init();
        }
    }
}
