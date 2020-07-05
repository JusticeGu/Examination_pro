package com.q7w.examination.dao;


import com.q7w.examination.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserDAO extends JpaRepository<User,Integer> {
    User findByUsername(String username);
    User findById (int uid);
    User findByWxuid(String wxuid);
    void deleteByUId(int uid);
 //   User getByUsernameAndPassword(String username);
}