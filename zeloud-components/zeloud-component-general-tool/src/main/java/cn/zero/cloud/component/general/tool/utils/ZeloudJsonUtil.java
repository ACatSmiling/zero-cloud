package cn.zero.cloud.component.general.tool.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 序列化：Serialization，是指将 Java 对象转换为 JSON 格式的字符串的过程
 * 反序列化：Deserialization，是指将 JSON 格式的字符串转换回 Java 对象的过程
 * <a href="https://github.com/FasterXML/jackson">FasterXML/jackson</a>
 *
 * @author Xisun Wang
 * @since 2024/3/21 11:12
 */
public class ZeloudJsonUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZeloudJsonUtil.class);

    private static final Version VERSION = new Version(1, 0, 0, null, "cn.zero.cloud", "platform");

    /**
     * 默认的 ObjectMapper
     */
    private static final ObjectMapper OBJECT_MAPPER = newObjectMapper("Base");

    /**
     * 默认的带格式化功能的 ObjectWriter
     */
    private static final ObjectWriter PRETTY_OBJECT_WRITER;

    static {
        PRETTY_OBJECT_WRITER = getPrettyWriter();
    }

    private ZeloudJsonUtil() {
        throw new IllegalStateException("Utility class!");
    }

    private static ObjectWriter getPrettyWriter() {
        // 创建一个 ObjectWriter，配置为格式化输出 json
        return OBJECT_MAPPER.writer(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * 对象转 json，使用默认的 ObjectMapper
     *
     * @param object 待处理的对象
     * @return 转换后的 json
     */
    public static String serializeToJson(Object object) {
        return serializeToJson(object, OBJECT_MAPPER);
    }

    /**
     * 对象转 json，使用默认的 ObjectMapper，并格式化 json
     *
     * @param object 待处理的对象
     * @return 转换并格式化后的 json
     */
    public static String serializeToJsonPretty(Object object) {
        return serializeToJson(object, PRETTY_OBJECT_WRITER);
    }

    /**
     * 对象转 json，使用自定义的 ObjectMapper
     *
     * @param object 待处理的对象
     * @param mapper 指定转换用的 ObjectMapper
     * @return 转换后的json
     */
    public static String serializeToJson(Object object, ObjectMapper mapper) {
        return serializeToJson(object, mapper.writer());
    }

    /**
     * 对象转 json，使用自定义的 ObjectMapper，并格式化 json
     *
     * @param object 待处理的对象
     * @param mapper 指定转换用的 ObjectMapper
     * @return 转换并格式化后的 json
     */
    public static String serializeToJsonPretty(Object object, ObjectMapper mapper) {
        ObjectWriter writer = mapper.writer(SerializationFeature.INDENT_OUTPUT);
        return serializeToJson(object, writer);
    }

    /**
     * 对象转 json
     *
     * @param object 待处理的对象
     * @param writer 指定转换用的 ObjectWriter
     * @return 转换后的 json
     */
    public static String serializeToJson(Object object, ObjectWriter writer) {
        try {
            return writer.writeValueAsString(object);
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to Serialize Object [" + object + "] to Json.", e);
        }
    }

    /**
     * json 转对象，使用默认的 ObjectMapper，适用于目标类型不包含泛型参数时
     *
     * @param json  待处理的 json
     * @param clazz 待转换的 class
     * @param <T>   泛型
     * @return 转换后的对象
     */
    public static <T> T deserializeToClassType(String json, Class<T> clazz) {
        return deserializeToClassType(json, clazz, OBJECT_MAPPER);
    }

    /**
     * json 转对象，使用自定义的 ObjectMapper，适用于目标类型不包含泛型参数时
     *
     * @param json   待处理的 json
     * @param clazz  待转换的 class
     * @param mapper 指定转换用的 ObjectMapper
     * @param <T>    泛型
     * @return 转换后的对象
     */
    public static <T> T deserializeToClassType(String json, Class<T> clazz, ObjectMapper mapper) {
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to Deserialize Json [" + json + "] to Class Object.", e);
        }
    }

    /**
     * json 转对象，使用默认的 ObjectMapper，适用于目标类型包含泛型参数时，如 List<MyClass> 或 Map<String, MyClass>
     *
     * @param json    待处理的 json
     * @param typeRef 待转换的目标类型
     * @param <T>     泛型
     * @return 转换后的对象
     */
    public static <T> T deserializeToReferenceType(String json, TypeReference<T> typeRef) {
        return deserializeToReferenceType(json, typeRef, OBJECT_MAPPER);
    }

    /**
     * json 转对象，使用自定义的 ObjectMapper，适用于目标类型包含泛型参数时，如 List<MyClass> 或 Map<String, MyClass>
     *
     * @param json    待处理的 json
     * @param typeRef 待转换的目标类型
     * @param mapper  指定转换用的 ObjectMapper
     * @param <T>     泛型
     * @return 转换后的对象
     */
    public static <T> T deserializeToReferenceType(String json, TypeReference<T> typeRef, ObjectMapper mapper) {
        try {
            return mapper.readValue(json, typeRef);
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to Deserialize Json [" + json + "] to Reference Object.", e);
        }
    }

    /**
     * 对象转 json，使用默认的 ObjectMapper，并指定字符集
     *
     * @param object             待处理的对象
     * @param currentCharsetName 当前字符集
     * @param targetCharsetName  目标字符集
     * @return 转换后的json
     */
    public static String serializeToJsonWithCharSet(Object object, String currentCharsetName, String targetCharsetName) {
        try {
            return new String(serializeToJson(object).getBytes(currentCharsetName), targetCharsetName);
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to Serialize Object [" + object + "] to Json with Charset [" + targetCharsetName + "].", e);
        }
    }

    /**
     * json 转对象，使用默认的 ObjectMapper，并指定字符集，适用于目标类型不包含泛型参数时
     *
     * @param json               待处理的 json
     * @param currentCharsetName 当前字符集
     * @param targetCharsetName  目标字符集
     * @param clazz              待转换的 class
     * @param <T>                泛型
     * @return 转换后的对象
     */
    public static <T> T deserializeToClassTypeWithCharSet(String json, String currentCharsetName, String targetCharsetName, Class<T> clazz) {
        try {
            return deserializeToClassType(new String(json.getBytes(currentCharsetName), targetCharsetName), clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to Deserialize Json [" + json + "] to Class Object with Charset [" + targetCharsetName + "].", e);
        }
    }

    /**
     * json 转对象，使用默认的 ObjectMapper，并指定字符集，适用于目标类型包含泛型参数时，如 List<MyClass> 或 Map<String, MyClass>
     *
     * @param json               待处理的 json
     * @param currentCharsetName 当前字符集
     * @param targetCharsetName  目标字符集
     * @param typeRef            待转换的目标类型
     * @param <T>                泛型
     * @return 转换后的对象
     */
    public static <T> T deserializeToReferenceTypeWithCharSet(String json, String currentCharsetName, String targetCharsetName, TypeReference<T> typeRef) {
        try {
            return deserializeToReferenceType(new String(json.getBytes(currentCharsetName), targetCharsetName), typeRef);
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to Deserialize Json [" + json + "] to Reference Object with Charset [" + targetCharsetName + "].", e);
        }
    }

    /**
     * 创建一个 ObjectMapper，并按需添加配置
     *
     * @param name SimpleModule 的名称，唯一性
     * @return ObjectMapper
     */
    private static ObjectMapper newObjectMapper(String name) {
        ObjectMapper mapper = new ObjectMapper();

        // 序列化时的可见性规则：忽略 getter 和 setter 方法等，只使用对象中的所有字段进行 json 的序列化和反序列化
        mapper.setVisibility(mapper.getVisibilityChecker().with(JsonAutoDetect.Visibility.NONE).withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        // 序列化时的包含规则：JsonInclude.Include.NON_NULL 告诉 ObjectMapper 只序列化非 null 值的属性，这意味着任何具有 null 值的属性都将从序列化的 json 中省略
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 禁用序列化空 bean 时抛出异常的特性，空对象将被序列化为一个空的 json 对象 {}（默认情况下，如果一个类没有任何公共属性或公共 getter 方法，尝试序列化它会导致异常）
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 配置日期和时间值的序列化方式。默认情况下，ObjectMapper 会将日期转换为时间戳。设置这个特性为 false 会改变行为，使得日期和时间以它们的 ISO-8601 字符串表示形式序列化，这通常是更可读的格式
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 配置 ObjectMapper 在反序列化过程中的行为。默认情况下，如果 json 中有未在目标类中定义的属性，反序列化会失败并抛出一个异常。设置这个特性为 false 会忽略未知属性，允许 json 中包含额外的字段，而不会导致反序列化失败
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 创建一个 Jackson SimpleModule，并向该模块注册几个自定义序列化器和反序列化器，用于处理不同类型的日期时间对象
        SimpleModule module = new SimpleModule(name, VERSION);
        // 1. 添加自定义序列化器，序列化 java.util.Date 对象，UTC 格式
        module.addSerializer(new StdSerializer<>(Date.class) {
            @Override
            public void serialize(Date date, JsonGenerator jg, SerializerProvider sp) throws IOException {
                jg.writeString(ZeloudDateUtil.formatDateWithUtc(date));
            }
        });
        // 2. 添加自定义序列化器，序列化 java.time.Instant 对象，UTC 格式
        module.addSerializer(new StdSerializer<>(Instant.class) {
            @Override
            public void serialize(Instant instant, JsonGenerator jg, SerializerProvider sp) throws IOException {
                jg.writeString(ZeloudDateUtil.formatInstantWithUtc(instant));
            }
        });
        // 3. 添加自定义序列化器，序列化 java.time.LocalDateTime 对象，UTC 格式
        module.addSerializer(new StdSerializer<>(LocalDateTime.class) {
            @Override
            public void serialize(LocalDateTime localDateTime, JsonGenerator jg, SerializerProvider sp) throws IOException {
                jg.writeString(ZeloudDateUtil.formatLocalDateTimeWithUtc(localDateTime));
            }
        });
        // 4. 添加自定义序列化器，序列化 java.time.ZonedDateTime 对象，UTC 格式
        module.addSerializer(new StdSerializer<>(ZonedDateTime.class) {
            @Override
            public void serialize(ZonedDateTime zonedDateTime, JsonGenerator jg, SerializerProvider sp) throws IOException {
                jg.writeString(ZeloudDateUtil.formatTimeWithUtc(zonedDateTime));
            }
        });
        // 5. 添加自定义反序列化器，反序列化 java.util.Date 对象，UTC 格式
        module.addDeserializer(Date.class, new StdScalarDeserializer<>(Date.class) {
            @Override
            public Date deserialize(JsonParser jp, DeserializationContext dc) throws IOException {
                try {
                    ZonedDateTime zonedDateTime = ZeloudDateUtil.parseStringForUtc(jp.getText());
                    return Date.from(zonedDateTime.toInstant());
                } catch (IllegalArgumentException e) {
                    throw JsonMappingException.from(jp, "Unable to parse date: " + e.getMessage());
                }
            }
        });
        // 6. 添加自定义反序列化器，反序列化 java.time.Instant 对象，UTC 格式
        module.addDeserializer(Instant.class, new StdScalarDeserializer<>(Instant.class) {
            @Override
            public Instant deserialize(JsonParser jp, DeserializationContext dc) throws IOException {
                try {
                    ZonedDateTime zonedDateTime = ZeloudDateUtil.parseStringForUtc(jp.getText());
                    return zonedDateTime.toInstant();
                } catch (IllegalArgumentException e) {
                    throw JsonMappingException.from(jp, "Unable to parse date: " + e.getMessage());
                }
            }
        });
        // 7. 添加自定义反序列化器，反序列化 java.time.LocalDateTime 对象，UTC 格式
        module.addDeserializer(LocalDateTime.class, new StdScalarDeserializer<>(LocalDateTime.class) {
            @Override
            public LocalDateTime deserialize(JsonParser jp, DeserializationContext dc) throws IOException {
                try {
                    ZonedDateTime zonedDateTime = ZeloudDateUtil.parseStringForUtc(jp.getText());
                    return zonedDateTime.toLocalDateTime();
                } catch (IllegalArgumentException e) {
                    throw JsonMappingException.from(jp, "Unable to parse date: " + e.getMessage());
                }
            }
        });
        // 8. 添加自定义反序列化器，反序列化 java.time.ZonedDateTime 对象，UTC 格式
        module.addDeserializer(ZonedDateTime.class, new StdScalarDeserializer<>(ZonedDateTime.class) {
            @Override
            public ZonedDateTime deserialize(JsonParser jp, DeserializationContext dc) throws IOException {
                try {
                    return ZeloudDateUtil.parseStringForUtc(jp.getText());
                } catch (IllegalArgumentException e) {
                    throw JsonMappingException.from(jp, "Unable to parse date: " + e.getMessage());
                }
            }
        });
        mapper.registerModule(module);
        return mapper;
    }

    public static void main(String[] args) {
        long epoch = 1712457290111L;
        JsonDemo jsonDemo = new JsonDemo();
        jsonDemo.setTimeStr("2024-04-07T10:34:50.111+0800");
        jsonDemo.setDate(new Date(epoch));
        jsonDemo.setInstant(Instant.ofEpochMilli(epoch));
        jsonDemo.setLocalDateTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(epoch), ZoneId.systemDefault()));
        jsonDemo.setZonedDateTime(ZonedDateTime.ofInstant(Instant.ofEpochMilli(epoch), ZoneId.systemDefault()));
        System.out.println(ZeloudJsonUtil.serializeToJson(jsonDemo));// {"timeStr":"2024-04-07T10:34:50.111+0800","date":"2024-04-07T10:34:50.111+0800","instant":"2024-04-07T10:34:50.111+0800","localDateTime":"2024-04-07T10:34:50.111+0800","zonedDateTime":"2024-04-07T10:34:50.111+0800"}
        System.out.println(ZeloudJsonUtil.serializeToJsonPretty(jsonDemo));// 与上面的输出对比，是格式化后的 json

        String jsonStr = "{\"timeStr\":\"2024-04-07T10:34:50.111+0800\",\"date\":\"2024-04-07T10:34:50.111+0800\",\"instant\":\"2024-04-07T10:34:50.111+0800\",\"localDateTime\":\"2024-04-07T10:34:50.111+0800\",\"zonedDateTime\":\"2024-04-07T10:34:50.111+0800\"}";
        System.out.println(ZeloudJsonUtil.deserializeToClassType(jsonStr, JsonDemo.class));// JsonDemo{timeStr='2024-04-07T10:34:50.111+0800', date=Sun Apr 07 10:34:50 CST 2024, instant=2024-04-07T02:34:50.111Z, localDateTime=2024-04-07T10:34:50.111, zonedDateTime=2024-04-07T10:34:50.111+08:00}

        System.out.println(ZeloudJsonUtil.serializeToJson("abc:a"));// "abc:a"

        String jsonStr2 = "{\"timeStr\":\"2024-04-07T10:34:50.111+0800\",\"date\":\"2024-04-10T04:55:24Z\",\"instant\":\"2024-04-07T10:34:50.111+0800\",\"localDateTime\":\"2024-04-07T10:34:50.111+0800\",\"zonedDateTime\":\"2024-04-07T10:34:50.111+0800\"}";
        System.out.println(ZeloudJsonUtil.deserializeToClassType(jsonStr2, JsonDemo.class));


        System.out.println("----------------------------------------------");
        long epoch2 = 1712457390111L;
        JsonDemo jsonDemo2 = new JsonDemo();
        jsonDemo2.setTimeStr("2024-04-08T10:34:50.111+0800");
        jsonDemo2.setDate(new Date(epoch2));
        jsonDemo2.setInstant(Instant.ofEpochMilli(epoch2));
        jsonDemo2.setLocalDateTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(epoch2), ZoneId.systemDefault()));
        jsonDemo2.setZonedDateTime(ZonedDateTime.ofInstant(Instant.ofEpochMilli(epoch2), ZoneId.systemDefault()));
        List<JsonDemo> jsonDemos = new ArrayList<>();
        jsonDemos.add(jsonDemo);
        jsonDemos.add(jsonDemo2);
        String jsons = ZeloudJsonUtil.serializeToJson(jsonDemos);
        System.out.println(jsons);// [{"timeStr":"2024-04-07T10:34:50.111+0800","date":"2024-04-07T10:34:50.111+0800","instant":"2024-04-07T10:34:50.111+0800","localDateTime":"2024-04-07T10:34:50.111+0800","zonedDateTime":"2024-04-07T10:34:50.111+0800"},{"timeStr":"2024-04-08T10:34:50.111+0800","date":"2024-04-07T10:36:30.111+0800","instant":"2024-04-07T10:36:30.111+0800","localDateTime":"2024-04-07T10:36:30.111+0800","zonedDateTime":"2024-04-07T10:36:30.111+0800"}]
        List<JsonDemo> object = ZeloudJsonUtil.deserializeToReferenceType(jsons, new TypeReference<>() {
        });
        System.out.println(object);// [JsonDemo{timeStr='2024-04-07T10:34:50.111+0800', date=Sun Apr 07 10:34:50 CST 2024, instant=2024-04-07T02:34:50.111Z, localDateTime=2024-04-07T10:34:50.111, zonedDateTime=2024-04-07T10:34:50.111+08:00}, JsonDemo{timeStr='2024-04-08T10:34:50.111+0800', date=Sun Apr 07 10:36:30 CST 2024, instant=2024-04-07T02:36:30.111Z, localDateTime=2024-04-07T10:36:30.111, zonedDateTime=2024-04-07T10:36:30.111+08:00}]

        LOGGER.info("aabb");
    }

    private static class JsonDemo {
        private String timeStr;

        private Date date;

        private Instant instant;

        private LocalDateTime localDateTime;

        private ZonedDateTime zonedDateTime;

        public String getTimeStr() {
            return timeStr;
        }

        public void setTimeStr(String timeStr) {
            this.timeStr = timeStr;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Instant getInstant() {
            return instant;
        }

        public void setInstant(Instant instant) {
            this.instant = instant;
        }

        public LocalDateTime getLocalDateTime() {
            return localDateTime;
        }

        public void setLocalDateTime(LocalDateTime localDateTime) {
            this.localDateTime = localDateTime;
        }

        public ZonedDateTime getZonedDateTime() {
            return zonedDateTime;
        }

        public void setZonedDateTime(ZonedDateTime zonedDateTime) {
            this.zonedDateTime = zonedDateTime;
        }

        @Override
        public String toString() {
            return "JsonDemo{" +
                    "timeStr='" + timeStr + '\'' +
                    ", date=" + date +
                    ", instant=" + instant +
                    ", localDateTime=" + localDateTime +
                    ", zonedDateTime=" + zonedDateTime +
                    '}';
        }
    }
}