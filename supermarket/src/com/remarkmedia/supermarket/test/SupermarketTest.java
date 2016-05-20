/**
 * 
 */
package com.remarkmedia.supermarket.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import com.remarkmedia.supermarket.main.Cashier;
import com.remarkmedia.supermarket.main.Customer;
import com.remarkmedia.supermarket.main.Good;
import com.remarkmedia.supermarket.main.Supermarket;


/**
 * 超市单元测试
 * @description 
 * @author ZORO
 * @date 2016-5-19
 */
public class SupermarketTest {
	private static Supermarket supermarket;
	@BeforeClass
	public static void testInitGoods(){
		supermarket = new Supermarket("onepiece");
		supermarket.initGoods("Apple",15);
		supermarket.initGoods("Macbook",15);
		supermarket.initGoods("Cookie",15);			
	}
	@Test
	public void testGetRandomGood(){
		for(int i =0;i<45;i++){
			supermarket.getRandomGood();
		}
		Assert.assertTrue(supermarket.getRandomGood()==null);			
	}
	@Test 
	public void testStart(){
		Supermarket market = new Supermarket("onepiece");
		market.start();
	}
	@Test 
	public void testStatInfo()throws Exception{
		Supermarket market = new Supermarket("onepiece");
		CountDownLatch latch = new CountDownLatch(1);
		Cashier cashier = new Cashier(market,latch,"testCasiher");
		Customer cust = new Customer("testCust");
		Good good = new Good("Apple");
		good.setInitTime(System.currentTimeMillis());
		cust.setInitTime(System.currentTimeMillis());
		TimeUnit.SECONDS.sleep(1);
		cust.setHandleTime(System.currentTimeMillis());
		TimeUnit.SECONDS.sleep(1);
		good.setSellTime(System.currentTimeMillis());
		cust.setGood(good);
		cashier.getReceiveList().add(cust);
		List<Cashier> list = new ArrayList<Cashier>();
		list.add(cashier);
		market.setCashierList(list);
		market.statInfo();
	}
}
