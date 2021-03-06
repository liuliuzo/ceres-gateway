package com.liuliu.ceres.bootstrap.configuration;

import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.ReactorNetty;
import reactor.netty.http.server.HttpServer;
import reactor.netty.resources.LoopResources;

/**
 * The type Ceres netty web server factory.
 *
 * @author liuliu
 */
//@Configuration
public class CeresNettyWebServerFactory {

    private static final int DEFAULT_IO_WORKER_COUNT = Integer.parseInt(System.getProperty(ReactorNetty.IO_WORKER_COUNT,
            "" + Math.max(Runtime.getRuntime().availableProcessors() << 1, 16)));

    /**
     * Netty reactive web server factory netty reactive web server factory.
     *
     * @return the netty reactive web server factory
     */
    @Bean
    public NettyReactiveWebServerFactory nettyReactiveWebServerFactory() {
        NettyReactiveWebServerFactory webServerFactory = new NettyReactiveWebServerFactory();
        webServerFactory.addServerCustomizers(new EventLoopNettyCustomizer());
        return webServerFactory;
    }

    private static class EventLoopNettyCustomizer implements NettyServerCustomizer {

        @Override
        public HttpServer apply(final HttpServer httpServer) {
            return httpServer.tcpConfiguration(tcpServer -> tcpServer
                            .runOn(LoopResources.create("ceres-netty", 1, DEFAULT_IO_WORKER_COUNT, true), false)
                            .selectorOption(ChannelOption.SO_REUSEADDR, true)
                            .selectorOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                            .option(ChannelOption.TCP_NODELAY, true)
                            .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT));
        }
    }
}
