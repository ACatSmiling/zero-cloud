package cn.zero.cloud.distributed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Xisun Wang
 * @since 2024/6/21 22:17
 */
@SpringBootApplication
public class RedisDistributedLockApplication {
    public static void main(String[] args) {
        SpringApplication.run(RedisDistributedLockApplication.class, args);
    }
}
