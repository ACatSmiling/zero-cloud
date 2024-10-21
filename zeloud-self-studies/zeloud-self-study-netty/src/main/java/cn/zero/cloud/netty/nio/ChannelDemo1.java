package cn.zero.cloud.netty.nio;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Xisun Wang
 * @since 2024/10/8 10:26
 */
@Slf4j
public class ChannelDemo1 {
    public static void main(String[] args) {
        URL resource = ChannelDemo1.class.getClassLoader().getResource("data.txt");
        if (resource == null || StringUtils.isBlank(resource.getPath())) {
            return;
        }

        try (RandomAccessFile file = new RandomAccessFile(resource.getPath(), "rw")) {
            FileChannel channel = file.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(10);
            do {
                // 向 buffer 写入
                int len = channel.read(buffer);
                log.debug("读到字节数：{}", len);
                if (len == -1) {
                    break;
                }
                // 切换 buffer 读模式
                buffer.flip();
                while (buffer.hasRemaining()) {
                    log.debug("{}", (char) buffer.get());
                }
                // 切换 buffer 写模式
                buffer.clear();
            } while (true);
        } catch (IOException e) {
            log.error("文件读取异常：", e);
        }
    }
}
