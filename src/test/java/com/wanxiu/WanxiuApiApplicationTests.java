package com.wanxiu;

import com.google.gson.Gson;
import com.wanxiu.common.AccountParam;
import com.wanxiu.service.AccountService;
import com.wanxiu.service.GiftService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class WanxiuApiApplicationTests {

	@Autowired
	AccountService accountService;

	@Autowired
	GiftService giftService;

//	@Test
	public void contextLoads() {

		int i=1;
		String coin=String.valueOf(i/100);
		System.out.print(coin);
	}

//	@Test
	public void accountService() {
//		System.out.println(new Gson().toJson(accountService.change("123", "test", 100, AccountParam.CHANGE_TYPE_FROM_GIFT)));
//		System.out.println(new Gson().toJson(accountService.change("1234", "test", 100, AccountParam.CHANGE_TYPE_FROM_GIFT)));
//		System.out.println(new Gson().toJson(accountService.change("1234", "test", 100, AccountParam.CHANGE_TYPE_EXPEND)));
		System.out.println(new Gson().toJson(accountService.change("1234", "test", 100, AccountParam.CHANGE_TYPE_RECHARGE)));
	}

//	@Test
	public void buyGift() {
		System.out.println(new Gson().toJson(giftService.buyGift("402881f86a09dfd0016a09f345c80000", 103088, "1", 1,false)));
	}
}

