package com.kartike.schoolvaccinationportal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "vaccination_drives_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VaccinationDrive {

	@Id
	private String driveId;
	
	private String vaccineName;
	
	private LocalDate driveDate;
	
	private int availableDoses;
	
	private String applicableClasses;
	
	private boolean completed;
}
