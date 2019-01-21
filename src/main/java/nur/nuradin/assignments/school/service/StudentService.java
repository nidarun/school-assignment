package nur.nuradin.assignments.school.service;

import lombok.val;
import nur.nuradin.assignments.school.database.StudentRepository;
import nur.nuradin.assignments.school.domain.Course;
import nur.nuradin.assignments.school.domain.Student;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Map;

@Service
@Transactional(isolation = Isolation.SERIALIZABLE)
public class StudentService {

  private final StudentRepository studentRepository;

  public StudentService(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  public Student enrollCourseforStudent(Student student, Course course) {
    if (student.getCourseGradesMap().containsKey(course)) {
      throw new IllegalArgumentException("Student is already enrolled in course");
    }
    student.getCourseGradesMap().put(course, "N/A");
    return updateStudent(student);
  }

  public Student dropCourseForStudent(Student student, Course course) {
    if (!student.getCourseGradesMap().containsKey(course)) {
      throw new IllegalArgumentException("Student is not currently enrolled in course");
    }
    student.getCourseGradesMap().remove(course);
    return updateStudent(student);
  }

  public Map<Course, String> getGradesForStudent(Student student) {
    if (!studentRepository.existsById(student.getStudentNumber())) {
      throw new EntityExistsException("Tried to retrieve grades for student yet student does not exist");
    } else {
      val studentOptional = studentRepository.findById(student.getStudentNumber());
      if (studentOptional.isPresent()) {
        return studentOptional.get().getCourseGradesMap();
      }
      throw new IllegalStateException("Student existsById yet cannot be foundById");
    }
  }

  public Student createStudent(Student student) {
    if (studentRepository.existsById(student.getStudentNumber())) {
      throw new EntityExistsException("Tried to create student but student already exists");
    } else {
      return studentRepository.save(student);
    }
  }

  public Student updateStudent(Student student) {
    if (!studentRepository.existsById(student.getStudentNumber())) {
      throw new EntityNotFoundException("Tried to update student but student was not found");
    } else {
      val studentOptional = studentRepository.findById(student.getStudentNumber());
      if (studentOptional.isPresent()) {
        return studentRepository.save(student);
      } else {
        throw new IllegalStateException("Student existsById yet cannot be foundById");
      }
    }
  }

  public Student deleteStudent(Long studentNumber) {
    if (!studentRepository.existsById(studentNumber)) {
      throw new EntityNotFoundException("Tried to delete student but student was not found");
    } else {
      val studentForDeletion = studentRepository.findById(studentNumber);
      if (studentForDeletion.isPresent()) {
        studentRepository.deleteById(studentNumber);
        return studentForDeletion.get();
      } else {
        throw new IllegalStateException("Student existsById yet cannot be foundById");
      }
    }
  }
}
