package nur.nuradin.assignments.school.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Map;

@Data
@Entity
public class Student {

  @Id private Long studentNumber;
  private String firstName;
  private String lastName;
  @ElementCollection
  private Map<Course, String> courseGradesMap;
}
