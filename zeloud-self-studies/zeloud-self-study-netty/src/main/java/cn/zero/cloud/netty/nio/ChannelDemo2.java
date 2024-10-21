package cn.zero.cloud.netty.nio;

import cn.zero.cloud.netty.util.ByteBufferUtil;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author Xisun Wang
 * @since 2024/10/8 14:12
 */
public class ChannelDemo2 {
    public static void main(String[] args) {
        // 方式一
        // ByteBuffer buffer1 = Charset.forName("utf-8").encode("你好");
        // 方式二
        ByteBuffer buffer2 = StandardCharsets.UTF_8.encode("你好");

        // ByteBufferUtil.debugAll(buffer1);
        ByteBufferUtil.debugAll(buffer2);

        CharBuffer buffer3 = StandardCharsets.UTF_8.decode(buffer2);
        System.out.println(buffer3.getClass());
        System.out.println(buffer3.toString());

    }
}
