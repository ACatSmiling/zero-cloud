package cn.zero.cloud.component.exception.core.factory;

import cn.zero.cloud.component.exception.core.constants.ExceptionConstants;
import cn.zero.cloud.component.exception.core.resolver.impl.GlobalExceptionResolver;
import cn.zero.cloud.component.exception.core.resolver.impl.IllegalStateExceptionResolver;
import cn.zero.cloud.component.exception.core.resolver.impl.RestResponseExceptionResolver;
import cn.zero.cloud.component.exception.core.type.impl.ZeloudRestResponseException;
import cn.zero.cloud.component.exception.core.resolver.impl.IllegalArgumentExceptionResolver;
import cn.zero.cloud.component.exception.core.resolver.AbstractExceptionResolver;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Xisun Wang
 * @since 2024/3/26 16:00
 */
public final class ExceptionResolverFactory {

    private ExceptionResolverFactory() {
        throw new IllegalStateException(ExceptionConstants.UTILITY_CLASS);
    }

    private static final Map<Class<? extends Exception>, Supplier<AbstractExceptionResolver>> EXCEPTION_RESOLVERS = new HashMap<>();

    static {
        EXCEPTION_RESOLVERS.put(ZeloudRestResponseException.class, RestResponseExceptionResolver::new);
        EXCEPTION_RESOLVERS.put(IllegalArgumentException.class, IllegalArgumentExceptionResolver::new);
        EXCEPTION_RESOLVERS.put(IllegalStateException.class, IllegalStateExceptionResolver::new);
    }

    /**
     * 注册一个异常
     *
     * @param exceptionClass   异常
     * @param resolverSupplier 异常处理器
     */
    public static void registerException(Class<? extends Exception> exceptionClass, Supplier<AbstractExceptionResolver> resolverSupplier) {
        EXCEPTION_RESOLVERS.put(exceptionClass, resolverSupplier);
    }

    /**
     * 只处理注册的异常
     *
     * @param e 待处理的异常
     * @return 匹配的异常处理器
     */
    public static AbstractExceptionResolver getExceptionResolverInstance(Exception e) {
        return EXCEPTION_RESOLVERS.getOrDefault(e.getClass(), GlobalExceptionResolver::new).get();
    }

    /**
     * 处理注册的异常，及异常的子类
     * 注意：检查整个映射以寻找超类可能会根据映射的大小和继承层次结构的深度而影响性能
     *
     * @param e 待处理的异常
     * @return 匹配的异常处理器
     */
    public static AbstractExceptionResolver getIncludeExceptionResolverInstance(Exception e) {
        Class<?> exceptionClass = e.getClass();

        // 检查是否有异常对应的处理器
        Supplier<AbstractExceptionResolver> resolverSupplier = EXCEPTION_RESOLVERS.get(exceptionClass);
        if (resolverSupplier != null) {
            return resolverSupplier.get();
        }

        // 检查是否有异常的任何超类对应的处理器
        for (Map.Entry<Class<? extends Exception>, Supplier<AbstractExceptionResolver>> entry : EXCEPTION_RESOLVERS.entrySet()) {
            if (entry.getKey().isAssignableFrom(exceptionClass)) {
                return entry.getValue().get();
            }
        }

        // 如果没有找到对应的处理器，返回默认值
        return new GlobalExceptionResolver();
    }

    public static void main(String[] args) {
        System.out.println(Object.class.isAssignableFrom(Integer.class)); // true，因为 Integer 是 Object 的子类
        System.out.println(Integer.class.isAssignableFrom(Object.class));// false，因为 Object 不是 Integer 的子类
        System.out.println(Object.class.isAssignableFrom(Object.class));// true，因为是同一个类型
    }
}
