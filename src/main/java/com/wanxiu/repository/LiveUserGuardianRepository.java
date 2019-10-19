package com.wanxiu.repository;

import com.wanxiu.entity.LiveUserGuardian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface LiveUserGuardianRepository extends JpaRepository<LiveUserGuardian,String> {

    Optional<LiveUserGuardian> findByTargetId(Integer targetId);

    Optional<LiveUserGuardian> findByUserIdAndTargetId(String userId,Integer targetId);

    Optional<LiveUserGuardian> findByUserIdAndTargetIdAndEndTimeGreaterThan(String userId, Integer targetId, Date endTime);

    LiveUserGuardian save(LiveUserGuardian liveUserGuardian);
}
