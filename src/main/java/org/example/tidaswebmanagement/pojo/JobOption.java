package org.example.tidaswebmanagement.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobOption {
    private List<String> job;
    private  List<Long> sums;
}
