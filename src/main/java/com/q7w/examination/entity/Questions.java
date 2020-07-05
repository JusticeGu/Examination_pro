package com.q7w.examination.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "questions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Questions extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 3033545151355633270L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int qid;

    private String question;
    private int type;
    private String answer;
    private String remarks;


}
