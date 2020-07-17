package com.q7w.examination.dao;

import com.q7w.examination.entity.Course;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseDAO extends JpaRepository<Course,Integer> {
    Course findByCid(int cid);
    List<Course> findAllByCourseNameLike(String coursename);
}
