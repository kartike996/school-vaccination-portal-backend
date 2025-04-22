package com.kartike.schoolvaccinationportal.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.kartike.schoolvaccinationportal.model.VaccinationDrive;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VaccinationDriveRepository extends MongoRepository<VaccinationDrive, String> {
    List<VaccinationDrive> findByDriveDateBetween(LocalDate start, LocalDate end);
}
