package cn.zero.cloud.spring.circular;

/**
 * Java SE 代码测试类
 *
 * @author Xisun Wang
 * @since 2024/7/13 14:49
 */
public class ClientCode {
    public static void main(String[] args) {
        A a = new A();
        B b = new B();

        a.setB(b);
        b.setA(a);
    }
}
