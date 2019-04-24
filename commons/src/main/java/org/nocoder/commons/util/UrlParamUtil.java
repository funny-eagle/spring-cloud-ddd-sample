package org.nocoder.commons.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;

public class UrlParamUtil {
    public UrlParamUtil() {
    }

    public static String getParamFromUrl(HttpServletRequest request, String paramName) {
        String paramStr = request.getQueryString();
        if (StringUtils.isBlank(paramStr)) {
            return null;
        } else {
            String[] requestParams = paramStr.split("&");

            for(int i = 0; i < requestParams.length; ++i) {
                String p = requestParams[i];
                int index = p.indexOf("=");
                if (index != -1 && p.substring(0, index).equals(paramName)) {
                    String value = p.substring(index + 1);
                    if (StringUtils.isNotBlank(value)) {
                        return value;
                    }

                    return null;
                }
            }

            return null;
        }
    }

    public static Map<String, String> getAllParamFromUrl(HttpServletRequest request) {
        String paramStr = request.getQueryString();
        Map<String, String> result = new HashMap();
        if (StringUtils.isBlank(paramStr)) {
            return result;
        } else {
            String[] requestParams = paramStr.split("&");

            for(int i = 0; i < requestParams.length; ++i) {
                String p = requestParams[i];
                int index = p.indexOf("=");
                if (index != -1) {
                    String key = p.substring(0, index);
                    String value = p.substring(index + 1);
                    if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
                        result.put(key, value);
                    }
                }
            }

            return result;
        }
    }

    public static String getUrlParamsString(Map<String, Object> webParams, HttpServletRequest request) {
        return getUrlParamsString(webParams, request.getQueryString());
    }

    public static String getUrlParamsString(Map<String, Object> webParams, String requestParam) {
        if ((webParams == null || webParams.size() == 0) && StringUtils.isEmpty(requestParam)) {
            return null;
        } else {
            if (StringUtils.isNotEmpty(requestParam) && webParams != null && webParams.size() > 0) {
                String[] requestParams = requestParam.split("&");
                requestParam = null;

                for(int i = 0; i < requestParams.length; ++i) {
                    String[] p = requestParams[i].split("=");
                    if (!webParams.keySet().contains(p[0])) {
                        if (StringUtils.isEmpty(requestParam)) {
                            requestParam = requestParams[i];
                        } else {
                            requestParam = requestParam + "&" + requestParams[i];
                        }
                    }
                }
            }

            String param = null;

            try {
                param = StringUtils.isEmpty(requestParam) ? null : URLDecoder.decode(requestParam, "UTF-8");
                if (webParams == null || webParams.size() == 0) {
                    return param;
                }

                Iterator var8 = webParams.keySet().iterator();

                while(var8.hasNext()) {
                    String key = (String)var8.next();
                    Object value = webParams.get(key);
                    if (value != null && !StringUtils.isBlank(String.valueOf(value))) {
                        if (StringUtils.isEmpty(param)) {
                            param = key + "=" + value;
                        } else {
                            param = param + "&" + key + "=" + value;
                        }
                    }
                }
            } catch (UnsupportedEncodingException var6) {
                var6.printStackTrace();
            }

            return param;
        }
    }
}
