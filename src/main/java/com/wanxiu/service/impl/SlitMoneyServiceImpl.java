package com.wanxiu.service.impl;

import com.wanxiu.common.Common;
import com.wanxiu.common.SlitMoneyParam;
import com.wanxiu.entity.*;
import com.wanxiu.repository.*;
import com.wanxiu.service.LevelService;
import com.wanxiu.service.SlitMoneyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Component
public class SlitMoneyServiceImpl implements SlitMoneyService {

    @Autowired
    private LiveSysSlitRatioRepository liveSysSlitRatioRepository;

    @Autowired
    private LiveSlitMoneyLogRepository liveSlitMoneyLogRepository;

    @Autowired
    private LiveSysRatioRepository liveSysRatioRepository;

    @Autowired
    private LiveCashAccountRepository liveCashAccountRepository;

    @Autowired
    private LiveUserRepository liveUserRepository;

    @Autowired
    private LevelService levelService;

    private static final Logger log = LoggerFactory.getLogger(SlitMoneyServiceImpl.class);


    public void slitMoney(String slitType, int coin, BigDecimal money ,String useType, String customerUser, String payeeUser) {

        log.info("开始分账");
        Optional<LiveSysSlitRatio> optionalLiveSysSlitRatio =liveSysSlitRatioRepository.findByType(slitType);
        int slitRatio=100;
        //分账比例
        if(optionalLiveSysSlitRatio.isPresent()){
            LiveSysSlitRatio liveSysSlitRatio
                    =optionalLiveSysSlitRatio.get();
            slitRatio=liveSysSlitRatio.getRatio();
        }

        if(slitType.equals(SlitMoneyParam.SLIT_TYPE_PLATFORM)){
            money=money.multiply(new BigDecimal(slitRatio)).divide(new BigDecimal(100),2,BigDecimal.ROUND_UP);

        }else{
            int ratio=liveSysRatioRepository.findAll().iterator().next().getRatio();
            //通过秀币与现金比例转化成现金
            BigDecimal _coin=new BigDecimal(coin).divide(new BigDecimal(ratio),2,BigDecimal.ROUND_UP);

            //再按提点比例分层
            money=_coin.multiply(new BigDecimal(slitRatio)).divide(new BigDecimal(100),2,BigDecimal.ROUND_UP);

            Optional<LiveUser> optionalLiveUser=liveUserRepository.findById(customerUser);
            if(optionalLiveUser.isPresent()){
                LiveUser liveUser=optionalLiveUser.get();
                if(liveUser.getPart().equals(Common.PART_ANCHOR_USER)){
                    Optional<LiveCashAccount> optionalLiveCashAccount=liveCashAccountRepository.findByUserId(customerUser);
                    if(optionalLiveCashAccount.isPresent()){
                        LiveCashAccount liveCashAccount=optionalLiveCashAccount.get();
                        BigDecimal fee=liveCashAccount.getFee();
                        fee=fee.subtract(money);
                        liveCashAccount.setFee(fee);
                        liveCashAccountRepository.save(liveCashAccount);
                    }
                }
            }

            //收益人账户
            Optional<LiveCashAccount> optionalLiveCashAccount=liveCashAccountRepository.findByUserId(payeeUser);
            if(optionalLiveCashAccount.isPresent()){
                LiveCashAccount liveCashAccount=optionalLiveCashAccount.get();
                BigDecimal fee=liveCashAccount.getFee();
                fee=fee.add(money);
               liveCashAccount.setFee(fee);
               liveCashAccountRepository.save(liveCashAccount);
            }

        }
        liveSlitMoneyLogRepository.save(new LiveSlitMoneyLog(slitType,
                money,
                slitRatio,
                new Date(),
                customerUser,
                useType,
                payeeUser,
                coin));
    }
}
