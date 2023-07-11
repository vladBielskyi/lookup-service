package com.lookup.service.repository;

import com.lookup.service.entity.BinInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BinInfoRepository extends CrudRepository<BinInfo, String> {

    @Query("SELECT b FROM BinInfo b WHERE :card BETWEEN b.minRange AND b.maxRange ORDER BY b.created ASC")
    Optional<List<BinInfo>> findAllByCardInRangeOrderByCreatedAsc(@Param("card") String card);

    @Modifying
    @Query("DELETE FROM BinInfo e WHERE e.created < :created")
    Integer deleteAllByCreated(@Param("created") LocalDateTime created);
}
