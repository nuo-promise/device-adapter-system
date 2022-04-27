package cn.sparking.device.adapter.factory;

import cn.sparking.device.adapter.BaseAdapter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Adapted Factory.
 * @deprecated not use
 */
@Component
public class AdapterManager implements ApplicationContextAware {

    private static final Map<String, BaseAdapter> ADAPTED_SERVICE_MAP = new ConcurrentHashMap<>();

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(final ApplicationContext ctx) throws BeansException {
        ctx.getBeansOfType(BaseAdapter.class).values().forEach(adapted -> {
            Class<? extends BaseAdapter> adaptedServiceClass = adapted.getClass();
            ADAPTED_SERVICE_MAP.put(adaptedServiceClass.getSimpleName(), adapted);
        });
        applicationContext = ctx;
    }

    /**
     * get adapted service.
     * @param beanName adapted bean name
     * @return {@link BaseAdapter}
     */
    public BaseAdapter getAdaptedService(final String beanName) {
        return ADAPTED_SERVICE_MAP.get(beanName);
    }

    /**
     * get class bean.
     * @param name class name
     * @param clazz class
     * @param <T> class
     * @return class
     */
    public static <T> T getBean(final String name, final Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }
}
