package com.wanxiu.service;

import org.springframework.stereotype.Service;

@Service
public interface LevelService {
    void addExp(int coin,String userId,int showId);
}
