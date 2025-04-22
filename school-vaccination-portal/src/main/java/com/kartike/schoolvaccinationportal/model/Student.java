package com.kartike.schoolvaccinationportal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "students_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
	
	@Id
    private String studentId;
   
    private String studentName;
    
    private String studentClass;
    
    private List<VaccinationStatus> vaccinationStatuses = new ArrayList<>();
   
}
