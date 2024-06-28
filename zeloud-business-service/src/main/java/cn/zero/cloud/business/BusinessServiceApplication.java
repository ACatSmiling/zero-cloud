package cn.zero.cloud.business;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Xisun Wang
 * @since 2024/6/28 19:57
 */
@SpringBootApplication
public class BusinessServiceApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(BusinessServiceApplication.class, args);

        String beanName = "SendNotificationExecutor";
        if (applicationContext.containsBean(beanName)) {
            Object myBean = applicationContext.getBean(beanName);
            // 现在你可以对 myBean 做任何你需要的操作
            System.out.println("Bean with name '" + beanName + "' is of type: " + myBean.getClass().getSimpleName());
        } else {
            System.out.println("No bean with name '" + beanName + "' is defined.");
        }
    }
}
