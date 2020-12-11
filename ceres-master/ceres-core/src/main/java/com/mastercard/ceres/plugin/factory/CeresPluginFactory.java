package com.mastercard.ceres.plugin.factory;

import com.mastercard.ceres.plugin.CeresPlugin;

/**
 * @className CeresPluginFactory
 * @description
 * @author liuliu
 * @email liuliu.zhao@mastercard.com
 * @date 2019-03-15 10:45
 **/
public interface  CeresPluginFactory {
    CeresPlugin newInstance(Class<?> clazz) throws Exception;
}
