package gateway.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.util.Arrays;
import java.util.List;

/**
 * RequestTimeGatewayFilterFactory
 * @author YangJinlong
 * @date 2019-03-28
 */
@Component
public class RequestTimeGatewayFilterFactory extends AbstractGatewayFilterFactory<RequestTimeGatewayFilterFactory.Config> {
    private static final String REQUEST_START_TIME = "requestStartTime";


    private static final Log log = LogFactory.getLog(RequestTimeGatewayFilterFactory.class);

    public RequestTimeGatewayFilterFactory() {
        super(RequestTimeGatewayFilterFactory.Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("requestStartTime");
    }

    @Override
    public GatewayFilter apply(RequestTimeGatewayFilterFactory.Config config) {
        return (exchange, chain) -> {
            final long startTime = System.currentTimeMillis();
            System.out.println("请求开始：" + exchange.getRequest().getURI() + " " + startTime);
            exchange.getAttributes().put(REQUEST_START_TIME, System.currentTimeMillis());

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                long endTime = System.currentTimeMillis();
                System.out.println("请求结束：" + exchange.getRequest().getURI() + " "  + endTime);
                System.out.println("请求 " + exchange.getRequest().getURI() + " 耗时：" + (endTime - (long)exchange.getAttribute(REQUEST_START_TIME)) + " hs");
            }));

        };
    }

    public static class Config {
        private String requestStartTime;

        public Config() {
        }

        public String getRequestStartTime() {
            return this.requestStartTime;
        }

        public void setRequestStartTime(String requestStartTime) {
            this.requestStartTime = requestStartTime;
        }
    }
}
