package com.q7w.examination.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "examdata")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Examdata extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 3033545151355633270L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eid;
    private int kid;
    private int pid;
    private int times;
    private String uno;
    private String anslist;
    private String objsinuanslist;
    private String wronglist;
    private String objscore;//分数
    private float subscore;
    private float totalscore;
    private int status;//1-正在考试 2-已提交(待批阅) 3-客观题已批阅 4-主观题已批阅 5-已合成
    private String remark;//备注
    private long subtime; //交卷时间

}
