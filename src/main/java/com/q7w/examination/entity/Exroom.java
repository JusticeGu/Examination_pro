package com.q7w.examination.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.minidev.json.annotate.JsonIgnore;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
@Entity
@Table(name = "Exroom")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Exroom extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 3033545151355633270L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Kid;

    private String name;
    private int pid;//试卷号
    private String remark;//考场备注
    private boolean enable;//考场启用
    private String group;//开放群体
    private long starttime;//起始时间
    private long deadline;
    private long time;//考试时间
    private int type;//考场类型


}
