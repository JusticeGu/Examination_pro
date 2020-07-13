package com.q7w.examination.Service.impl;

import com.q7w.examination.Service.CourseService;
import com.q7w.examination.dao.CourseDAO;
import com.q7w.examination.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CourseServiceimpl implements CourseService {
    @Autowired
    CourseDAO courseDAO;
    @Override
    public List<Course> list() {
        return courseDAO.findAll();
    }

    @Override
    public int addCourse() {
        return 0;
    }

    @Override
    public boolean isexist(int cid) {
        Course course = courseDAO.findByCid(cid);
        return null!=course;
    }
}
