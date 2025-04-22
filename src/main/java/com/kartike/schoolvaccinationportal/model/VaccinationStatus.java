package com.kartike.schoolvaccinationportal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VaccinationStatus {
	
    private String vaccineName;
    
    private LocalDate VaccinationDate;
    
    private String driveId;
    
    private boolean isVaccinated;
}
