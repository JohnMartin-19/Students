packge com.mburu.student_api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

improt java.util.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "department")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 150)
    private String name;

    //onetomany(Dept -> Students)
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> student = new ArrayList<>();

    @CreatedDate
    @Column(updateable = false)
    private LocalDatetime createdAt;

    @LastModifiedAt
    @Column(updateable = true)
    private LocalDateTime updatedAt;




}