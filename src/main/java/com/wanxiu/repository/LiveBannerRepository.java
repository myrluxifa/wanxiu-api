package com.wanxiu.repository;

import com.wanxiu.entity.LiveBanner;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LiveBannerRepository  extends CrudRepository<LiveBanner,String> {

   Optional<LiveBanner> findById(String id);

   Iterable<LiveBanner> findAll();

   <S extends LiveBanner> S save(S s);

   List<LiveBanner> findByStatusAndRoomTypeOrderBySortNum(int status,String roomType);

}
