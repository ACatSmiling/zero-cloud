package cn.zero.cloud.component.telemetry.core.constants;

/**
 * @author Xisun Wang
 * @since 2024/3/14 16:38
 */
public class TelemetryConstants {
    public static final String AOP_METHOD_IS_NULL = "AOP Method is null!";

    public static final String MULTIPLE_VALUES = "Multiple values are not recommended!";

    public static final String MULTIPLE_MAPPINGS = "Multiple mappings are not recommended!";

    public static final String OBJECT_TYPE_EXAMPLE = "OBJECT_TYPE_EXAMPLE";

    public static final String UUID_KEY_EXAMPLE = "UUID_KEY_EXAMPLE";

    public static final String VERB_CREATE = "CREATE";

    public static final String VERB_UPDATE = "UPDATE";

    public static final String VERB_DELETE = "DELETE";

    public static final String VERB_GET = "GET";

    private interface NameAware {
        String getName();
    }

    @Deprecated // 取消使用枚举类，改用 String，因为子类中无法继承，不便于子 module 中扩展
    public enum ModuleName implements NameAware {
        DEFAULT_BLANK(""),

        TEST_API("TEST_API"),

        COMMON_API("COMMON_API"),

        KAFKA_API("KAFKA_API"),

        ZELOUD_BUSINESS_SERVICE("ZELOUD_BUSINESS_SERVICE"),

        ;

        private final String name;

        ModuleName(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }

    @Deprecated // 取消使用枚举类，改用 String，因为子类中无法继承，不便于子 module 中扩展
    public enum MetricName implements NameAware {
        DEFAULT_BLANK(""),

        TEST_METRIC("TEST_METRIC"),

        COMMON_METRIC("COMMON_METRIC"),

        KAFKA_METRIC("KAFKA_METRIC"),

        THREAD_POOL_TASK_EXECUTE("THREAD_POOL_TASK_EXECUTE"),

        THREAD_POOL_WORK_STATE("THREAD_POOL_WORK_STATE"),

        COMPONENT_CACHE_INTEGRATION("COMPONENT_CACHE_INTEGRATION"),

        ;

        private final String name;

        MetricName(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }

    @Deprecated // 取消使用枚举类，改用 String，因为子类中无法继承，不便于子 module 中扩展
    public enum FeatureName implements NameAware {
        DEFAULT_BLANK(""),

        TEST_FEATURE("TEST_FEATURE"),

        COMMON_FEATURE("COMMON_FEATURE"),

        KAFKA_FEATURE("KAFKA_FEATURE"),

        THREAD_POOL_TASK_EXECUTE("THREAD_POOL_TASK_EXECUTE"),

        THREAD_POOL_WORK_STATE("THREAD_POOL_WORK_STATE"),

        COMPONENT_CACHE("COMPONENT_CACHE"),

        ;

        private final String name;

        FeatureName(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }

    @Deprecated // 取消使用枚举类，改用 String，因为子类中无法继承，不便于子 module 中扩展
    public enum Verb implements NameAware {
        DEFAULT_BLANK(""),

        CREATE("CREATE"),

        UPDATE("UPDATE"),

        DELETE("DELETE"),

        SELECT("SELECT"),

        GET("GET"),

        ;

        private final String name;

        Verb(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }

    @Deprecated // 取消使用枚举类，改用 String，因为子类中无法继承，不便于子 module 中扩展
    public enum ObjectType implements NameAware {
        DEFAULT_BLANK(""),

        TEST_OBJECT("TEST_OBJECT"),

        COMMON_OBJECT("COMMON_OBJECT"),

        KAFKA_OBJECT("KAFKA_OBJECT"),

        COMPONENT_CACHE_OBJECT("COMPONENT_CACHE_OBJECT"),

        ;

        private final String name;

        ObjectType(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }

    public enum ProcessResult implements NameAware {
        SUCCESS("SUCCESS"),

        FAILURE("FAILURE"),

        IGNORE("IGNORE"),

        ;

        private final String name;

        ProcessResult(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }
}
