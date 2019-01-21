package nur.nuradin.assignments.school;

import lombok.val;
import nur.nuradin.assignments.school.database.CourseRepository;
import nur.nuradin.assignments.school.domain.Course;
import nur.nuradin.assignments.school.service.CourseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@DisplayName("Unit testing - CourseService.class")
public class CourseServiceUnitTest {

  @InjectMocks private CourseService courseService;
  @Mock private CourseRepository courseRepository;

  @Test
  public void givenExistingCourse_whenCreateCourse_thenThrowException() {
    // INPUT
    val course = new Course();
    course.setCourseCode("csc108");
    course.setName("Introduction to Python");

    // MOCK
    when(courseRepository.existsById(course.getCourseCode().toUpperCase())).thenReturn(true);

    // EXECUTE
    Executable closure = () -> courseService.createCourse(course);

    // ASSERT
    val exception = assertThrows(EntityExistsException.class, closure);
    assertEquals("Tried to create course but course already exists", exception.getMessage());
  }

  @Test
  public void givenNonexistentCourse_whenCreateCourse_thenCreateAndSaveCourse() {
    // INPUT
    val course = new Course();
    course.setCourseCode("coe528");
    course.setName("Object Oriented Software Engineering Design");

    // MOCK
    when(courseRepository.existsById(course.getCourseCode().toUpperCase())).thenReturn(false);
    when(courseRepository.save(course)).thenReturn(course);

    // EXECUTE
    val result = courseService.createCourse(course);

    // ASSERT
    assertEquals(course.getCourseCode(), result.getCourseCode());
    assertEquals(course.getName(), result.getName());
  }

  @Test
  public void givenNonexistentCourse_whenUpdateCourse_thenThrowException() {
    // INPUT
    val course = new Course();
    course.setCourseCode("coe865");
    course.setName("Advanced Computer Networks");

    // MOCK
    when(courseRepository.existsById(course.getCourseCode().toUpperCase())).thenReturn(false);

    // EXECUTE
    Executable closure = () -> courseService.updateCourse(course);

    // ASSERT
    val exception = assertThrows(EntityNotFoundException.class, closure);
    assertEquals("Tried to update course but course was not found", exception.getMessage());
  }

  @Test
  public void givenCourseExistsButCannotBeFound_whenUpdateCourse_thenThrowException() {
    // INPUT
    val course = new Course();
    course.setCourseCode("coe865");
    course.setName("Advanced Computer Networks");

    // MOCK
    when(courseRepository.existsById(course.getCourseCode().toUpperCase())).thenReturn(true);
    when(courseRepository.findById(course.getCourseCode().toUpperCase()))
        .thenReturn(Optional.empty());

    // EXECUTE
    Executable closure = () -> courseService.updateCourse(course);

    // ASSERT
    val exception = assertThrows(IllegalStateException.class, closure);
    assertEquals("Course existsById yet cannot be foundById", exception.getMessage());
  }

  @Test
  public void givenExistingCourse_whenUpdateCourse_thenRetrieveAndUpdateCourse() {
    // INPUT
    val course = new Course();
    course.setCourseCode("ele888");
    course.setName("Intelligent Systems");

    // MOCK
    when(courseRepository.existsById(course.getCourseCode().toUpperCase())).thenReturn(true);
    when(courseRepository.findById(course.getCourseCode().toUpperCase()))
        .thenReturn(Optional.of(course));
    when(courseRepository.save(course)).thenReturn(course);

    // EXECUTE
    val result = courseService.updateCourse(course);

    // ASSERT
    assertEquals(course.getCourseCode(), result.getCourseCode());
    assertEquals(course.getName(), result.getName());
  }

  @Test
  public void givenNonexistentCourse_whenDeleteCourse_thenThrowException() {
    // INPUT
    val course = new Course();
    course.setCourseCode("coe865");
    course.setName("Advanced Computer Networks");

    // MOCK
    when(courseRepository.existsById(course.getCourseCode().toUpperCase())).thenReturn(false);

    // EXECUTE
    Executable closure = () -> courseService.deleteCourse(course.getCourseCode());

    // ASSERT
    val exception = assertThrows(EntityNotFoundException.class, closure);
    assertEquals("Tried to delete course but course was not found", exception.getMessage());
  }

  @Test
  public void givenCourseExistsButCannotBeFound_whenDeleteCourse_thenThrowException() {
    // INPUT
    val course = new Course();
    course.setCourseCode("coe865");
    course.setName("Advanced Computer Networks");

    // MOCK
    when(courseRepository.existsById(course.getCourseCode().toUpperCase())).thenReturn(true);
    when(courseRepository.findById(course.getCourseCode().toUpperCase()))
        .thenReturn(Optional.empty());

    // EXECUTE
    Executable closure = () -> courseService.deleteCourse(course.getCourseCode());

    // ASSERT
    val exception = assertThrows(IllegalStateException.class, closure);
    assertEquals("Course existsById yet cannot be foundById", exception.getMessage());
  }

  @Test
  public void givenExistingCourse_whenDeleteCourse_thenDeleteCourse() {
    // INPUT
    val course = new Course();
    course.setCourseCode("ele888");
    course.setName("Intelligent Systems");

    // MOCK
    when(courseRepository.existsById(course.getCourseCode().toUpperCase())).thenReturn(true);
    when(courseRepository.findById(course.getCourseCode().toUpperCase()))
        .thenReturn(Optional.of(course));

    // EXECUTE
    val result = courseService.deleteCourse(course.getCourseCode());

    // ASSERT
    verify(courseRepository, times(1)).deleteById(course.getCourseCode().toUpperCase());
    assertEquals(course.getCourseCode(), result.getCourseCode());
    assertEquals(course.getName(), result.getName());
  }
}
