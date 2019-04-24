package org.nocoder.commons.distributelock;

import org.nocoder.commons.redis.IRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author yangjinlong
 * @date 2018-09-27
 */
public class DistributeLock implements Closeable {

    Logger logger = LoggerFactory.getLogger(DistributeLock.class);

    private DistributeLock() {

    }

    private IRedisService redisService;

    public DistributeLock(IRedisService redisService) {
        this.redisService = redisService;
    }

    /**
     * 尝试获取锁的间隔时间 50毫秒
     */
    private int tryInterval = 50;

    /**
     * 锁等待超时时间 10秒
     */
    private int timeoutMsecs = 10 * 1000;

    /**
     * 锁名
     */
    private String lockName;

    /**
     * 锁超时时长
     */
    private long lockTimeout;

    /**
     * 获取锁，成功则返回该锁的过期时间，失败则返回0；
     *
     * @param lockName 锁名
     * @param retry    获取锁失败后是否重试直到超时
     * @throws InterruptedException
     */
    public void getLock(String lockName, boolean retry) {
        this.lockName = lockName;
        long lockTimeout;
        if (!retry) {
            lockTimeout = System.currentTimeMillis() + 30000 + 1;
            int result = redisService.setNx(lockName, lockTimeout + "").intValue();
            if (result == 1) {
                this.lockTimeout = lockTimeout;
            }
            return;
        }

        //等待时间
        int waitTime = 0;
        while (waitTime <= timeoutMsecs) {
            lockTimeout = System.currentTimeMillis() + 30000 + 1;
            int result = redisService.setNx(lockName, lockTimeout + "").intValue();
            if (result == 1) {
                this.lockTimeout = lockTimeout;
                return;
            }

            //当前锁的过期时间
            String currentTimeStr = redisService.getStr(lockName);
            long currentLockTime = currentTimeStr == null ? 0 : Long.valueOf(currentTimeStr);
            //已过期
            if (currentLockTime == 0 || currentLockTime <= System.currentTimeMillis()) {
                //锁更新前的过期时间
                String oldLockTimeStr = redisService.getSet(lockName, lockTimeout + "");
                long oldLockTime = oldLockTimeStr == null ? 0 : Long.valueOf(oldLockTimeStr);
                if (oldLockTime == 0 || oldLockTime == currentLockTime) {
                    System.out.println(Thread.currentThread().getName() + currentTimeStr + "==" + oldLockTime);
                    this.lockTimeout = lockTimeout;
                    return;
                }
            }

            //停顿后重试
            try {
                Thread.sleep(tryInterval);
            } catch (InterruptedException e) {
                logger.error("{}", e);
            }
            waitTime = waitTime + tryInterval;
        }
    }

    /**
     * 解锁，如果是加锁者 则删除锁
     *
     * @param lockName
     * @param lockTimeout
     */

    public void releaseLock(String lockName, long lockTimeout) {
        String currentLockTimeStr = redisService.getStr(lockName);
        long currentLockTime = Long.valueOf(currentLockTimeStr == null ? "0" : currentLockTimeStr);
        if (lockTimeout != 0 && currentLockTime == lockTimeout) {
            //如果是加锁者 则删除锁
            redisService.removeObject(lockName);
        }
        logger.info("distribute lock {} has been released!", lockName);
    }

    @Override
    public void close() throws IOException {
        releaseLock(this.lockName, this.lockTimeout);
    }
}
