package nur.nuradin.assignments.school;

import lombok.val;
import nur.nuradin.assignments.school.database.StudentRepository;
import nur.nuradin.assignments.school.domain.Course;
import nur.nuradin.assignments.school.domain.Student;
import nur.nuradin.assignments.school.service.StudentService;
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
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@DisplayName("Unit testing - StudentService.class")
public class StudentServiceUnitTest {

  @InjectMocks private StudentService studentService;
  @Mock private StudentRepository studentRepository;

  @Test
  public void givenExistingCourse_whenCreateCourse_thenThrowException() {
    // INPUT
    val student = new Student();
    student.setFirstName("Don");
    student.setLastName("Michaels");

    // MOCK
    when(studentRepository.existsById(student.getStudentNumber())).thenReturn(true);

    // EXECUTE
    Executable closure = () -> studentService.createStudent(student);

    // ASSERT
    val exception = assertThrows(EntityExistsException.class, closure);
    assertEquals("Tried to create student but student already exists", exception.getMessage());
  }

  @Test
  public void givenNonexistentCourse_whenCreateCourse_thenCreateCourse() {
    // INPUT
    val student = new Student();
    student.setFirstName("Don");
    student.setLastName("Michaels");

    // MOCK
    when(studentRepository.existsById(student.getStudentNumber())).thenReturn(false);
    when(studentRepository.save(student)).thenReturn(student);

    // EXECUTE
    val result = studentService.createStudent(student);

    // ASSERT
    assertEquals(student, result);
  }

  @Test
  public void givenNonexistentCourse_whenUpdateCourse_thenUpdateCourse() {
    // INPUT
    val student = new Student();
    student.setFirstName("Don");
    student.setLastName("Michaels");

    // MOCK
    when(studentRepository.existsById(student.getStudentNumber())).thenReturn(false);

    // EXECUTE
    Executable closure = () -> studentService.updateStudent(student);

    // ASSERT
    val exception = assertThrows(EntityNotFoundException.class, closure);
    assertEquals("Tried to update student but student was not found", exception.getMessage());
  }

  @Test
  public void givenCourseExistsButCannotBeFound_whenUpdateCourse_thenThrowException() {
    // INPUT
    val student = new Student();
    student.setFirstName("Don");
    student.setLastName("Michaels");

    // MOCK
    when(studentRepository.existsById(student.getStudentNumber())).thenReturn(true);
    when(studentRepository.findById(student.getStudentNumber())).thenReturn(Optional.empty());

    // EXECUTE
    Executable closure = () -> studentService.updateStudent(student);

    // ASSERT
    val exception = assertThrows(IllegalStateException.class, closure);
    assertEquals("Student existsById yet cannot be foundById", exception.getMessage());
  }

  @Test
  public void givenExistingCourse_whenUpdateCourse_thenThrowException() {
    // INPUT
    val student = new Student();
    student.setFirstName("Don");
    student.setLastName("Michaels");

    // MOCK
    when(studentRepository.existsById(student.getStudentNumber())).thenReturn(true);
    when(studentRepository.findById(student.getStudentNumber())).thenReturn(Optional.of(student));
    when(studentRepository.save(student)).thenReturn(student);

    // EXECUTE
    val result = studentService.updateStudent(student);

    // ASSERT
    assertEquals(student, result);
  }

  @Test
  public void givenNonexistentCourse_whenDeleteCourse_thenDeleteCourse() {
    // INPUT
    val student = new Student();
    student.setFirstName("Don");
    student.setLastName("Michaels");

    // MOCK
    when(studentRepository.existsById(student.getStudentNumber())).thenReturn(false);

    // EXECUTE
    Executable closure = () -> studentService.deleteStudent(student.getStudentNumber());

    // ASSERT
    val exception = assertThrows(EntityNotFoundException.class, closure);
    assertEquals("Tried to delete student but student was not found", exception.getMessage());
  }

