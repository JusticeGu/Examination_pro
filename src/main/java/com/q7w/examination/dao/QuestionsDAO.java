package com.q7w.examination.dao;

import com.q7w.examination.entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface QuestionsDAO  extends JpaRepository<Questions,Integer> {
    Questions findByQid(int qid);
    Questions findByCid(int cid);
  //  Questions findByCourseIDAndDiffcult(int cid,int diffcult);
    List<Questions> findAllByCid(int cid);
    List<Questions> findAllByCidAndTypeAndDiffcult(int cid,int type,int diffcult);
  //  List<Questions> findAllByCourseIDAndDiffcult(int cid,int diffcult);
  //  List<Questions> findAllByTypeAndCourseID(int type,int cid);
}
