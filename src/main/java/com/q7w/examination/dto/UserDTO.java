package com.q7w.examination.dto;

import com.q7w.examination.dto.base.OutputConverter;
import com.q7w.examination.entity.Uesr.AdminRole;
import com.q7w.examination.entity.User;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author Evan
 * @date 2020/4/1 19:57
 */
@Data
@ToString
public class UserDTO implements OutputConverter<UserDTO, User> {

    private int UId;

    private String username;
    private String name;
    private String wxuid;
    private String uno;
    private Long createTime;
    private String phone;
    private String email;
    private boolean enabled;

    private List<AdminRole> roles;


}
