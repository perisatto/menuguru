package com.perisatto.fiapprj.menuguru.application.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.perisatto.fiapprj.menuguru.handler.exceptions.NotFoundException;
import com.perisatto.fiapprj.menuguru.handler.exceptions.ValidationException;
import com.perisatto.fiapprj.menuguru.order.domain.model.Order;
import com.perisatto.fiapprj.menuguru.order.domain.model.OrderItem;
import com.perisatto.fiapprj.menuguru.order.domain.model.OrderStatus;
import com.perisatto.fiapprj.menuguru.order.domain.service.OrderService;
import com.perisatto.fiapprj.menuguru.order.port.out.OrderCustomerPort;
import com.perisatto.fiapprj.menuguru.order.port.out.OrderProductPort;

public class OrderServiceTest {
	
	private OrderCustomerPort orderCustomerPort;
	private OrderProductPort orderProductPort;

	@Test
	void givenValidParameters_thenCreateOrder() throws Exception {
		Long customerId = 1L;
		Long firstProductId = 1L;
		Double firstProductActualPrice = 35.50;
		Integer firstProductQuantity = 1;		
		OrderItem firstItem = new OrderItem(firstProductId, null, firstProductQuantity);

		Long secondProductId = 1L;
		Double secondProductActualPrice = 35.50;
		Integer secondProductQuantity = 1;		
		OrderItem secondItem = new OrderItem(secondProductId, null, secondProductQuantity);		

		Set<OrderItem> orderItems = new LinkedHashSet<OrderItem>();
		orderItems.add(firstItem);
		orderItems.add(secondItem);	


		OrderService orderService = new OrderService(orderCustomerPort, orderProductPort);

		Order order = orderService.createOrder(customerId, orderItems);

		assertThat(order.getId()).isGreaterThan(0);
		assertThat(order.getStatus()).isEqualTo(OrderStatus.RECEBIDO);
		assertThat(order.getTotalPrice()).isEqualTo(firstProductActualPrice + secondProductActualPrice);
	}

	@Test
	void givenNoCustomerId_thenCreateOrder() throws Exception {
		Long firstProductId = 10L;
		Double firstProductActualPrice = 35.50;
		Integer firstProductQuantity = 1;		
		OrderItem firstItem = new OrderItem(firstProductId, null, firstProductQuantity);

		Long secondProductId = 1L;
		Double secondProductActualPrice = 35.50;
		Integer secondProductQuantity = 1;		
		OrderItem secondItem = new OrderItem(secondProductId, null, secondProductQuantity);		

		Set<OrderItem> orderItems = new LinkedHashSet<OrderItem>();
		orderItems.add(firstItem);
		orderItems.add(secondItem);	


		OrderService orderService = new OrderService(orderCustomerPort, orderProductPort);

		Order order = orderService.createOrder(null, orderItems);

		assertThat(order.getId()).isGreaterThan(0);
		assertThat(order.getStatus()).isEqualTo(OrderStatus.RECEBIDO);
		assertThat(order.getTotalPrice()).isEqualTo(firstProductActualPrice + secondProductActualPrice);		
	}

	@Test
	void givenInvalidCustomerId_thenRefusesToCreateOrder() {
		try { 
			Long customerId = 0L;
			Long firstProductId = 1L;
			Integer firstProductQuantity = 1;		
			OrderItem firstItem = new OrderItem(firstProductId, null, firstProductQuantity);

			Long secondProductId = 1L;
			Integer secondProductQuantity = 1;		
			OrderItem secondItem = new OrderItem(secondProductId, null, secondProductQuantity);		

			Set<OrderItem> orderItems = new LinkedHashSet<OrderItem>();
			orderItems.add(firstItem);
			orderItems.add(secondItem);	


			OrderService orderService = new OrderService(orderCustomerPort, orderProductPort);

			Order order = orderService.createOrder(customerId, orderItems);
			
			assertTrue(false);
		} catch (Exception e) {
			assertThatExceptionOfType(ValidationException.class);
		}
	}
	
	@Test
	void givenInexistentCustomerId_thenRefusesToCreateOrder() {
		try { 
			Long customerId = 20L;
			Long firstProductId = 1L;
			Integer firstProductQuantity = 1;		
			OrderItem firstItem = new OrderItem(firstProductId, null, firstProductQuantity);

			Long secondProductId = 1L;
			Integer secondProductQuantity = 1;		
			OrderItem secondItem = new OrderItem(secondProductId, null, secondProductQuantity);		

			Set<OrderItem> orderItems = new LinkedHashSet<OrderItem>();
			orderItems.add(firstItem);
			orderItems.add(secondItem);	


			OrderService orderService = new OrderService(orderCustomerPort, orderProductPort);

			Order order = orderService.createOrder(customerId, orderItems);
			
			assertTrue(false);
		} catch (Exception e) {
			assertThatExceptionOfType(NotFoundException.class);
		}
	}
	
	@Test
	void givenInvalidProductId_thenRefusesToCreateOrder() {
		try { 
			Long customerId = 20L;
			Long firstProductId = 0L;
			Integer firstProductQuantity = 1;		
			OrderItem firstItem = new OrderItem(firstProductId, null, firstProductQuantity);

			Long secondProductId = null;
			Integer secondProductQuantity = 1;		
			OrderItem secondItem = new OrderItem(secondProductId, null, secondProductQuantity);		

			Set<OrderItem> orderItems = new LinkedHashSet<OrderItem>();
			orderItems.add(firstItem);
			orderItems.add(secondItem);	


			OrderService orderService = new OrderService(orderCustomerPort, orderProductPort);

			Order order = orderService.createOrder(customerId, orderItems);
			
			assertTrue(false);
		} catch (Exception e) {
			assertThatExceptionOfType(ValidationException.class);
		}
	}
	
	
	@Test
	void givenInexistentProductId_thenRefusesToCreateOrder() {
		try { 
			Long customerId = 20L;
			Long firstProductId = 1L;
			Integer firstProductQuantity = 1;		
			OrderItem firstItem = new OrderItem(firstProductId, null, firstProductQuantity);

			Long secondProductId = 3L;
			Integer secondProductQuantity = 1;		
			OrderItem secondItem = new OrderItem(secondProductId, null, secondProductQuantity);		

			Set<OrderItem> orderItems = new LinkedHashSet<OrderItem>();
			orderItems.add(firstItem);
			orderItems.add(secondItem);	


			OrderService orderService = new OrderService(orderCustomerPort, orderProductPort);

			Order order = orderService.createOrder(customerId, orderItems);
			
			assertTrue(false);
		} catch (Exception e) {
			assertThatExceptionOfType(NotFoundException.class);
		}
	}
	
	
	@Test
	void givenInvalidQuantity_thenRefusesToCreateOrder() {
		try { 
			Long customerId = 20L;
			Long firstProductId = 1L;
			Integer firstProductQuantity = 0;		
			OrderItem firstItem = new OrderItem(firstProductId, null, firstProductQuantity);

			Long secondProductId = 3L;
			Integer secondProductQuantity = 1;		
			OrderItem secondItem = new OrderItem(secondProductId, null, secondProductQuantity);		

			Set<OrderItem> orderItems = new LinkedHashSet<OrderItem>();
			orderItems.add(firstItem);
			orderItems.add(secondItem);	


			OrderService orderService = new OrderService(orderCustomerPort, orderProductPort);

			Order order = orderService.createOrder(customerId, orderItems);			
			
			assertTrue(false);
		} catch (Exception e) {
			assertThatExceptionOfType(NotFoundException.class);
		}
	}
}
