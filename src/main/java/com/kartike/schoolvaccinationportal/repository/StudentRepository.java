package com.kartike.schoolvaccinationportal.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.kartike.schoolvaccinationportal.model.Student;

import java.util.List;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
	
    List<Student> findByStudentId(String studentId);

    @Query(value = "{ 'vaccinationStatuses.isVaccinated': true }", count = true)
    long countVaccinatedStudents();
}
