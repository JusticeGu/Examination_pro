package com.q7w.examination.dao;

import com.q7w.examination.entity.Course;
import com.q7w.examination.entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface QuestionsDAO  extends JpaRepository<Questions,Integer> {
    Questions findByQid(int qid);
    List<Questions> findAllByCourse(Course course);
    List<Questions> findAllByType(int type);
   // List<Questions> findAllByCidAndTypeAndDiffcult(int cid,int type,int diffcult);
  //  List<Questions> findAllByCourseIDAndDiffcult(int cid,int diffcult);
    List<Questions> findAllByTypeAndCourse(int type,Course course);

}
