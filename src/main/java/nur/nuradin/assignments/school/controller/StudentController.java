package nur.nuradin.assignments.school.controller;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import nur.nuradin.assignments.school.domain.Course;
import nur.nuradin.assignments.school.domain.CourseChangeRequest;
import nur.nuradin.assignments.school.domain.Student;
import nur.nuradin.assignments.school.service.StudentService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/students")
public class StudentController {

  private final StudentService studentService;

  public StudentController(StudentService studentService) {
    this.studentService = studentService;
  }

  @GetMapping(value = "/grades", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<Course, String>> getGrades(@RequestBody Student student) {
    try {
      val result = studentService.getGradesForStudent(student);
      log.info(
          "Student's {} {} ({}) grades retrieved",
          student.getFirstName(),
          student.getLastName(),
          student.getStudentNumber());
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      log.warn(
          "Student's {} {} ({}) grades could not be retrieved",
          student.getFirstName(),
          student.getLastName(),
          student.getStudentNumber());
      log.error(e.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }

  @PostMapping(value = "/enroll", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Student> enrollCourse(
      @RequestBody CourseChangeRequest courseChangeRequest) {
    try {
      val result =
          studentService.enrollCourseforStudent(
              courseChangeRequest.getStudent(), courseChangeRequest.getCourse());
      log.info(
          "Student {} {} ({}) enrolled in course {} ({})",
          courseChangeRequest.getStudent().getFirstName(),
          courseChangeRequest.getStudent().getLastName(),
          courseChangeRequest.getStudent().getStudentNumber(),
          courseChangeRequest.getCourse().getCourseCode(),
          courseChangeRequest.getCourse().getName());
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      log.warn(
          "Student {} {} ({}) could not be enrolled in course {} ({})",
          courseChangeRequest.getStudent().getFirstName(),
          courseChangeRequest.getStudent().getLastName(),
          courseChangeRequest.getStudent().getStudentNumber(),
          courseChangeRequest.getCourse().getCourseCode(),
          courseChangeRequest.getCourse().getName());
      log.error(e.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }

  @PostMapping(value = "/drop", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Student> dropCourse(@RequestBody CourseChangeRequest courseChangeRequest) {
    try {
      val result =
          studentService.dropCourseForStudent(
              courseChangeRequest.getStudent(), courseChangeRequest.getCourse());
      log.info(
          "Student {} {} ({}) has dropped course {} ({})",
          courseChangeRequest.getStudent().getFirstName(),
          courseChangeRequest.getStudent().getLastName(),
          courseChangeRequest.getStudent().getStudentNumber(),
          courseChangeRequest.getCourse().getCourseCode(),
          courseChangeRequest.getCourse().getName());
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      log.warn(
          "Student {} {} ({}) could not drop course {} ({})",
          courseChangeRequest.getStudent().getFirstName(),
          courseChangeRequest.getStudent().getLastName(),
          courseChangeRequest.getStudent().getStudentNumber(),
          courseChangeRequest.getCourse().getCourseCode(),
          courseChangeRequest.getCourse().getName());
      log.error(e.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }

  @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Student> create(@RequestBody Student student) {
    try {
      val result = studentService.createStudent(student);
      log.info(
          "Student {} {} ({}) created",
          result.getFirstName(),
          result.getLastName(),
          result.getStudentNumber());
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      log.warn(
          "Student {} {} ({}) could not be created",
          student.getFirstName(),
          student.getLastName(),
          student.getStudentNumber());
      log.error(e.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }

  @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Student> update(@RequestBody Student student) {
    try {
      val result = studentService.updateStudent(student);
      log.info(
          "Student {} {} ({}) updated",
          result.getFirstName(),
          result.getLastName(),
          result.getStudentNumber());
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      log.warn(
          "Student {} {} ({}) could not be updated",
          student.getFirstName(),
          student.getLastName(),
          student.getStudentNumber());
      log.error(e.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }

  @DeleteMapping(value = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Student> delete(@RequestBody Long studentNumber) {
    try {
      val result = studentService.deleteStudent(studentNumber);
      log.info(
          "Student {} {} ({}) deleted",
          result.getFirstName(),
          result.getLastName(),
          result.getStudentNumber());
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      log.warn("Student {} could not be deleted", studentNumber);
      log.error(e.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }
}
