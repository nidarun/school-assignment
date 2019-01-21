package nur.nuradin.assignments.school.service;

import lombok.val;
import nur.nuradin.assignments.school.database.CourseRepository;
import nur.nuradin.assignments.school.domain.Course;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@Service
@Transactional(isolation = Isolation.SERIALIZABLE)
public class CourseService {

  private final CourseRepository courseRepository;

  public CourseService(CourseRepository courseRepository) {
    this.courseRepository = courseRepository;
  }

  public Course createCourse(Course course) {
    course.setCourseCode(course.getCourseCode().toUpperCase());
    if (courseRepository.existsById(course.getCourseCode())) {
      throw new EntityExistsException("Tried to create course but course already exists");
    } else {
      return courseRepository.save(course);
    }
  }

  public Course updateCourse(Course course) {
    course.setCourseCode(course.getCourseCode().toUpperCase());
    if (!courseRepository.existsById(course.getCourseCode())) {
      throw new EntityNotFoundException("Tried to update course but course was not found");
    } else {
      val courseBeforeUpdate = courseRepository.findById(course.getCourseCode());
      if (courseBeforeUpdate.isPresent()) {
        return courseRepository.save(course);
      } else {
        throw new IllegalStateException("Course existsById yet cannot be foundById");
      }
    }
  }

  public Course deleteCourse(String courseCode) {
    val courseIdentifier = courseCode.toUpperCase();
    if (!courseRepository.existsById(courseIdentifier)) {
      throw new EntityNotFoundException("Tried to delete course but course was not found");
    } else {
      val courseForDeletion = courseRepository.findById(courseIdentifier);
      if (courseForDeletion.isPresent()) {
        courseRepository.deleteById(courseForDeletion.get().getCourseCode().toUpperCase());
        return courseForDeletion.get();
      } else {
        throw new IllegalStateException("Course existsById yet cannot be foundById");
      }
    }
  }
}
