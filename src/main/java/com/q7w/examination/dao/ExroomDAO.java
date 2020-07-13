package com.q7w.examination.dao;

import com.q7w.examination.entity.Exroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExroomDAO extends JpaRepository<Exroom,Integer> {
    Exroom findByKid(int kid);
}
