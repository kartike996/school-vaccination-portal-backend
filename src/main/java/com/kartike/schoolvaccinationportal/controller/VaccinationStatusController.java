package com.kartike.schoolvaccinationportal.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kartike.schoolvaccinationportal.model.Student;
import com.kartike.schoolvaccinationportal.model.VaccinationDrive;
import com.kartike.schoolvaccinationportal.model.VaccinationStatus;
import com.kartike.schoolvaccinationportal.repository.StudentRepository;
import com.kartike.schoolvaccinationportal.repository.VaccinationDriveRepository;
import com.kartike.schoolvaccinationportal.service.StudentService;
import com.kartike.schoolvaccinationportal.service.VaccinationStatusService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/status")
@RequiredArgsConstructor
public class VaccinationStatusController {

	@Autowired
	private VaccinationStatusService vaccinationStatusService;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private VaccinationDriveRepository driveRepository;

	@Autowired
	private StudentService studentService;

	@PostMapping("/students")
	public ResponseEntity<String> registerForDrive(@RequestBody Student student) {
		
		Student savedStudent = studentRepository.save(student);

		return ResponseEntity.ok("Student registered for vaccination drive.");
	}

	@PutMapping("/students/{id}")
	public ResponseEntity<?> updateStudent(@PathVariable String id, @RequestBody Student updatedStudent) {
		
		return studentRepository.findById(id).map(student -> {
			student.setStudentName(updatedStudent.getStudentName());
			student.setStudentClass(updatedStudent.getStudentClass());
			student.setStudentId(updatedStudent.getStudentId());
			Student saved = studentRepository.save(student);
			return ResponseEntity.ok(saved);
		}).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/{studentId}/vaccination-status")
	public ResponseEntity<List<VaccinationStatus>> getVaccinationStatuses(@PathVariable("studentId") String studentId) {
		
		Student student = studentRepository.findById(studentId).orElse(null);

		if (student == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(student.getVaccinationStatuses());
	}

	@DeleteMapping("/{studentId}/vaccination/{driveId}")
	public ResponseEntity<String> removeVaccinationEntry(@PathVariable("studentId") String studentId,
			@PathVariable("driveId") String driveId) {
		
		Student student = studentRepository.findById(studentId).orElse(null);

		if (student == null) {
			return ResponseEntity.notFound().build();
		}

		boolean removed = student.getVaccinationStatuses().removeIf(status -> status.getDriveId().equals(driveId));

		if (!removed) {
			return ResponseEntity.badRequest().body("Vaccination entry not found for this drive.");
		}

		studentRepository.save(student);
		return ResponseEntity.ok("Vaccination entry removed.");
	}

	@GetMapping("/students")
	public ResponseEntity<List<Student>> getAllStudents() {
		
		List<Student> students = studentRepository.findAll();
		
		return ResponseEntity.ok(students);
	}

	@GetMapping("/driveRegisteredStudents")
	public ResponseEntity<List<Student>> getAllStudentsRegisteredForDrive() {
		
		List<Student> students = studentRepository.findAll();
		
		List<Student> filteredStudents = students.stream().filter(
				student -> student.getVaccinationStatuses() != null && !student.getVaccinationStatuses().isEmpty())
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(filteredStudents);
	}

	@GetMapping("/drives")
	public ResponseEntity<List<VaccinationDrive>> getAllDrives() {
		
		List<VaccinationDrive> drives = driveRepository.findAll();
		
		return ResponseEntity.ok(drives);
	}

	@GetMapping("/dashboard")
	public ResponseEntity<Map<String, Object>> getDashboardMetrics() {
		
		long totalStudents = studentRepository.count();
		long vaccinatedStudents = studentRepository.countVaccinatedStudents();
		double percentageVaccinated = totalStudents == 0 ? 0
				: Math.round((vaccinatedStudents * 100.0 / totalStudents) * 100.0) / 100.0;

		LocalDate today = LocalDate.now();
		LocalDate next30 = today.plusDays(30);
		List<VaccinationDrive> upcomingDrives = driveRepository.findByDriveDateBetween(today, next30);

		Map<String, Object> response = new HashMap<>();
		response.put("totalStudents", totalStudents);
		response.put("vaccinatedStudents", vaccinatedStudents);
		response.put("percentageVaccinated", percentageVaccinated);
		response.put("upcomingDrives", upcomingDrives);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/students/bulk-upload")
	public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) {
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
			List<Student> students = new ArrayList<>();
			String line;
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(",");
				if (tokens.length >= 3) {
					Student student = new Student();
					student.setStudentName(tokens[1]);
					student.setStudentClass(tokens[2]);
					student.setStudentId(tokens[0]);
					students.add(student);
				}
			}
			studentRepository.saveAll(students);
			return ResponseEntity.ok("Bulk upload successful.");
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing CSV file.");

		}
	}

