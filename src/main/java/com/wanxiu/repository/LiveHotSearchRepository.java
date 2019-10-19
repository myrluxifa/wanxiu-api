package com.wanxiu.repository;

import com.wanxiu.entity.LiveAccount;
import com.wanxiu.entity.LiveHotSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LiveHotSearchRepository extends JpaRepository<LiveHotSearch,String> {

   List<LiveHotSearch> findByStatusOrderByIsAdminDescCountDesc(Integer status);

   Optional<LiveHotSearch> findByContentAndStatus(String keywords, Integer status);
}
