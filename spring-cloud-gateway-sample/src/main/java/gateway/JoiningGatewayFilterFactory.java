package gateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.AbstractMap;
import java.util.Map;

import static gateway.ForkingGatewayFilterFactory.FORK_JOIN_GATEWAY_FILTER_RESPONSE_ATTR;

@Component
public class JoiningGatewayFilterFactory implements GatewayFilterFactory<AbstractGatewayFilterFactory.NameConfig> {


    @Override
    public GatewayFilter apply(AbstractGatewayFilterFactory.NameConfig config) {

        return (exchange, chain) -> {

            //this is a little gross
            Map<String, Mono<ClientResponse>> responses = exchange.getAttribute(FORK_JOIN_GATEWAY_FILTER_RESPONSE_ATTR);
            Mono<Map<String, String>> monoResponse = convert(responses);

            DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
            return exchange.getResponse().writeWith(monoResponse.map(r -> dataBufferFactory.wrap(r.toString().getBytes())));
        };
    }


    private Mono<Map.Entry<String, String>> convert(Map.Entry<String, Mono<ClientResponse>> source) {
        String key = source.getKey();
        Mono<ClientResponse> monoResponse = source.getValue();
        Mono<String> monoBody = monoResponse.flatMap(response -> response.bodyToMono(String.class));
        return monoBody.map(body -> new AbstractMap.SimpleEntry<>(key, body));
    }

    private Mono<Map<String, String>> convert(Map<String, Mono<ClientResponse>> source) {
        Mono<Map<String, String>>  monoMap = Flux.fromIterable(source.entrySet())
                .flatMap(this::convert)
                .collectMap(Map.Entry::getKey, Map.Entry::getValue);

        System.out.println(monoMap);
        return monoMap;

    }

    @Override
    public AbstractGatewayFilterFactory.NameConfig newConfig() {
        return new AbstractGatewayFilterFactory.NameConfig();
    }
}
