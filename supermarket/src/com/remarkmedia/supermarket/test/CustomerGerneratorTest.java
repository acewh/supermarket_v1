package com.remarkmedia.supermarket.test;

import org.junit.BeforeClass;
import org.junit.Test;

import com.remarkmedia.supermarket.main.CustomerGenerator;
import com.remarkmedia.supermarket.main.Supermarket;
/**
 * 顾客生成器单元测试
 * @description 
 * @author ZORO
 * @date 2016-5-19
 */
public class CustomerGerneratorTest {
	private static Supermarket supermarket;
	@BeforeClass
	public static void testInitGoods(){
		supermarket = new Supermarket("onepiece");
		supermarket.initGoods("Apple",15);
		supermarket.initGoods("Macbook",15);
		supermarket.initGoods("Cookie",15);			
	}
	@Test
	public void testRun(){
		CustomerGenerator generator = new CustomerGenerator(supermarket);
		generator.run();
	}
}
