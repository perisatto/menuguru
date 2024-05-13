package com.perisatto.fiapprj.menuguru.application.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.perisatto.fiapprj.menuguru.customer.domain.model.CPF;
import com.perisatto.fiapprj.menuguru.customer.domain.model.Customer;
import com.perisatto.fiapprj.menuguru.handler.exceptions.NotFoundException;
import com.perisatto.fiapprj.menuguru.handler.exceptions.ValidationException;
import com.perisatto.fiapprj.menuguru.order.domain.model.Order;
import com.perisatto.fiapprj.menuguru.order.domain.model.OrderItem;
import com.perisatto.fiapprj.menuguru.order.domain.model.OrderStatus;
import com.perisatto.fiapprj.menuguru.order.domain.service.OrderService;
import com.perisatto.fiapprj.menuguru.order.port.out.ManageOrderPort;
import com.perisatto.fiapprj.menuguru.order.port.out.OrderCustomerPort;
import com.perisatto.fiapprj.menuguru.order.port.out.OrderProductPort;
import com.perisatto.fiapprj.menuguru.product.domain.model.Product;
import com.perisatto.fiapprj.menuguru.product.domain.model.ProductType;

public class OrderServiceTest {

	private OrderCustomerPort orderCustomerPort = new OrderCustomerPort() {

		@Override
		public Customer getCustomer(Long id) throws Exception {
			if(id==10L) {
				String customerName = "Roberto Machado";
				String customerEmail = "roberto.machado@bestmail.com";
				String documentNumber = "90779778057";

				CPF customerCPF = new CPF(documentNumber);
				Customer customer = new Customer(10L, customerCPF, customerName, customerEmail);
				return customer;
			} else {
				throw new NotFoundException("tst-0000", "Customer not found");
			}
		}

	};
	private OrderProductPort orderProductPort = new OrderProductPort() {

		@Override
		public Product getProduct(Long id) throws Exception {
			String productName = "X-Bacon";
			ProductType productType = ProductType.LANCHE;
			String productDescription = "O x-bacon é um sanduíche irresistível que une o sabor intenso do bacon crocante com queijo derretido"
					+ ", alface, tomate e um suculento hambúrguer, tudo envolto em um pão macio e tostado. "
					+ "Uma explosão de sabores em cada mordida!";
			Double productPrice = 35.50;
			String productImage = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNk+A8AAQUBAScY42YAAAAASUVORK5CYII=";			
			Product product = new Product(productName, productType, productDescription, productPrice, productImage);
			product.setId(1L);
			return product;
		}

	};
	private ManageOrderPort manageOrderPort = new ManageOrderPort() {

		@Override
		public Order createOrder(Order order) throws Exception {
			Order newOrder = order;
			newOrder.setId(1L);
			return newOrder;
		}

		@Override
		public Optional<Order> getOrder(Long orderId) throws Exception {
			if(orderId==1L) {
				OrderStatus orderStatus = OrderStatus.RECEBIDO;
				Long customerId = 1L;

				Long firstProductId = 1L;
				Double firstProductActualPrice = 35.50;
				Integer firstProductQuantity = 1;		
				OrderItem firstItem = new OrderItem(firstProductId, firstProductActualPrice, firstProductQuantity);

				Long secondProductId = 1L;
				Double secondProductActualPrice = 35.50;
				Integer secondProductQuantity = 1;		
				OrderItem secondItem = new OrderItem(secondProductId, secondProductActualPrice, secondProductQuantity);		

				Set<OrderItem> orderItems = new LinkedHashSet<OrderItem>();
				orderItems.add(firstItem);
				orderItems.add(secondItem);

				Order order = new Order(orderStatus, customerId, orderItems);
				order.setId(orderId);
				return Optional.of(order);
			}else {
				return Optional.empty();
			}
		}

		@Override
		public Set<Order> findAll(Integer limit, Integer page) throws Exception {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Optional<Order> updateOrder(Order order) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Set<Order> listPreparationQueue(Integer limit, Integer page) throws Exception {
			// TODO Auto-generated method stub
			return null;
		}		
	};

	@Test
	void givenValidParameters_thenCreateOrder() throws Exception {
		Long customerId = 10L;
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


		OrderService orderService = new OrderService(orderCustomerPort, orderProductPort, manageOrderPort);

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


		OrderService orderService = new OrderService(orderCustomerPort, orderProductPort, manageOrderPort);

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


			OrderService orderService = new OrderService(orderCustomerPort, orderProductPort, manageOrderPort);

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


			OrderService orderService = new OrderService(orderCustomerPort, orderProductPort, manageOrderPort);

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


			OrderService orderService = new OrderService(orderCustomerPort, orderProductPort, manageOrderPort);

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


			OrderService orderService = new OrderService(orderCustomerPort, orderProductPort, manageOrderPort);

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


			OrderService orderService = new OrderService(orderCustomerPort, orderProductPort, manageOrderPort);

			Order order = orderService.createOrder(customerId, orderItems);			

			assertTrue(false);
		} catch (Exception e) {
			assertThatExceptionOfType(NotFoundException.class);
		}
	}

	@Test
	void givenValidOrderId_thenRetrieveOrder() throws Exception {
		Long orderId = 1L;

		OrderService orderService = new OrderService(orderCustomerPort, orderProductPort, manageOrderPort);

		Order order = orderService.getOrder(orderId);

		assertThat(order.getId()).isEqualTo(orderId);
		assertThat(order.getItems()).isNotEmpty();
		assertThat(order.getTotalPrice()).isGreaterThan(0.0);		
	}

	@Test
	void givenInexistentOrderId_thenRefusesToRetrieveOrder() {
		try { 
			Long orderId = 2L;

			OrderService orderService = new OrderService(orderCustomerPort, orderProductPort, manageOrderPort);

			Order order = orderService.getOrder(orderId);

			assertTrue(false);
		}catch (Exception e) {
			assertThatExceptionOfType(NotFoundException.class);
		}

	}
}
