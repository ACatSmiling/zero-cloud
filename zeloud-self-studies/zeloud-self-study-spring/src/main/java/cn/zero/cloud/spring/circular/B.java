package cn.zero.cloud.spring.circular;

/**
 * @author Xisun Wang
 * @since 2024/7/13 14:46
 */
public class B {
    private A a;

    public B() {
        System.out.println("Class B was created successfully");
    }

    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }
}