  @Test
  public void givenCourseExistsButCannotBeFound_whenDeleteCourse_thenThrowException() {
    // INPUT
    val student = new Student();
    student.setFirstName("Don");
    student.setLastName("Michaels");

    // MOCK
    when(studentRepository.existsById(student.getStudentNumber())).thenReturn(true);
    when(studentRepository.findById(student.getStudentNumber())).thenReturn(Optional.empty());

    // EXECUTE
    Executable closure = () -> studentService.deleteStudent(student.getStudentNumber());

    // ASSERT
    val exception = assertThrows(IllegalStateException.class, closure);
    assertEquals("Student existsById yet cannot be foundById", exception.getMessage());
  }

  @Test
  public void givenExistingCourse_whenDeleteCourse_thenThrowException() {
    // INPUT
    val student = new Student();
    student.setFirstName("Don");
    student.setLastName("Michaels");

    // MOCK
    when(studentRepository.existsById(student.getStudentNumber())).thenReturn(true);
    when(studentRepository.findById(student.getStudentNumber())).thenReturn(Optional.of(student));
    doNothing().when(studentRepository).deleteById(student.getStudentNumber());

    // EXECUTE
    val result = studentService.deleteStudent(student.getStudentNumber());

    // ASSERT
    verify(studentRepository, times(1)).deleteById(student.getStudentNumber());
    assertEquals(student, result);
  }

  @Test
  public void
      givenCourseChangeRequestWithEnrolledCourse_whenEnrollCourseForStudent_thenSuccessfullyEnrolled() {
    // INPUT
    val student = new Student();
    student.setFirstName("Don");
    student.setLastName("Michaels");
    student.setCourseGradesMap(new HashMap<>());

    val course = new Course();
    course.setCourseCode("CPS888");
    course.setName("Software Development Engineering");

    student.getCourseGradesMap().put(course, "F");

    // EXECUTE
    Executable closure = () -> studentService.enrollCourseforStudent(student, course);

    // ASSERT
    val exception = assertThrows(IllegalArgumentException.class, closure);
    assertEquals("Student is already enrolled in course", exception.getMessage());
  }

  @Test
  public void
      givenCourseChangeRequestWithoutEnrolledCourse_whenDropCourseForStudent_thenSuccessfullyDropped() {
    // INPUT
    val student = new Student();
    student.setFirstName("Don");
    student.setLastName("Michaels");
    student.setCourseGradesMap(new HashMap<>());

    val course = new Course();
    course.setCourseCode("CPS888");
    course.setName("Software Development Engineering");

    // EXECUTE
    Executable closure = () -> studentService.dropCourseForStudent(student, course);

    // ASSERT
    val exception = assertThrows(IllegalArgumentException.class, closure);
    assertEquals("Student is not currently enrolled in course", exception.getMessage());
  }

  @Test
  public void givenNonexistentStudent_whenGetGradesForStudent_thenThrowException() {
    // INPUT
    val student = new Student();
    student.setFirstName("Don");
    student.setLastName("Michaels");
    student.setCourseGradesMap(new HashMap<>());

    val course = new Course();
    course.setCourseCode("CPS888");
    course.setName("Software Development Engineering");

    student.getCourseGradesMap().put(course, "F");

    // MOCK
    when(studentRepository.existsById(student.getStudentNumber())).thenReturn(false);

    // EXECUTE
    Executable closure = () -> studentService.getGradesForStudent(student);

    // ASSERT
    val exception = assertThrows(EntityExistsException.class, closure);
    assertEquals("Tried to retrieve grades for student yet student does not exist", exception.getMessage());
  }

  @Test
  public void givenExistingStudent_whenGetGradesForStudent_thenThrowException() {

    // INPUT
    val student = new Student();
    student.setFirstName("Don");
    student.setLastName("Michaels");
    student.setCourseGradesMap(new HashMap<>());

    val course = new Course();
    course.setCourseCode("CPS888");
    course.setName("Software Development Engineering");

    student.getCourseGradesMap().put(course, "F");

    // MOCK
    when(studentRepository.existsById(student.getStudentNumber())).thenReturn(true);
    when(studentRepository.findById(student.getStudentNumber())).thenReturn(Optional.of(student));

    // EXECUTE
    val result = studentService.getGradesForStudent(student);

    // ASSERT
    assertEquals(result, student.getCourseGradesMap());
  }
}
