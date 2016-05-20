package com.remarkmedia.supermarket.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.log4j.Logger;

/**
 * 超市主类，之前以为代码是发到国外，没怎么写注释
 * @description 
 * @author ACE 
 * @date 2016-5-16
 */
public class Supermarket{
	private Logger logger = Logger.getLogger(Supermarket.class);
	private String name;
	public Supermarket(String _name){
		this.name = _name;
	}
	/**
	 * 超市顾客等待队列
	 */
	private LinkedBlockingDeque<Customer> customerQueue = new LinkedBlockingDeque<Customer>();
	/**
	 * 超市库存
	 */
	private Map<String,LinkedList<Good>> repository = new HashMap<String, LinkedList<Good>>();
	/**
	 * 超市的收银员集合
	 */
	private List<Cashier> cashierList;		
	private ExecutorService exec = Executors.newCachedThreadPool();
	protected volatile boolean isSoldOut = false;
	/**
	 * 超市启动
	 */
	public void start(){
		// TODO Auto-generated method stub
		logger.info(name+"    开始!!!!");
		logger.info("初始化营业员!!!");
		cashierList = new ArrayList<Cashier>();
		CountDownLatch latch = new CountDownLatch(3);
		Cashier cashierA = new Cashier(this,latch,"Cashier_A");
		Cashier cashierB = new Cashier(this,latch,"Cashier_B");
		Cashier cashierC = new Cashier(this,latch,"Cashier_C");
		exec.execute(cashierA);
		exec.execute(cashierB);
		exec.execute(cashierC);
		cashierList.add(cashierA);
		cashierList.add(cashierB);
		cashierList.add(cashierC);
		//初始化商品
		initGoods("Apple",15);
		initGoods("Macbook",15);
		initGoods("Cookie",15);				
		//开始营业
		long beginSaleTime =System.currentTimeMillis();
		CustomerGenerator custGenerator = new CustomerGenerator(this);
		exec.execute(custGenerator);
		exec.shutdown();
		try {
			//等待收银结束
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long endSaleTime = System.currentTimeMillis();
		logger.info("从开始销售到售罄总共时间:"+(endSaleTime-beginSaleTime)/1000+"秒");
		//打印统计信息
		statInfo();
	}	
	public void setSoldOut(boolean isSoldOut) {
		this.isSoldOut = isSoldOut;
	}
	/**
	 * 初始化商品信息
	 * @param name name of goods
	 * @param count 
	 */
	public void initGoods(String name,long count){
		LinkedList<Good> list = new LinkedList<Good>();
		repository.put(name,list);		
		for(int i=1;i<=count;i++){
			Good good = new Good(name);
			//设置商品入库时间
			good.setInitTime(System.currentTimeMillis());
			list.add(good);
		}
		logger.info("初始化 "+name+","+count+"个  成功!!!");
	}
	/**
	 * 随机获取一件商品
	 * @return
	 */
	public Good getRandomGood(){
		List<String> remainGoods = new ArrayList<String>();
		for(String key:repository.keySet()){
			List goodList = repository.get(key);
			if(goodList.size()!=0){
				remainGoods.add(key);
			}
		}
		if(remainGoods.size()==0){
			logger.info("all of the goods sold out !!!!!");
			isSoldOut=true;			
			return null;
		}else if(remainGoods.size()==1){
			String goodName = remainGoods.get(0);
			return repository.get(goodName).pollFirst(); 
		}else{
			Random rand = new Random();
			int seed = rand.nextInt(remainGoods.size());
			String goodName = remainGoods.get(seed);
			return repository.get(goodName).pollFirst();
		}
	}	
	/**
	 * 统计营业情况
	 */
	public void statInfo(){
		long custWait = 0;
		long goodWait = 0;
		int custSize = 0;
		for(Cashier cashier:cashierList){
			custSize +=cashier.getReceiveList().size();
			logger.info(cashier.getName()+"接待的顾客数量:"+cashier.getReceiveList().size());
			for(Customer cust:cashier.getReceiveList()){
				custWait+=cust.getWaitTime();
				goodWait+=cust.getGood().getSoldTime();
			}
		}		
		//顾客按购买商品后时间开始计算
		logger.info("每个顾客平均等待时间:"+custWait/custSize/1000+"秒");
		//商品按入库时间开始计算
		logger.info("每个商品平均售出时间:"+goodWait/custSize/1000+"秒");				
	}
	/**
	 * 判断超市是否打洋
	 * @return
	 */
	public boolean isClosed(){
		return isSoldOut&&customerQueue.size()==0;
	}
	/**
	 * 获取客户队列
	 * @return
	 */
	public LinkedBlockingDeque<Customer> getCustomerQueue() {
		return customerQueue;
	}		
	public List<Cashier> getCashierList() {
		return cashierList;
	}
	public void setCashierList(List<Cashier> cashierList) {
		this.cashierList = cashierList;
	}
}
