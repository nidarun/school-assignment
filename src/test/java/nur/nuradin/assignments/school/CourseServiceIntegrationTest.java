package nur.nuradin.assignments.school;

import lombok.val;
import nur.nuradin.assignments.school.database.CourseRepository;
import nur.nuradin.assignments.school.domain.Course;
import nur.nuradin.assignments.school.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("Integration test - CourseService.class")
@Transactional(isolation = Isolation.SERIALIZABLE)
public class CourseServiceIntegrationTest {

  @Autowired private CourseService courseService;
  @Autowired private CourseRepository courseRepository;

  @BeforeEach
  void resetRepository() {
    val course = getTestCourse();
    if (courseRepository.existsById(course.getCourseCode())) {
      courseRepository.deleteById(course.getCourseCode());
    }
  }

  @Test
  public void givenCourse_whenCreateCourse_courseIsCreated() {
    // INPUT
    val course = getTestCourse();

    // EXECUTE
    val result = courseService.createCourse(course);

    // ASSERT
    assertEquals(course, result);
  }

  @Test
  public void givenCourse_whenUpdateCourse_courseIsUpdated() {
    // INPUT
    val course = getTestCourse();
    val updatedCourse = getTestCourse();
    updatedCourse.setName("Operating Systems");

    // SETUP
    courseRepository.save(course);

    // EXECUTE
    val result = courseService.updateCourse(updatedCourse);

    // ASSERT
    assertEquals("COE538", result.getCourseCode());
    assertEquals("Operating Systems", result.getName());
  }

  @Test
  public void givenCourse_whenDeleteCourse_courseIsDeleted() {
    // INPUT
    val course = getTestCourse();

    // SETUP
    courseRepository.save(course);

    // EXECUTE
    val result = courseService.deleteCourse(course.getCourseCode());

    // ASSERT
    assertEquals(course, result);
    assertTrue(!courseRepository.existsById(course.getCourseCode()));
  }

  private Course getTestCourse() {
    val course = new Course();
    course.setCourseCode("COE538");
    course.setName("Computer Organization and Architecture");
    return course;
  }
}
