package com.remarkmedia.supermarket.test;

import java.util.concurrent.CountDownLatch;

import junit.framework.Assert;

import org.junit.Test;

import com.remarkmedia.supermarket.main.Cashier;
import com.remarkmedia.supermarket.main.Customer;
import com.remarkmedia.supermarket.main.Good;
import com.remarkmedia.supermarket.main.Supermarket;
/**
 * 收银员单元测试
 * @description 
 * @author ZORO
 * @date 2016-5-19
 */
public class CashierTest {
	@Test
	public void testHandleCustRequest(){
		Supermarket supermarket = new Supermarket("test");
		CountDownLatch latch = new CountDownLatch(1);
		Customer cust = new Customer("testCust");
		cust.setGood(new Good("Apple"));
		Cashier cashier = new Cashier(supermarket,latch,"testCashier");
		cashier.handleCustomer(cust);
		
		Assert.assertEquals(cashier.getReceiveList().size(),1);
	}
	@Test
	public void testRun()throws Exception{
		Supermarket supermarket = new Supermarket("test");
		Customer cust = new Customer("testCust");
		cust.setGood(new Good("Apple"));
		supermarket.getCustomerQueue().put(cust);
		supermarket.setSoldOut(true);
		CountDownLatch latch = new CountDownLatch(1);
		Cashier cashier = new Cashier(supermarket,latch,"testCashier");
		cashier.run();
	}
}
