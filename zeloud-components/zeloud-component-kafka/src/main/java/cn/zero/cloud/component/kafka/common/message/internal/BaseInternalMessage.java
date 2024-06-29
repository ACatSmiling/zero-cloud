package cn.zero.cloud.component.kafka.common.message.internal;


import cn.zero.cloud.component.kafka.common.constants.MessageCategory;
import cn.zero.cloud.component.kafka.common.message.BaseMessage;

/**
 * @author Xisun Wang
 * @since 2024/3/8 16:58
 */
public abstract class BaseInternalMessage<A> extends BaseMessage<A> {
    @Override
    public MessageCategory getMessageCategory() {
        return MessageCategory.INTERNAL;
    }
}
