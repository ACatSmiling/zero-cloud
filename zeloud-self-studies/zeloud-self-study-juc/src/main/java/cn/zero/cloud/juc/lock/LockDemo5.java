package cn.zero.cloud.juc.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author Xisun Wang
 * @since 2024/6/8 10:47
 */
@Slf4j
public class LockDemo5 {
    private static class Phone {
        public static synchronized void sendEmail() {
            // sendEmail方法暂停3秒钟
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("thread {} send email", Thread.currentThread().getName());
        }

        public static synchronized void sendSms() {
            log.info("thread {} send sms", Thread.currentThread().getName());
        }
    }

    public static void main(String[] args) {
        new Thread(Phone::sendEmail, "a").start();

        // main线程暂停，主要是保证线程a先启动
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(Phone::sendSms, "b").start();
    }
}
