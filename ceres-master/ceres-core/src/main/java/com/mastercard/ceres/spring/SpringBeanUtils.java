package com.mastercard.ceres.spring;

import org.springframework.context.ConfigurableApplicationContext;

/**
 * @className SpringBeanUtils
 * @description
 * @author liuliu
 * @email liuliu.zhao@mastercard.com
 * @date 2019-03-15 10:45
 **/
public class SpringBeanUtils{

    private static final SpringBeanUtils INSTANCE = new SpringBeanUtils();

    private ConfigurableApplicationContext cfgContext;

    private SpringBeanUtils() {
    }

    /**
     * get SpringBeanUtils.
     *
     * @return SpringBeanUtils
     */
    public static SpringBeanUtils getInstance() {
        return INSTANCE;
    }

    /**
     * acquire spring bean.
     *
     * @param type type
     * @param <T>  class
     * @return bean
     */
    public <T> T getBean(final Class<T> type) {
        return cfgContext.getBean(type);
    }

    /**
     * set application context.
     *
     * @param cfgContext application context
     */
    public void setCfgContext(final ConfigurableApplicationContext cfgContext) {
        this.cfgContext = cfgContext;
    }
}
