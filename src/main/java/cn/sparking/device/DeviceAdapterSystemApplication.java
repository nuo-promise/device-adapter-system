package cn.sparking.device;

import cn.sparking.device.logo.SparkingLogo;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DeviceAdapterSystemApplication implements ApplicationRunner {

	/**
     * SpringBoot Run.
     * @param args args
     */
    public static void main(final String[] args) {
        SpringApplication application = new SpringApplication(DeviceAdapterSystemApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }

    @Override
    public void run(final ApplicationArguments args) {
        SparkingLogo.displayLogo();
    }
}
