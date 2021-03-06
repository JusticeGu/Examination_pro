package com.q7w.examination.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name = "s_answer")
public class Answer {

    public static final Integer STATUS_CORRECT = 1;

    public static final Integer STATUS_NOT_CORRECT = 0;
    private static final long serialVersionUID = 3033545151355633270L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ansid;
    private int eid;
    private int qid;
    private String answerContent;
    private int visualQid;
}
