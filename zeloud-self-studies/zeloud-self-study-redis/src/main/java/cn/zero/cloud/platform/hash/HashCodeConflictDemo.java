package cn.zero.cloud.platform.hash;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Xisun Wang
 * @since 2024/6/20 23:17
 */
public class HashCodeConflictDemo {
    public static void main(String[] args) {
        // 2112
        System.out.println("Aa".hashCode());
        // 2112
        System.out.println("BB".hashCode());

        // 851553
        System.out.println("柳柴".hashCode());
        // 851553
        System.out.println("柴柕".hashCode());

        Set<Integer> hashCodeSet = new HashSet<>();
        for (int i = 0; i < 200000; i++) {
            int hashCode = new Object().hashCode();
            if (hashCodeSet.contains(hashCode)) {
                // 出现了重复的hashcode: 2134400190	 运行到105466
                System.out.println("出现了重复的hashcode: " + hashCode + "\t 运行到" + i);
                break;
            }
            hashCodeSet.add(hashCode);
        }
    }
}
