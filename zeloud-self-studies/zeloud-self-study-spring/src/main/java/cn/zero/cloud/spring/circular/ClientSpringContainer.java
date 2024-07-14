package cn.zero.cloud.spring.circular;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Spring 容器测试类
 *
 * @author Xisun Wang
 * @since 2024/7/13 21:16
 */
public class ClientSpringContainer {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        A a = context.getBean("a", A.class);
        B b = context.getBean("b", B.class);
    }
}
