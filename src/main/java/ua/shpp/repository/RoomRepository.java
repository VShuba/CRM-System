package ua.shpp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.shpp.entity.RoomEntity;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    boolean existsByName(String name);

    boolean existsByNameAndBranchId(String name, Long branchId);

    Page<RoomEntity> findAllByBranchId(Long branchId, Pageable pageRequest);
}
