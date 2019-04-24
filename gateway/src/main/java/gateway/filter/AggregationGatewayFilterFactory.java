package gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class AggregationGatewayFilterFactory implements GatewayFilterFactory {

    private final WebClient webClient = WebClient.builder().build();

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            // 在这里使用 http 请求访问接口，然后返回将返回的数据进行整合
            Mono<ClientResponse> forkedResponse = webClient.get().uri("http://localhost:8080/hello").exchange();



            return chain.filter(exchange);
        };
    }
    public static class Config {
        private String conf;

        public Config() {
        }

        public String getConf() {
            return conf;
        }

        public void setConf(String conf) {
            this.conf = conf;
        }
    }
}
