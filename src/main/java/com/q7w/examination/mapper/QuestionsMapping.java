package com.q7w.examination.mapper;

import com.q7w.examination.entity.QuestionCategory;
import com.q7w.examination.entity.Questions;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionsMapping {
    @Select("SELECT * FROM `questions` WHERE questionCategory = '#{questionCategory}' ORDER BY  RAND() LIMIT #{num} ;")
    List<Questions> queryquestions(@Param("questionCategory") QuestionCategory questionCategory, @Param("num") int num);
}
