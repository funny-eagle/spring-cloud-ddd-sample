package org.nocoder.commons.log;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.impl.ChainBase;
import org.apache.commons.chain.impl.ContextBase;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 2018/8/3.
 */
public abstract class BaseServiceInterceptor {
    private Logger LOG = LoggerFactory.getLogger(BaseServiceInterceptor.class);
    public static final String METHOD_SIGNATURE = "signature";
    public static final String REQUEST_IDENTIFICATION = "identification";
    public static final String START_TIME = "startTime";
    public static final String REQUEST_ARGS = "args";
    private ThreadLocal<Long> startTime = new ThreadLocal<Long>();
    protected List<Command> commandList = new ArrayList<Command>();

    public abstract void requestLog();

    public abstract void doBefore(JoinPoint joinPoint) throws Throwable;

    public abstract void doAfterReturning(Object ret) throws Throwable;

    public abstract void doAfterReturning() throws Throwable;

    /**
     * 在doBefore方法里需要执行
     *
     * @param joinPoint
     * @throws Throwable
     */
    protected void before(JoinPoint joinPoint) throws Throwable {
        Map contextMap = new HashMap();
        contextMap.put(START_TIME, startTime);
        contextMap.put(METHOD_SIGNATURE, joinPoint.getSignature());
        contextMap.put(REQUEST_ARGS, joinPoint.getArgs());
        Context context = new ContextBase(contextMap);

        ChainBase chainBase = new ChainBase(commandList);
        chainBase.execute(context);
    }

    protected void logAfterDeal() {
        try {
            StringBuilder logInfo = new StringBuilder();
            String[] threadTemp = Thread.currentThread().getName().split("-");
            long time = startTime.get();
            String timeStr = new SimpleDateFormat("HHmmssSSS").format(new Date(time));
            //拼接请求标识,时间(时分秒毫秒)+线程号
            logInfo.append("[").append(timeStr).append("exec").append(threadTemp[threadTemp.length - 1]).append("]结束");
            logInfo.append(",处理耗时: " + (System.currentTimeMillis() - time) + "毫秒");
            LOG.info(logInfo.toString());
        } catch (Exception e) {
            LOG.warn("请求结束日志打印错误");
        }
    }
}
