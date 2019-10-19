package com.wanxiu.service;

import com.wanxiu.common.ServiceRes;
import com.wanxiu.dto.AnchorBalanceDTO;
import com.wanxiu.dto.BalanceDTO;
import com.wanxiu.dto.res.ResEntity;
import com.wanxiu.entity.LiveAccount;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {

    /**
     * 增加收入
     * @param accountID 账户id
     * @param createUser 操作用户id
     * @param coins 秀币数量
     * @param changeType 参考 AccountParam.CHANGE_TYPE
     * @return 查不到账户返null  成功返回更新后LiveAccount
     */
    ServiceRes<LiveAccount> change(String accountID, String createUser, int coins, int changeType);

    String getAccountId(String userId);


    AnchorBalanceDTO getAnchorBalance(String userId);

    ResEntity applyForWithdraw(String userId, String coin);

//    BalanceDTO getBalance(String userId);

}
