package cn.zero.cloud.spring.circular.setter;

/**
 * @author Xisun Wang
 * @since 2024/7/13 14:39
 */
public class ServiceAA {
    private ServiceBB serviceBB;

    public void setServiceBB(ServiceBB serviceBB) {
        this.serviceBB = serviceBB;
        System.out.println("ServiceAA 中设置了 ServiceBB");
    }
}
