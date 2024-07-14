package cn.zero.cloud.spring.circular.setter;

/**
 * @author Xisun Wang
 * @since 2024/7/13 14:39
 */
public class ServiceBB {
    private ServiceAA serviceAA;

    public void setServiceAA(ServiceAA serviceAA) {
        this.serviceAA = serviceAA;
        System.out.println("ServiceBB 中设置了 ServiceAA");
    }
}
