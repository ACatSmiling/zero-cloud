package cn.zero.cloud.spring.circular.setter;

/**
 * 演示 setter 注入
 *
 * @author Xisun Wang
 * @since 2024/7/13 14:42
 */
public class SetterInjection {
    public static void main(String[] args) {
        // 创建 ServiceAA
        ServiceAA a = new ServiceAA();
        // 创建 ServiceBB
        ServiceBB b = new ServiceBB();

        // 通过 setter 注入，ServiceAA 和 ServiceBB 能够正常创建

        // 将 ServiceBB 注入到 ServiceAA 中
        a.setServiceBB(b);
        // 将 ServiceAA 注入到 ServiceBB 中
        b.setServiceAA(a);
    }
}
