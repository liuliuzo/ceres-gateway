package com.mastercard.ceres.bootstrap.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.mastercard.ceres.core.CeresContext;
import com.mastercard.ceres.plugin.base.InBoundPlugin;

import reactor.core.publisher.Mono;

@Component
public class CeresDemoPlugin1 extends InBoundPlugin {

    private static final Logger log = LoggerFactory.getLogger(CeresDemoPlugin1.class);

    @Override
    public int pluginOrder() {
        return 1;
    }

    @Override
    public String pluginName() {
        return "CeresDemoPlugin1";
    }

    @Override
    public boolean stopPluginProcessing() {
        return true;
    }

    @Override
    public boolean skipPlugin() {
        return false;
    }

    @Override
    public Mono<Void> doPlugin(CeresContext context) {
        log.info("doPlugin !");
        return Mono.empty();
    }
}