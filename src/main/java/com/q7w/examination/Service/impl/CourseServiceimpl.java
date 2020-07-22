package com.q7w.examination.Service.impl;

import com.q7w.examination.Service.CourseService;
import com.q7w.examination.dao.CourseDAO;
import com.q7w.examination.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
/**
 * @author xiaogu
 * @date 2020/7/15 19:29
 **/
@Service
public class CourseServiceimpl implements CourseService {
    @Autowired
    CourseDAO courseDAO;
    @Override
    public List<Course> list() {
        return courseDAO.findAll();
    }

    @Override
    public List<Course> querycourse(String coursename) {
        return courseDAO.findAllByCourseNameLike(coursename);
    }

    @Override
    public int addCourse(Course course) {
        Date now= new Date();
        Long createtime = now.getTime();
        course.setCreateTime(createtime);
        course.setUpdateTime(createtime);
        try{
            courseDAO.save(course);
            return 1;
        } catch (IllegalArgumentException e){
            return 2;
        }

    }

    @Override
    public int delCourse(int cid) {
        if(!isexist(cid)){return -1;}
        courseDAO.deleteById(cid);
        return 1;
    }

    @Override
    public boolean isexist(int cid) {
        Course course = courseDAO.findByCid(cid);
        return null!=course;
    }

    @Override
    public Course findcourse(int cid) {
        Course course = courseDAO.findByCid(cid);
        return courseDAO.findByCid(cid);
    }
}
