package com.mburu.student_api.repository;

import com.mburu.student_api.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    @EntityGraph(attributePaths = {"students"})
    @Override
    List<Department> findAll();
    boolean existsByName(String name);
    Optional<Department> findById(Long id);
    Optional<Department> findByName(String name);


}
