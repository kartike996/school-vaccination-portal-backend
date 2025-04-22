package com.kartike.schoolvaccinationportal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.kartike.schoolvaccinationportal.model.Student;
import com.kartike.schoolvaccinationportal.model.VaccinationDrive;
import com.kartike.schoolvaccinationportal.model.VaccinationStatus;
import com.kartike.schoolvaccinationportal.repository.StudentRepository;
import com.kartike.schoolvaccinationportal.repository.VaccinationDriveRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    
    private final VaccinationDriveRepository vaccinationDriveRepository;

    public Student registerStudentForDrive(String studentId, String driveId) {
    	
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        
        if (studentOpt.isEmpty()) {
            throw new RuntimeException("Student not found");
        }
        Student student = studentOpt.get();

        VaccinationDrive drive = vaccinationDriveRepository.findById(driveId).orElseThrow(() -> new RuntimeException("Drive not found"));

        VaccinationStatus status = new VaccinationStatus();
        status.setVaccineName(drive.getVaccineName());
        status.setDriveId(driveId);
        status.setVaccinated(false);

        student.getVaccinationStatuses().add(status);
        return studentRepository.save(student);
    }

    public List<Student> getAllStudentsVaccinationStatuses() {
        return studentRepository.findAll();
    }

    public Student getVaccinationStatusByStudentId(String studentId) {
        return studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
    }
}
