package com.q7w.examination.dao;

import com.q7w.examination.entity.Paper;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaperDAO extends JpaRepository<Paper,Integer> {
    Paper findByPid(int pid);
    Paper findByName(String name);
    List<Paper> findAllByType(int cid);
}
