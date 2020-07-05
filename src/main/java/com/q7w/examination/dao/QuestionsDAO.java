package com.q7w.examination.dao;

import com.q7w.examination.entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionsDAO  extends JpaRepository<Questions,Integer> {
    Questions findByQid(int qid);
}
