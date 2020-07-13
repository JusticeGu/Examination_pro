package com.q7w.examination.entity.Uesr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.q7w.examination.entity.Uesr.AdminMenu;
import com.q7w.examination.entity.Uesr.AdminPermission;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "admin_role")
@ToString
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class AdminRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * Role name.
     */
    private String name;

    /**
     * Role name in Chinese.
     */
    @Column(name = "name_zh")
    private String nameZh;

    /**
     * Role status.
     */
    private boolean enabled;


    /**
     * Transient property for storing permissions owned by current role.
     */
    @Transient
    private List<AdminPermission> perms;

    /**
     * Transient property for storing menus owned by current role.
     */
    @Transient
    private List<AdminMenu> menus;
}
