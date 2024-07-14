package cn.zero.cloud.spring.circular;

/**
 * @author Xisun Wang
 * @since 2024/7/13 14:46
 */
public class A {
    private B b;

    public A() {
        System.out.println("Class A was created successfully");
    }

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }
}
