package com.q7w.examination.dao;

import com.q7w.examination.entity.Exroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExroomDAO extends JpaRepository<Exroom,Integer> {
    Exroom findByKid(int kid);
    List<Exroom> findAllByStarttimeBetween(long now,long ddl);
    @Query(nativeQuery =true,value = "select count(*) from exroom where create_time >= ?1-24*60*60*1000 and create_time < ?1")
    Integer getNumExamPerDay(Long t);
    List<Exroom> findByStarttimeBefore(long now);
    List<Exroom> findByStarttimeAfter(long now);

}
