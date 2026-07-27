package org.example.tidaswebmanagement.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDegreeOption {
    private List<String> degree;
    private List<Long> sums;
}