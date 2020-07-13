package com.q7w.examination.Service;

import com.q7w.examination.entity.Course;

import java.util.List;

public interface CourseService {
    public List<Course> list();
    public int addCourse();
    public boolean isexist(int cid);

}
