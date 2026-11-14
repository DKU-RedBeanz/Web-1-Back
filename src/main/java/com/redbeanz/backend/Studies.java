package com.redbeanz.backend;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
@Entity
@Table(name = "Studies")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Studies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "title", length = 100, nullable = false)
    private String title;
    @Column(name = "summary", length = 255, nullable = false)
    private String summary;
    @Column(name = "descriprion")
    private String description;
    @Column(name = "goal", length = 30, nullable = false)
    private String goal;
    @Column(name = "level", length = 30, nullable = false)
    private String level;
    @Column(name = "time", length = 30, nullable = false)
    private String time;
    @Column(name = "mode", length = 30, nullable = false)
    private String mode;
    @Column(name = "region", length = 30)
    private String region;
    @Column(name = "location", length = 100)
    private String location;
    @Column(name = "member_count", nullable = false)
    private Long member_count = 0L;
    @Column(name = "capacity", nullable = false)
    private Long capacity;
    @Column(name = "leader_nickname", length = 50, nullable = false)
    private String leader_nickname;
    @Column(name = "created_at", nullable = false)
    private String created_at;
    @CreationTimestamp // INSERT 시 현재 시간이 자동으로 들어감
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}