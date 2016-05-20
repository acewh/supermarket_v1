package com.remarkmedia.supermarket.main;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
/**
 * 顾客生成线程
 * @description create a customer in 1-3 second 
 * @author ZORO
 * @date 2016-5-17
 */
public class CustomerGenerator implements Runnable{
	private Logger logger = Logger.getLogger(CustomerGenerator.class);
	/**
	 * 需要生成顾客的超市
	 */
	private final Supermarket supermarket;
	public CustomerGenerator(Supermarket _supermarket){
		this.supermarket = _supermarket;
	}	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Random rand = new Random();
		int customerIndex = 0;
		boolean isRunning = true;
		while(isRunning){
			int sleep = rand.nextInt(3)+1;
			try {
				TimeUnit.SECONDS.sleep(sleep);
				customerIndex++;
				Customer cust = new Customer("Customer"+customerIndex);
				Good good = supermarket.getRandomGood();
				if(good!=null){
					//set the start waiting time 
					cust.setInitTime(System.currentTimeMillis());
					cust.setGood(good);
					logger.info(cust.getName()+" 到来!!,购买了一个:"+cust.getGood().getName());
					supermarket.getCustomerQueue().put(cust);
				}else{
					isRunning = false;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
