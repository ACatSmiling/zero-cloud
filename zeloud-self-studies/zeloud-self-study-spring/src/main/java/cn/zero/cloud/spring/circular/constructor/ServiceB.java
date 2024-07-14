package cn.zero.cloud.spring.circular.constructor;

/**
 * @author Xisun Wang
 * @since 2024/7/13 14:30
 */
public class ServiceB {
    private ServiceA serviceA;

    public ServiceB(ServiceA serviceA) {
        this.serviceA = serviceA;
    }
}
