package com.wanxiu.repository;

import com.wanxiu.entity.LiveGiftRank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LiveGiftRankRepository extends CrudRepository<LiveGiftRank,String>, JpaSpecificationExecutor<LiveGiftRank> {

   List<LiveGiftRank> findByShowIdAndUserTypeAndTypeIn(Pageable pageAble, String showId, String userType, List<String> type);

   List<LiveGiftRank> findByShowIdAndUserTypeAndTypeAndCycleId(Pageable pageAble, Integer showId, String userType, String type, String cycleId);

    List<LiveGiftRank> findByShowIdAndUserTypeAndTypeAndCycleIdOrderByCoinsDesc(Pageable pageAble, Integer showId, String userType, String type, String cycleId);

   Iterable<LiveGiftRank> findAll();

   <S extends LiveGiftRank> S save(S s);

    LiveGiftRank findByShowIdAndUserIdAndCycleId(Integer showId, String userId, String dCycleId);

    LiveGiftRank findByUserIdAndCycleIdAndUserType(String userId, String all, String userType);

    LiveGiftRank findByShowIdAndUserIdAndUserTypeAndCycleId(Integer showId, String userId, String userType, String dCycleId);

    Long countByShowIdAndUserTypeAndTypeAndCycleId(Integer showId, String userType, String type, String cycleId);
}
