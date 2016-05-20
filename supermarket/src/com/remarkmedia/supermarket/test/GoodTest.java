package com.remarkmedia.supermarket.test;

import org.junit.Test;
import com.remarkmedia.supermarket.main.Good;
/**
 * 物品单元测试
 * @description 
 * @author ZORO
 * @date 2016-5-19
 */
public class GoodTest {
	@Test
	public void goodTest(){
		Good good = new Good("Apple");
		good.setInitTime(System.currentTimeMillis());
		good.setName("Cookie");
		good.setSellTime(System.currentTimeMillis());
		good.getInitTime();
		good.getName();
		good.getSellTime();
		good.getSoldTime();
	}
}
