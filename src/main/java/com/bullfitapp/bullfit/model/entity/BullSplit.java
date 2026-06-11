package com.bullfitapp.bullfit.model.entity;

import com.bullfitapp.bullfit.model.enums.SplitCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "bull_splits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BullSplit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SplitCategory category;

    @Column(length = 500)
    private String tags;

    // Named 'published' so Lombok generates isPublished() — same pattern as 'custom' on Exercise.
    @Builder.Default
    @Column(name = "is_published", nullable = false)
    private boolean published = false;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Builder.Default
    @Column(name = "save_count", nullable = false)
    private int saveCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // CascadeType.ALL + orphanRemoval means deleting a day from this list
    // also deletes it from the DB — used by the update method.
    @Builder.Default
    @OneToMany(mappedBy = "bullSplit", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("dayNumber ASC")
    private List<SplitDay> days = new ArrayList<>();

    // Owning side of the M:M — Hibernate manages the user_saved_splits join table.
    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "user_saved_splits",
            joinColumns = @JoinColumn(name = "split_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> savedByUsers = new HashSet<>();
}
