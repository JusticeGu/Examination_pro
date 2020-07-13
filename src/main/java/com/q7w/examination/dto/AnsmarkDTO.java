package com.q7w.examination.dto;

import com.q7w.examination.entity.Examdata;
import lombok.Data;

import java.util.List;
@Data
public class AnsmarkDTO {
    private float score;

    private List<String> wrongIds;

    private List<Examdata> examdata;
}
