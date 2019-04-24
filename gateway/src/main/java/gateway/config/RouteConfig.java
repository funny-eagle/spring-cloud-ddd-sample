package gateway.config;

import gateway.filter.ForkingGatewayFilterFactory;
import gateway.filter.JoiningGatewayFilterFactory;
import gateway.filter.RequestTimeGatewayFilterFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
@EnableConfigurationProperties(RouteConfig.UriConfiguration.class)
public class RouteConfig {

    @Resource
    private ForkingGatewayFilterFactory forkingGatewayFilterFactory;
    @Resource
    private JoiningGatewayFilterFactory joiningGatewayFilterFactory;

    @Resource
    private RequestTimeGatewayFilterFactory requestTimeGatewayFilterFactory;

    /**
     * RouteLocator 用于创建路由
     * RouteLocatorBuilder 允许在路由中添加 predicate 和 filter，以便根据特定条件路由
     * UriConfiguration 从配置中获取URL
     *
     * @param builder
     * @return
     */
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder, UriConfiguration uriConfiguration) {
//        String httpUri = uriConfiguration.getHttpbin();
        return builder.routes()
                .route(p -> p
                        .path("/get")
                        .filters(f -> f.addRequestHeader("Hello", "World"))
                        .uri("http://httpbin.org:80"))
                /**
                 * 使用curl 发起请求，header 中包含 Host: *.hystrix.com，否则请求不会被路由
                 * $ curl --dump-header - --header 'Host: www.hystrix.com' http://localhost:8080/delay/3
                 * 注：使用 --dump-header 查看响应的headers
                 * --dump-heaer 后面的 - 是为了告知 cURL 将headers 打印出来
                 */
//                .route(p -> p.host("*.hystrix.com")
//                        .filters(f -> f.hystrix(config -> config
//                                .setName("mycmd")
//                                .setFallbackUri("forward:/fallback")))
//                        .uri(httpUri))
                .route(r -> r.path("/hello")
                        .filters(f -> {
                            f.filter(requestTimeGatewayFilterFactory.apply(config -> config.setRequestStartTime("startTime")));
                            f.filter(forkingGatewayFilterFactory.apply(c -> c.setName("hello1").setUri("http://localhost:8000/BOOK-SERVICE/api/join/hello1")));
                            f.filter(forkingGatewayFilterFactory.apply(c -> c.setName("hello2").setUri("http://localhost:8000/BOOK-SERVICE/api/join/hello2")));
                            f.filter(joiningGatewayFilterFactory.apply(c -> c.setName("join")));
                            return f;
                        }).uri("http://localhost:8000"))
//                .route(predicateSpec -> predicateSpec.path("/api/book/**")
//                        .filters(gatewayFilterSpec -> {
//                            gatewayFilterSpec.filter(myFilter);
//                            return gatewayFilterSpec;
//                        }).uri("lb://book-service").id("book-service"))
                .build();
    }

    @ConfigurationProperties
    class UriConfiguration {
        private String httpbin = "http://httpbin.org:80";

        public String getHttpbin() {
            return httpbin;
        }

        public void setHttpbin(String httpbin) {
            this.httpbin = httpbin;
        }
    }
}
