package com.q7w.examination.Service;

import com.q7w.examination.entity.Course;

import java.util.List;

public interface CourseService {
    public List<Course> list();
    public int addCourse(Course course);
    public boolean isexist(int cid);
    public int delCourse(int cid);

}
