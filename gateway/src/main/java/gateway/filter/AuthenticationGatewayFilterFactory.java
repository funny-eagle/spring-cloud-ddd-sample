package gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;

/**
 * 鉴权过滤器
 */
public class AuthenticationGatewayFilterFactory implements GatewayFilterFactory {

    @Override
    public GatewayFilter apply(Object config) {

        // 根据路径判断是否需要验证接口访问权

//        String token = HttpHeaderUtil.getToken();
//        if (token == null || !redisService.exists(RedisKey.LIBRARY_USER_LOGIN_TOKEN + token)){
//            // token 为空 或者 redis不存在token
//            throw  new ClientAuthenticationException(ExceptionConstants.LIBRARY_USER_LOGIN_STATUS_TIMEOUT,"登录超时,请重新登录");
//        }else{
//            //更新token及当前用户信息的过期时间
//            currLoginUserUtil.refreshExistTime(60 * 30);
//        }


        return null;
    }

    public static class Config{
        private String name;

        public Config(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
