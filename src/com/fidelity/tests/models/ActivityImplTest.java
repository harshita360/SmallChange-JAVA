package com.fidelity.tests.models;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.fidelity.exceptions.ActivityException;
import com.fidelity.models.Trade;
import com.fidelity.repository.ActivityRepository;
import com.fidelity.repository.impl.ActivityRepositoryImpl;

@TestInstance(Lifecycle.PER_CLASS)
public class ActivityImplTest {
	ActivityRepository activity =null;
	
	@BeforeAll
	public void setUp() {
		activity = new ActivityRepositoryImpl();
		Trade trade1=new Trade("trade-01","B",null,new BigInteger("123"),"port-1","ins-1",LocalDateTime.now(),12,new BigDecimal(60),new BigDecimal(5));
		Trade trade2=new Trade("trade-01","B",null,new BigInteger("099"),"port-2","ins-1",LocalDateTime.now(),12,new BigDecimal(60),new BigDecimal(5));
		ArrayList<Trade> trades= new ArrayList<Trade>();
		trades.add(trade1);
		trades.add(trade2);
		activity.setActivities(trades);
	}
	
	@AfterAll
	public void tearDown() {
		activity = null;
	}
	
	@Test
	public void addActivitySuccessTest() {
		Trade trade = new Trade("trade-01","B",null,new BigInteger("890"),"port-1","ins-1",LocalDateTime.now(),12,new BigDecimal(60),new BigDecimal(5));
		activity.addActivity(trade);
		assertNotEquals(activity.getUserActivity(trade.getClientId()).size(),0);
	}
	
	@Test
	public void addActivityFailureTest() {
		assertThrows(NullPointerException.class,()->{
			activity.addActivity(null);
		});
	}
	
	@Test
	public void getUserActivitySuccessTest1() {
		assertNotEquals(activity.getUserActivity(new BigInteger("123")).size(),0);
	}
	
	@Test
	public void getUserActivitySuccessTest2() {
		assertEquals(activity.getUserActivity(new BigInteger("345")).size(),0);
	}
	
	@Test
	public void getUserActivityFailureTest1() {
		assertThrows(IllegalArgumentException.class,()->{
			activity.getUserActivity(new BigInteger("-2"));
		});
	}
	
	@Test
	public void getUserActivityFailureTest2() {
		assertThrows(NullPointerException.class,()->{
			activity.getUserActivity(null);
		});
	}
	
	@Test
	public void getPortfolioActivitySuccessTest1() {
		assertNotNull(activity.getPortfolioActivity("port-2"));
	}
	
	@Test
	public void getPortfolioActivitySuccessTest2() {
		assertEquals(activity.getPortfolioActivity("port-4").size(),0);
	}
	
	@Test
	public void getPortfolioActivityFailureTest1() {
		assertThrows(IllegalArgumentException.class,()->{
			activity.getPortfolioActivity("");
		});
	}
	
	@Test
	public void getPortfolioActivityFailureTest2() {
		assertThrows(NullPointerException.class,()->{
			activity.getPortfolioActivity(null);
		});
	}
	
	@Test
	public void deleteActivityClientIdSuccessTest() {
		activity.deleteActivityClientId(new BigInteger("123"));
		assertEquals(activity.getUserActivity(new BigInteger("123")).size(),0);
	}
	
	@Test
	public void deleteActivityPortfolioIdSuccessTest() {
		activity.deleteActivityPortfolioId("port-2");
		assertEquals(activity.getPortfolioActivity("port-2").size(),0);
	}
	
	@Test
	public void deleteActivityClientIdFailureTest1() {
		assertThrows(NullPointerException.class,()->{
			activity.deleteActivityClientId(null);
		});
	}
	
	@Test
	public void deleteActivityClientIdFailureTest2() {
		assertThrows(IllegalArgumentException.class,()->{
			activity.deleteActivityClientId(new BigInteger("-23"));
		});
	}
	
	@Test
	public void deleteActivityClientIdFailureTest3() {
		assertThrows(ActivityException.class,()->{
			activity.setActivities(new ArrayList<Trade>());
			activity.deleteActivityClientId(new BigInteger("123"));
		});
	}
	
	@Test
	public void deleteActivityClientIdFailureTest4() {
		assertThrows(ActivityException.class,()->{
			activity.deleteActivityClientId(new BigInteger("457"));
		});
	}
	
	@Test
	public void deleteActivityPortfolioIdFailureTest1() {
		assertThrows(NullPointerException.class,()->{
			activity.deleteActivityPortfolioId(null);
		});
	}
	
	@Test
	public void deleteActivityPortfolioIdFailureTest2() {
		assertThrows(IllegalArgumentException.class,()->{
			activity.deleteActivityPortfolioId("");
		});
	}
	
	@Test
	public void deleteActivityPortfolioIdFailureTest3() {
		assertThrows(ActivityException.class,()->{
			activity.setActivities(new ArrayList<Trade>());
			activity.deleteActivityPortfolioId("port-01");
		});
	}
	
	@Test
	public void deleteActivityPortfolioIdFailureTest4() {
		assertThrows(ActivityException.class,()->{
			activity.deleteActivityPortfolioId("port-03");
		});
	}
	
	

}
