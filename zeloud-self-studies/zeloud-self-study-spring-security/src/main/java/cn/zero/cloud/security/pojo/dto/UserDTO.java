package cn.zero.cloud.security.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Xisun Wang
 * @since 7/9/2024 16:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String username;

    private String password;
}
