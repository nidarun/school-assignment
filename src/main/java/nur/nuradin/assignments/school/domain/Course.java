package nur.nuradin.assignments.school.domain;

import lombok.Data;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.util.List;

@Data
@Entity
public class Course {

  @Id private String courseCode;
  private String name;
}
