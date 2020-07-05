package com.q7w.examination.dao;



import com.q7w.examination.entity.AdminRole;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AdminRoleDAO extends JpaRepository<AdminRole, Integer> {
    AdminRole findById(int id);
}
