package org.nocoder.aggregation.controller;

import com.yuntu.commons.BaseResponse;
import com.yuntu.commons.util.UrlParamUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author YangJinlong
 */
public class BaseController {

    @Resource
    private RestTemplate restTemplate;

    protected BaseResponse rpc(String serviceName, String url, String method, Object jsonBody,
                               Map<String, Object> urlParams, HttpServletRequest request) {

        HttpHeaders headers = new HttpHeaders();
        if (StringUtils.isNotBlank(request.getHeader("token"))) {
            headers.add("token", request.getHeader("token"));
        }

        url = "http://" + serviceName + url;
        String requestParam = UrlParamUtil.getUrlParamsString(urlParams, request);
        if (requestParam != null) {
            url = url + "?" + requestParam;
        }

        HttpEntity<Object> httpEntity = new HttpEntity<>(jsonBody, headers);
        Validate.isTrue("GET".equals(method) || "POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method), "method is error");
        return restTemplate.exchange(url, HttpMethod.resolve(method), httpEntity, BaseResponse.class).getBody();
    }
}
