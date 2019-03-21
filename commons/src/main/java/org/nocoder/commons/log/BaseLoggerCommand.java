package org.nocoder.commons.log;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.aspectj.lang.Signature;
import org.nocoder.commons.util.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

/**
 * Created by wangwenqiang on 2018/8/3.
 */
public class BaseLoggerCommand implements Command {

    private Logger LOG = LoggerFactory.getLogger(BaseLoggerCommand.class);

    @Override
    public boolean execute(Context context) throws Exception {
        return execute(context, null);
    }

    protected boolean execute(Context context, HttpServletRequest request) throws Exception {
        Signature signature = (Signature) context.get(BaseServiceInterceptor.METHOD_SIGNATURE);
        ThreadLocal<Long> startTime = (ThreadLocal<Long>) context.get(BaseServiceInterceptor.START_TIME);
        StringBuilder logInfo = new StringBuilder();
        Long callStartTime = System.currentTimeMillis();
        try {
            String[] threadTemp = Thread.currentThread().getName().split("-");
            String timeStr = new SimpleDateFormat("HHmmssSSS").format(new Date(callStartTime));
            //拼接请求标识,时间(时分秒毫秒)+线程号
            logInfo.append("[").append(timeStr).append("exec").append(threadTemp[threadTemp.length - 1]).append("]开始");
            if (request != null) {
                logInfo.append(",IP:" + RequestUtil.getRemoteIp(request)); //调用者ip
            }
            logInfo.append(",调用方法:" + signature.getDeclaringTypeName() + "." + signature.getName());//调用接口
            Object[] args = (Object[]) context.get(BaseServiceInterceptor.REQUEST_ARGS);
            logInfo.append(",请求参数:" + parseArgsJsonString(args)); //请求参数
            LOG.info(logInfo.toString());
        } catch (Exception e) {
            LOG.warn("请求开始日志打印错误");
        }
        startTime.set(callStartTime);
        return false;
    }

    /**
     * @return 本机IP
     * @throws SocketException
     */
    public static String getRealIp() throws SocketException {
        String localip = null;// 本地IP，如果没有配置外网IP则返回它
        String netip = null;// 外网IP

        Enumeration<NetworkInterface> netInterfaces =
                NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        boolean finded = false;// 是否找到外网IP
        while (netInterfaces.hasMoreElements() && !finded) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> address = ni.getInetAddresses();
            while (address.hasMoreElements()) {
                ip = address.nextElement();
                if (!ip.isSiteLocalAddress()
                        && !ip.isLoopbackAddress()
                        && ip.getHostAddress().indexOf(":") == -1) {// 外网IP
                    netip = ip.getHostAddress();
                    finded = true;
                    break;
                } else if (ip.isSiteLocalAddress()
                        && !ip.isLoopbackAddress()
                        && ip.getHostAddress().indexOf(":") == -1) {// 内网IP
                    localip = ip.getHostAddress();
                }
            }
        }

        if (netip != null && !"".equals(netip)) {
            return netip;
        } else {
            return localip;
        }
    }

    private String parseArgsJsonString(Object[] args) {
        String jsonStr = "";
        for (Object arg : args) {
            if (arg == null || arg instanceof ServletRequest || arg instanceof ServletResponse) {
                continue;
            }
            if (arg instanceof String || arg instanceof JSONObject) {
                jsonStr = jsonStr + arg + ",";
            } else {
                try {
                    jsonStr = jsonStr + JSONObject.toJSONString(arg) + ",";
                } catch (Exception e) {
                    LOG.warn("cannot parse request.");
                }
            }
        }
        return jsonStr;
    }

}
