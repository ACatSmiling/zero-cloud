package cn.zero.cloud.spring.circular.constructor;

/**
 * @author Xisun Wang
 * @since 2024/7/13 14:28
 */
public class ServiceA {
    private ServiceB serviceB;

    public ServiceA(ServiceB serviceB) {
        this.serviceB = serviceB;
    }
}
