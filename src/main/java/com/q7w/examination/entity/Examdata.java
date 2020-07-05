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
    private int uno;
    private int kid;
    private String objsinqunlist;
    private String objsinuanslist;
    private String objsinanslist;
    private String objmulqunlist;
    private String objmuluanslist;
    private String objmulanslist;
    private String subqunlist;
    private String subanslist;
    private String objscorelist;
    private String subscorelist;
    private float objscore;
    private float subscore;
    private int status;//1-正在考试 2-已提交(待批阅) 3-客观题已批阅 4-主观题已批阅 5-已合成

}
