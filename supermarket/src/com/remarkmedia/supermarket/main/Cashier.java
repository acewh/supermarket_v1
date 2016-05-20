package com.remarkmedia.supermarket.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/**
 * 营业员类
 * @description handle a Customer Request in 5-10 seconds   
 * @author ACE
 * @date 2016-5-16
 */
public class Cashier implements Runnable{
	private Logger logger = Logger.getLogger(Cashier.class);
	//用于统计收银结束
	private final CountDownLatch latch;
	private String name;
	/**
	 * 营业员接待的顾客集合
	 */
	private List<Customer> receiveList;
	/**
	 * 营业员所属的超市
	 */
	private final Supermarket supermarket;
	public List<Customer> getReceiveList() {
		return receiveList;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}	
	public Cashier(Supermarket _supermarket,CountDownLatch _latch,String _name){
		receiveList = new ArrayList<Customer>();
		this.supermarket = _supermarket;
		this.name = _name;
		this.latch = _latch;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub	
		try{
			while(!supermarket.isClosed()){
				Customer customer = supermarket.getCustomerQueue().poll();				
				if(customer!=null){				
					//handler customer		
					handleCustomer(customer);
					logger.info(name+"  处理了  "+customer.getName()+"的 购物请求!");
				}
			}
		}finally{
			latch.countDown();
		}
	}
	/**
	 * 模拟处理顾客请求，处理一个请求需要5~10秒
	 * @param cust
	 */
	public void handleCustomer(Customer cust){
		Random rand = new Random();
		int seed = rand.nextInt(6)+5;
		try {
			TimeUnit.SECONDS.sleep(seed);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cust.getGood().setSellTime(System.currentTimeMillis());
		cust.setHandleTime(System.currentTimeMillis());		
		receiveList.add(cust);
	}
}
