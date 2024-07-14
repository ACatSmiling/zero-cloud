package cn.zero.cloud.spring.circular.constructor;

/**
 * 演示构造注入
 *
 * @author Xisun Wang
 * @since 2024/7/13 14:30
 */
public class ConstructorInjection {
    public static void main(String[] args) {
        // 通过构造注入，ServiceA 和 ServiceB 无法创建
        // new ServiceA(new ServiceB(new ServiceA()));
    }
}
