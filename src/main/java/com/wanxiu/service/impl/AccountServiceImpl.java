package com.wanxiu.service.impl;


import com.alipay.api.domain.Account;
import com.wanxiu.common.AccountParam;
import com.wanxiu.common.CoinsLogParam;
import com.wanxiu.common.Common;
import com.wanxiu.common.ServiceRes;
import com.wanxiu.dto.AnchorBalanceDTO;
import com.wanxiu.dto.BalanceDTO;
import com.wanxiu.dto.CashWithrawalDTO;
import com.wanxiu.dto.res.ResEntity;
import com.wanxiu.entity.*;
import com.wanxiu.mapper.CashWithrawalMapper;
import com.wanxiu.repository.*;
import com.wanxiu.service.AccountService;
import com.wanxiu.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 账户操作
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    LiveAccountRepository accountRepository;

    @Autowired
    LiveCoinsLogRepository coinsLogRepository;

    @Autowired
    LiveCashWithdrawalRepository liveCashWithdrawalRepository;

    @Autowired
    CashWithrawalMapper cashWithrawalMapper;

    @Autowired
    LiveSysRatioRepository liveSysRatioRepository;

    @Autowired
    LiveUserRepository liveUserRepository;

    @Override
    @Transactional
    public synchronized ServiceRes<LiveAccount> change(String accountID, String createUser, int coins, int changeType) {
        try {
            Optional<LiveAccount> account = accountRepository.findById(accountID);
            if(account.isPresent()) {
                LiveAccount la = account.get(); //账户
                LiveCoinsLog log = new LiveCoinsLog();//日志
                log.setUserId(createUser);
                switch (changeType){
                    case AccountParam.CHANGE_TYPE_FROM_GIFT:
                        log.setType(CoinsLogParam.TYPE_INCOME_GIFT);
                        log.setOldCoins(la.getCoins());
                        la.setCoins(la.getCoins());
                        la.setGiftAmount(la.getGiftAmount() + coins);
                        la.setIncomeAmount(la.getIncomeAmount() + coins);
                        log.setUserId(la.getUserId());
                        log.setNewCoins(la.getCoins());
                        break;
                    case AccountParam.CHANGE_TYPE_NOT_FROM_GIFT:
                        log.setType(CoinsLogParam.TYPE_INCOME);
                        log.setOldCoins(la.getCoins());
                        la.setCoins(la.getCoins() + coins);
                        la.setIncomeAmount(la.getIncomeAmount() + coins);
                        log.setUserId(la.getUserId());
                        log.setNewCoins(la.getCoins());
                        break;
                    case AccountParam.CHANGE_TYPE_INCOME_MANAGER:
                        log.setType(CoinsLogParam.TYPE_INCOME_MANAGER);
                        log.setOldCoins(la.getCoins());
                        la.setCoins(la.getCoins() + coins);
                        la.setIncomeAmount(la.getIncomeAmount() + coins);
                        log.setUserId(la.getUserId());
                        log.setNewCoins(la.getCoins());
                        break;
                    case AccountParam.CHANGE_TYPE_EXPEND_MANAGER:
                        if (la.getCoins() < coins) {
                            return new ServiceRes<>().fail(AccountParam.ERROR_FEE_NOT_ENOUGH, "秀币余额不足！");
                        }
                        log.setType(CoinsLogParam.TYPE_EXPEND_MANAGER);
                        log.setOldCoins(la.getCoins());
                        la.setCoins(la.getCoins() - coins);
                        log.setNewCoins(la.getCoins());
                        la.setExpendAmount(la.getExpendAmount() + coins);
                        break;
                    case AccountParam.CHANGE_TYPE_EXPEND_GIFT:
                        if (la.getCoins() < coins) {
                            return new ServiceRes<>().fail(AccountParam.ERROR_FEE_NOT_ENOUGH, "秀币余额不足！");
                        }
                        log.setType(CoinsLogParam.TYPE_EXPEND_GIFT);
                        log.setOldCoins(la.getCoins());
                        la.setCoins(la.getCoins() - coins);
                        log.setNewCoins(la.getCoins());
                        la.setExpendAmount(la.getExpendAmount() + coins);
                        break;
                    case AccountParam.CHANGE_TYPE_WITHDRAW:
                        if (la.getFee() < coins) {
                            return new ServiceRes<>().fail(AccountParam.ERROR_FEE_NOT_ENOUGH, "提现余额不足！");
                        }
                        log.setType(CoinsLogParam.TYPE_EXPEND_WITHDRAW);
                        la.setWithdrawAmount(la.getWithdrawAmount() + coins);
                        log.setOldCoins(la.getFee());
                        la.setFee(la.getFee() - coins);
                        log.setFee(coins);
                        log.setNewCoins(la.getFee());
                        break;
                    case AccountParam.CHANGE_TYPE_EXPEND:
                        if (la.getCoins() < coins) {
                            return new ServiceRes<>().fail(AccountParam.ERROR_FEE_NOT_ENOUGH, "秀币余额不足！");
                        }
                        log.setType(CoinsLogParam.TYPE_EXPEND);
                        log.setOldCoins(la.getCoins());
                        la.setCoins(la.getCoins() - coins);
                        log.setNewCoins(la.getCoins());
                        la.setExpendAmount(la.getExpendAmount() + coins);
                        break;
                    case AccountParam.CHANGE_TYPE_RECHARGE:
//                        la.setIncomeAmount(la.getIncomeAmount() + coins);//充值不计income
                        log.setType(CoinsLogParam.TYPE_RECHARGE);
                        log.setOldCoins(la.getCoins());
                        la.setCoins(la.getCoins() + coins);
                        log.setNewCoins(la.getCoins());
                        break;
                    default:;
                }

                accountRepository.save(la);

                // 记录流水
                log.setCoins(coins);
                log.setCreateTime(Calendar.getInstance().getTime());
                log.setCreateTime(Calendar.getInstance().getTime());
                log.setCreateUser(createUser);
                log.setTargetId(accountID);
                coinsLogRepository.save(log);
                return new ServiceRes<>().success(la);
            } else {
                return new ServiceRes<>().fail(AccountParam.ERROR_ACCOUNT_NON_EXISTS, "账户不存在！");
            }

        } catch (Exception e) {
            return new ServiceRes<>().fail(e.getMessage());
        }

    }

    @Override
    public String getAccountId(String userId) {
        Optional<LiveAccount> optionalAccount=accountRepository.findByUserId(userId);
        if(optionalAccount.isPresent()){
            return optionalAccount.get().getId();
        }else{
            return "-1";
        }
    }

    public AnchorBalanceDTO getAnchorBalance(String userId){
        Optional<LiveAccount> optionalAccount=accountRepository.findByUserId(userId);

        if(optionalAccount.isPresent()){
            int coins=optionalAccount.get().getCoins();
            List<LiveCashWithdrawal> liveCashWithdrawalList=liveCashWithdrawalRepository.findByUserId(userId);
            List<CashWithrawalDTO> cashWithrawalDTOList=new ArrayList<>();
            for(LiveCashWithdrawal withdrawal:liveCashWithdrawalList){
                CashWithrawalDTO cashWithrawalDTO =cashWithrawalMapper.getCashWithrawal(withdrawal, TimeUtil.simpleTime(withdrawal.getCreateTime()));
                cashWithrawalDTOList.add(cashWithrawalDTO);
            }
            return new AnchorBalanceDTO(String.valueOf(coins),cashWithrawalDTOList);
        }else{
            return new AnchorBalanceDTO();
        }
    }

    public ResEntity applyForWithdraw(String userId, String coin){
        Optional<LiveUser> optionalLiveUser=liveUserRepository.findById(userId);
        if(!optionalLiveUser.isPresent()) return new ResEntity(Common.RESULT.USER_404);
        if(StringUtils.isBlank(optionalLiveUser.get().getOpenId())){
            return new ResEntity(Common.RESULT.WECHAT_NOT_BOUND);
        }
        if(liveCashWithdrawalRepository.countByUserIdAndStatus(userId,0)>0) return new ResEntity(Common.RESULT.WITHDRAW_409);
        Optional<LiveAccount> optionalAccount=accountRepository.findByUserId(userId);
        if(optionalAccount.isPresent()){
            LiveAccount liveAccount=optionalAccount.get();
            if(liveAccount.getCoins()<Integer.valueOf(coin)){
                return new ResEntity(Common.RESULT.BALANCE_INSUFFICIENT);
            }
        }else{
            return new ResEntity(Common.RESULT.ACCOUNT_404);
        }
        BigDecimal ratio=new BigDecimal(liveSysRatioRepository.findAll().iterator().next().getRatio());
        BigDecimal cashFee=new BigDecimal(coin).divide(ratio);
        LiveCashWithdrawal liveCashWithdrawal=liveCashWithdrawalRepository.save(new LiveCashWithdrawal(userId,Integer.valueOf(coin),new Date(),0,cashFee));
        if(liveCashWithdrawal!=null)return new ResEntity(Common.RESULT.SUCCESS);
        return new ResEntity(Common.RESULT.FAIL);
    }

//    public BalanceDTO getBalance(String userId){
//        Optional<LiveAccount> optionalAccount=accountRepository.findByUserId(userId);
//
//        if(optionalAccount.isPresent()){
//            int coins=optionalAccount.get().getCoins();
//            List<LiveCashWithdrawal> liveCashWithdrawalList=liveCashWithdrawalRepository.findByUserId(userId);
//            List<CashWithrawalDTO> cashWithrawalDTOList=new ArrayList<>();
//            for(LiveCashWithdrawal withdrawal:liveCashWithdrawalList){
//                CashWithrawalDTO cashWithrawalDTO =cashWithrawalMapper.getCashWithrawal(withdrawal, TimeUtil.simpleTime(withdrawal.getCreateTime()));
//                cashWithrawalDTOList.add(cashWithrawalDTO);
//            }
//            return new AnchorBalanceDTO(String.valueOf(coins),cashWithrawalDTOList);
//        }else{
//            return new AnchorBalanceDTO();
//        }
//    }
}
