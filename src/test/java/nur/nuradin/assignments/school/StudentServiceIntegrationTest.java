package nur.nuradin.assignments.school;

import lombok.val;
import nur.nuradin.assignments.school.database.CourseRepository;
import nur.nuradin.assignments.school.database.StudentRepository;
import nur.nuradin.assignments.school.domain.Course;
import nur.nuradin.assignments.school.domain.Student;
import nur.nuradin.assignments.school.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("Integration test - StudentService.class")
@Transactional(isolation = Isolation.SERIALIZABLE)
public class StudentServiceIntegrationTest {

  @Autowired private StudentService studentService;
  @Autowired private StudentRepository studentRepository;
  @Autowired private CourseRepository courseRepository;

  @BeforeEach
  void initialize() {
    val student = getTestStudent();
    student
        .getCourseGradesMap()
        .keySet()
        .stream()
        .forEach(
            course -> {
              if (!courseRepository.existsById(course.getCourseCode())) {
                courseRepository.save(course);
              }
            });
    if (studentRepository.existsById(student.getStudentNumber())) {
      studentRepository.deleteById(student.getStudentNumber());
    }
  }

  @Test
  public void givenStudent_whenCreateStudent_studentIsCreated() {
    // INPUT
    val student = getTestStudent();

    // EXECUTE
    val result = studentService.createStudent(student);

    // ASSERT
    assertEquals(student, result);
  }

  @Test
  public void givenStudent_whenUpdateStudent_studentIsUpdated() {
    // INPUT
    val student = getTestStudent();
    val updatedStudent = getTestStudent();
    val newCourse = new Course();
    newCourse.setCourseCode("COE817");
    newCourse.setName("Network Security");
    updatedStudent.getCourseGradesMap().put(newCourse, "A-");

    // SETUP
    courseRepository.save(newCourse);
    studentRepository.save(student);

    // EXECUTE
    val result = studentService.updateStudent(updatedStudent);

    // ASSERT
    assertEquals("Vito", result.getFirstName());
    assertEquals("Genovese", result.getLastName());
    assertEquals(Long.valueOf(500999123), result.getStudentNumber());
    assertEquals(updatedStudent.getCourseGradesMap().size(), result.getCourseGradesMap().size());
    assertTrue(result.getCourseGradesMap().containsKey(newCourse));

    // CLEANUP
    courseRepository.delete(newCourse);
  }

  @Test
  public void givenStudent_whenDeleteStudent_studentIsDeleted() {
    // INPUT
    val student = getTestStudent();

    // SETUP
    studentRepository.save(student);

    // EXECUTE
    val result = studentService.deleteStudent(student.getStudentNumber());

    // ASSERT
    assertEquals(student, result);
    assertTrue(!studentRepository.existsById(student.getStudentNumber()));
  }

  private Student getTestStudent() {
    val student = new Student();
    student.setStudentNumber(500999123L);
    student.setFirstName("Vito");
    student.setLastName("Genovese");
    student.setCourseGradesMap(new HashMap<>());

    val course1 = new Course();
    course1.setCourseCode("ELE882");
    course1.setName("Multimedia Compression Algorithms");

    val course2 = new Course();
    course2.setCourseCode("MTH514");
    course2.setName("Statistical Calculus and Numerical Analyses");

    student.getCourseGradesMap().put(course1, "A+");
    student.getCourseGradesMap().put(course2, "B");
    return student;
  }
}
