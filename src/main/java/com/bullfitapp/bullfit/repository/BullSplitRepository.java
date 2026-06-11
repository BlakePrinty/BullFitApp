package com.bullfitapp.bullfit.repository;

import com.bullfitapp.bullfit.model.entity.BullSplit;
import com.bullfitapp.bullfit.model.entity.User;
import com.bullfitapp.bullfit.model.enums.SplitCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BullSplitRepository extends JpaRepository<BullSplit, Long> {
    List<BullSplit> findByCreatedByOrderByCreatedAtDesc(User createdBy);

    // Finds splits the user has saved via the user_saved_splits join table.
    @Query("SELECT s FROM BullSplit s JOIN s.savedByUsers u WHERE u.id = :userId")
    List<BullSplit> findSavedByUserId(@Param("userId") Long userId);

    // Explore feed — published splits, optional category filter.
    @Query("SELECT s FROM BullSplit s WHERE s.published = true " +
            "AND (:category IS NULL OR s.category = :category)")
    Page<BullSplit> findExplore(
            @Param("category") SplitCategory category,
            Pageable pageable);

    // Used before saving to prevent duplicate entries in user_saved_splits.
    @Query("SELECT COUNT(u) FROM BullSplit s JOIN s.savedByUsers u " +
            "WHERE s.id = :splitId AND u.id = :userId")
    long countSavedByUser(@Param("splitId") Long splitId, @Param("userId") Long userId);

    // Native query to remove a row from the join table without loading the collection.
    // clearAutomatically = true evicts the split from the EntityManager cache after execution.
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM user_saved_splits WHERE split_id = :splitId AND user_id = :userId",
            nativeQuery = true)
    int removeSavedByUser(@Param("splitId") Long splitId, @Param("userId") Long userId);
}
