package com.wanxiu.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface SlitMoneyService {

    void slitMoney(String slitType, int coin, BigDecimal money , String useType, String customerUser, String payeeUser);

}

