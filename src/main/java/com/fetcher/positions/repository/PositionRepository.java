package com.fetcher.positions.repository;

import com.fetcher.positions.entity.Position;
import com.fetcher.positions.entity.PositionType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

    @Query("SELECT p FROM Position p WHERE (:type is null or p.type = :type) and (:location is null"
            + " or p.location = :location) and (:name is null or p.name = :name)")
    List<Position> findByTypeAndLocationAndName(PositionType type, String location, String name, Pageable pageable);

}
