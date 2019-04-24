package gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
public class ForkingGatewayFilterFactory implements GatewayFilterFactory<ForkingGatewayFilterFactory.Config> {

    public static final String RESPONSE_ATTR = ForkingGatewayFilterFactory.class.getName() + ".responseAttribute";

    private final WebClient webClient = WebClient.builder().build();

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            //populate rest of response field
            Mono<ClientResponse> forkedResponse = webClient.get().uri(config.uri).exchange();
            //computeIfAbsent would be nice here
            //also this block probably isn't thread safe
            Map<String, Mono<ClientResponse>> responseMap = exchange.getAttributeOrDefault(RESPONSE_ATTR, new HashMap<>(2));
            responseMap.put(config.name, forkedResponse);
            exchange.getAttributes().put(RESPONSE_ATTR, responseMap);

            return chain.filter(exchange);
        };
    }

    public static class Config {
        String name;
        String uri;

        public Config setName(String name) {
            this.name = name;
            return this;
        }

        public Config setUri(String uri) {
            this.uri = uri;
            return this;
        }
    }

    @Override
    public Config newConfig() {
        return new Config();
    }
}
