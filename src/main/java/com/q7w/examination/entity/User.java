package com.q7w.examination.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.q7w.examination.entity.Uesr.AdminRole;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@ApiModel
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 3033545151355633270L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int UId;
    @Column
    private String username;
    @Column
    private String name;//真实姓名
    private String user_password;//密码
    private String uno;//学、工号
    private String salt;
    private String wxuid;
    @Column
    private boolean is_superuser;
    @Column
    private String email;
    private String phone;
    @Column
    private int active_status;
    private boolean enabled;
    @Transient
    private List<AdminRole> roles;


    public User(int uId, String userName, String userPassword) {

    }
}
