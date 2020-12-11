package com.mastercard.ceres.plugin.loader;

import java.util.Comparator;
import java.util.SortedSet;

import com.mastercard.ceres.plugin.CeresPlugin;
import com.mastercard.ceres.plugin.PluginType;

/**
 * @className PluginLoader
 * @description
 * @author liuliu
 * @version 1.0
 * @email liuliu.zhao@mastercard.com
 */
public interface CeresPluginLoader {

    SortedSet<CeresPlugin> getPluginsByType(PluginType pluginType);

    CeresPlugin getPluginByNameAndType(String pluginName,PluginType pluginType);

    Comparator<CeresPlugin> FILTER_COMPARATOR = Comparator.<CeresPlugin>comparingInt(CeresPlugin::pluginOrder)
            .thenComparing(CeresPlugin::pluginName);
}