	@GetMapping("/report")
	public ResponseEntity<Map<String, Object>> getVaccinationReport(
			@RequestParam(required = true, name = "vaccineName") String vaccineName,
			@RequestParam(name = "page") int page, @RequestParam(name = "size") int size) {

		List<Student> all = studentRepository.findAll();

		if (vaccineName != null && !vaccineName.isEmpty()) {
			all = all.stream()
					.filter(s -> s.getVaccinationStatuses() != null && s.getVaccinationStatuses().stream()
							.anyMatch(v -> v.getVaccineName().equalsIgnoreCase(vaccineName)))
					.collect(Collectors.toList());
		}

		int total = all.size();
		int start = Math.min(page * size, total);
		int end = Math.min(start + size, total);

		List<Student> paginatedList = all.subList(start, end);

		Map<String, Object> response = new HashMap<>();
		response.put("students", paginatedList);
		response.put("currentPage", page);
		response.put("totalItems", total);
		response.put("totalPages", (int) Math.ceil((double) total / size));

		return ResponseEntity.ok(response);
	}

	@PostMapping("/drives")
	public ResponseEntity<String> createDrive(@RequestBody VaccinationDrive drive) {
		
		if (drive.getDriveDate().isBefore(LocalDate.now().plusDays(15))) {
			return ResponseEntity.badRequest().body("Drives must be scheduled at least 15 days in advance.");
		}

		boolean overlap = driveRepository
				.findByDriveDateBetween(drive.getDriveDate().minusDays(1), drive.getDriveDate().plusDays(1)).stream()
				.anyMatch(d -> d.getApplicableClasses().equals(drive.getApplicableClasses()));

		if (overlap) {
			return ResponseEntity.badRequest().body("A drive already exists around that date for the same class.");
		}

		driveRepository.save(drive);
		return ResponseEntity.ok("Drive created successfully.");
	}

	@PutMapping("/drives/{id}")
	public ResponseEntity<?> updateDrive(@PathVariable(name = "id") String id,
			@RequestBody VaccinationDrive updatedDrive) {
		
		return driveRepository.findById(id).map(existing -> {
			if (existing.getDriveDate().isBefore(LocalDate.now())) {
				return ResponseEntity.badRequest().body("Cannot edit past or ongoing drives.");
			}

			existing.setDriveDate(updatedDrive.getDriveDate());
			existing.setAvailableDoses(updatedDrive.getAvailableDoses());
			existing.setVaccineName(updatedDrive.getVaccineName());
			existing.setApplicableClasses(updatedDrive.getApplicableClasses());
			driveRepository.save(existing);
			return ResponseEntity.ok(existing);
		}).orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/students/{studentId}/vaccination/{driveId}")
	public ResponseEntity<Student> updateStatus(@PathVariable("studentId") String studentId,
			@PathVariable("driveId") String driveId, @RequestParam("vaccinated") boolean vaccinated) {
		
		Student status = vaccinationStatusService.updateVaccinationStatus(studentId, driveId, vaccinated);
		
		return ResponseEntity.ok(status);
	}

	@PostMapping("/{studentId}/register/{driveId}")
	public ResponseEntity<String> registerStudentToDrive(@PathVariable("studentId") String studentId,
			@PathVariable("driveId") String driveId) {
		
		studentService.registerStudentForDrive(studentId, driveId);
		
		return ResponseEntity.ok("Student registered for the drive.");
	}
}
