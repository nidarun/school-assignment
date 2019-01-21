package nur.nuradin.assignments.school.domain;

import lombok.Value;

@Value
public class CourseChangeRequest {
  Student student;
  Course course;
}
