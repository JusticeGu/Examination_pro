package com.q7w.examination.dto;

import com.q7w.examination.dto.base.OutputConverter;
import com.q7w.examination.entity.QuestionCategory;
import com.q7w.examination.entity.Questions;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class QuestionsDTO  implements OutputConverter<QuestionsDTO, Questions> {

    private int qid;
    private String question;
    private int type;
    private QuestionCategory questionCategory;
}
