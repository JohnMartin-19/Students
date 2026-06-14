packaage com.mburu.student_api.service;

import com.mburu.student_api.dto.DepartmentRequest;
import com.mburu.student_api.dto.DepartmentResponse;
import com.mburu.student_api.entity.Department;
import com.mburu.student_api.repository.DepartmentRepository;
import com.mburu.student_api.exception.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private static final Logger log = LoggerFactory.getLogger(DepartmentService.class);
    private final DepartmentRepository departmentRepository;

//post request for creating a new department
    public DepartmentResponse create(DepartmentRequest request) {

        //check if the department exists
        if(departmentRepository.existsByName(request.getName())) {
            log.warn("Error creating the department: {}" , request.getName());
            throw new DuplicateResourceException("Department already exists: {}", request.getName());

        }
        Department department = Department.builder()
                .name(request.getName())
                .build();
        Department saved = departmentRepository.save(department);
        log.info("Department created successfully with ID: {}", saved.getId());
    }

    //get request - get department by ID

    public DepartmentResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }


    //used to handle get Request for all depts
    public Page<DepartmentResponse> getAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortBy));
        return departmentRepository.findAll(pageable).map(this::toResponse);
    }

    //paginated get request - searching
    public Page<DepartmentResponse> search(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
        return departmentRepository.search(keyword, pageable).map(this::toResponse);
    }

    //put request - to updated the department resource
    public DepartmentResponse update(Long id, DepartmentRequest request) {
        Department department = findOrThrow(id);
        department.setName(request.getName);
        return toResponse(departmentRepository.save(department))
    }

    //delete endpoint
    public void delete(Long id) {
        findOrThrow(id);
        departmentRepository.deleteById(id);
    }

    //helper methods

    private Department findOrThrow(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found with id: " + id));
    }

    private DepartmentResponse toResponse(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .studentName(
                        department.getStudent() != null
                                ? department.getStudent().getName()
                                : null)
                .createdAt(department.getCreatedAt())
                .updatedAt(department.getUpdatedAt())
                .build();
    }

}
