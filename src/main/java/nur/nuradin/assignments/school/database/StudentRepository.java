package nur.nuradin.assignments.school.database;

import nur.nuradin.assignments.school.domain.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {}
