package com.liuliu.ceres.plugin.httpclient.webClient;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import com.liuliu.ceres.constant.Constants;
import com.liuliu.ceres.core.CeresContext;
import com.liuliu.ceres.plugin.base.EndpointPlugin;
import com.liuliu.ceres.plugin.chain.CeresPluginChain;
import com.liuliu.ceres.plugin.httpclient.webClient.utils.WebClientUtil;

import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import org.springframework.http.HttpStatus;

/**
 * @className WebClientEndpointPlugin
 * @description
 * @author liuliu
 * @email liuliu.zhao@mastercard.com
 * @date 2019-03-15 10:45
 **/
public class WebClientEndpointPlugin extends EndpointPlugin {

    private static final Logger log = LoggerFactory.getLogger(WebClientEndpointPlugin.class);

    private ObjectProvider<HttpClient> httpClient;

    public WebClientEndpointPlugin(final ObjectProvider<HttpClient> httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public String pluginName() {
        return "WebClientEndpointPlugin";
    }

    @Override
    public int pluginOrder() {
        return 2;
    }

    @Override
    public boolean skipPlugin() {
        return false;
    }

    @Override
    public Mono<Void> doPlugin(CeresContext context, CeresPluginChain chain) {
        log.info("doPlugin {}!", this);
        ServerWebExchange exchange = context.getCeresRequst();
        ServerHttpRequest request = exchange.getRequest();
        HttpMethod method = HttpMethod.valueOf(exchange.getRequest().getMethodValue());
        WebClient webClient = WebClientUtil.getWebClient("http://127.0.0.1:8080/", httpClient);

        RequestBodySpec bodySpec = webClient.method(method).uri("hello/mono01").headers(httpHeaders -> {
            httpHeaders.addAll(exchange.getRequest().getHeaders());
            httpHeaders.remove(HttpHeaders.HOST);
        });;

        RequestHeadersSpec<?> headersSpec;
        if (requiresBody(method)) {
            headersSpec = bodySpec.body(BodyInserters.fromDataBuffers(request.getBody()));
        } else {
            headersSpec = bodySpec;
        }
 
        long timeout = (long) Optional.ofNullable(exchange.getAttribute(Constants.HTTP_TIME_OUT)).orElse(3000L);
        return handleRequestBody(bodySpec, exchange, timeout, chain, context);
    }

    private MediaType buildMediaType(final ServerWebExchange exchange) {
        return MediaType.valueOf(Optional.ofNullable(exchange.getRequest().getHeaders().getFirst(HttpHeaders.CONTENT_TYPE))
                        .orElse(MediaType.APPLICATION_JSON_VALUE));
    }

    private Mono<Void> handleRequestBody(RequestBodySpec bodySpec, ServerWebExchange exchange, long timeout,
            CeresPluginChain chain, CeresContext context) {
        return bodySpec
                .contentType(buildMediaType(exchange))
                .exchange()
                .timeout(Duration.ofMillis(timeout),Mono.error(new TimeoutException("Response took longer than timeout: "+timeout)))
                .doOnError(throwable -> log.error(throwable.getMessage()))
                .onErrorMap(TimeoutException.class,th -> new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, th.getMessage(), th))
                .flatMap(clientResponse -> doNext(clientResponse, exchange, chain, context));
    }

    private Mono<Void> doNext(ClientResponse clientResponse,ServerWebExchange exchange,CeresPluginChain chain,CeresContext context) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().putAll(clientResponse.headers().asHttpHeaders());
        response.setStatusCode(clientResponse.statusCode());

        exchange.getAttributes().put(Constants.CLIENT_RESPONSE_ATTR, clientResponse);
        return chain.execute(context);
    }

    private boolean requiresBody(HttpMethod method) {
        switch (method) {
        case PUT:
        case POST:
        case PATCH:
            return true;
        default:
            return false;
        }
    }
}
