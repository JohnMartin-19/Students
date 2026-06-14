package com.mburu.student_api.repository;

import com.mburu.student_api.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.EntityGraph;
import java.util.List;
import java.util.Optional;
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @EntityGraph(attributePaths = {"students"})
    @Override
    List<Department> findAll();

    boolean existsByName(String name);

    Optional<Department> findByName(String name);

    @Query("SELECT d FROM Department d WHERE LOWER(d.name LIKE LOWER(CONCAT('%', :KEYWORD, '%', ))")
    Page<Department> search(@Param("keyword") String keyword, Pageable pageable);


}
