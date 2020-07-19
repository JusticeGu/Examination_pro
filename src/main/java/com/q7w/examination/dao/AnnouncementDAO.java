package com.q7w.examination.dao;

import com.q7w.examination.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementDAO extends JpaRepository<Announcement,Integer> {
    Announcement findByAid(int aid);
}
