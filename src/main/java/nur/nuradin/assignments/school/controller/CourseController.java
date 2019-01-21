package nur.nuradin.assignments.school.controller;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import nur.nuradin.assignments.school.domain.Course;
import nur.nuradin.assignments.school.service.CourseService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@Slf4j
@RestController
@RequestMapping("/course")
public class CourseController {

  private final CourseService courseService;

  public CourseController(CourseService courseService) {
    this.courseService = courseService;
  }

  @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Course> createCourse(@RequestBody Course course) {
    try {
      val result = courseService.createCourse(course);
      log.info("Course {}: {} created", result.getCourseCode(), result.getName());
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      log.info("Course {}: {} could not be created", course.getCourseCode(), course.getName());
      log.error(e.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }

  @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Course> updateCourse(@RequestBody Course course) {
    try {
      val result = courseService.updateCourse(course);
      log.info("Course {}: {} updated", result.getCourseCode(), result.getName());
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      log.info("Course {}: {} could not be updated", course.getCourseCode(), course.getName());
      log.error(e.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }

  @DeleteMapping(value = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Course> deleteStudent(@RequestBody String courseCode) {
    try {
      val result = courseService.deleteCourse(courseCode);
      log.info("Course {}: {} deleted", result.getCourseCode(), result.getName());
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      log.info("Course {}: {} could not be deleted", courseCode);
      log.error(e.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }
}
