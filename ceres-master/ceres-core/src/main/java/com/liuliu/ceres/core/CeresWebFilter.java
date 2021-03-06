package com.liuliu.ceres.core;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;
import reactor.util.annotation.Nullable;

/**
 * 
 * @author liuliu
 *
 */
public class CeresWebFilter implements WebFilter {

    private static final Logger log = LoggerFactory.getLogger(CeresWebFilter.class);

    private CeresPluginWebHandler ceresPluginWebHandler;

    private static final String FILTER_TAG = "/ceres";

    public CeresPluginWebHandler getCeresPluginWebHandler() {
        return ceresPluginWebHandler;
    }

    public void setCeresPluginWebHandler(CeresPluginWebHandler ceresPluginWebHandler) {
        this.ceresPluginWebHandler = ceresPluginWebHandler;
    }

    @Override
    public Mono<Void> filter(@Nullable final ServerWebExchange exchange, @Nullable final WebFilterChain chain) {
        ServerHttpRequest request = Objects.requireNonNull(exchange).getRequest();
        String urlPath = request.getURI().getPath();
        log.info("urlPath:{}", urlPath);
        if (FILTER_TAG.equals(urlPath)) {
            return ceresPluginWebHandler.handle(exchange);
        }
        return chain.filter(exchange);
    }
}
