package nur.nuradin.assignments.school.database;

import nur.nuradin.assignments.school.domain.Course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends CrudRepository<Course, String> {}
