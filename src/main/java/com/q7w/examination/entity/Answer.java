package com.q7w.examination.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;
import java.util.Map;

@Data
@Table(name = "s_answer")
public class Answer {

    public static final Integer STATUS_CORRECT = 1;

    public static final Integer STATUS_NOT_CORRECT = 0;
    private static final long serialVersionUID = 3033545151355633270L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ansid;
    private String answerContent;
    private int uid;
    private int pid;
    private int kid;
    private List<Map<Integer,Object>>  objanslist;//客观题答案列表
    private List<Map<Integer,Object>> subanslist;//
    private String wronglist;


}
