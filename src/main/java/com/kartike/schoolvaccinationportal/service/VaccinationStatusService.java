package com.kartike.schoolvaccinationportal.service;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import com.kartike.schoolvaccinationportal.model.Student;
import com.kartike.schoolvaccinationportal.model.VaccinationStatus;
import com.kartike.schoolvaccinationportal.repository.StudentRepository;

@Service
@RequiredArgsConstructor
public class VaccinationStatusService {

    private final StudentRepository studentRepository;

    public Student updateVaccinationStatus(String studentId, String driveId, boolean vaccinated) {
    	
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));

        VaccinationStatus status = student.getVaccinationStatuses().stream()
                .filter(s -> s.getDriveId().equals(driveId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Vaccination status not found"));

        status.setVaccinated(vaccinated);
        status.setVaccinationDate(LocalDate.now());
        List<VaccinationStatus> statuses = student.getVaccinationStatuses();

        for (VaccinationStatus s : statuses) {
            if (s.getDriveId().equals(driveId)) {
                s.setVaccinated(vaccinated);
                break;
            }
        }
        student.setVaccinationStatuses(statuses);
        studentRepository.save(student);
        return student;
    }
}
