package com.perisatto.fiapprj.menuguru.order.domain.service;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.perisatto.fiapprj.menuguru.handler.exceptions.NotFoundException;
import com.perisatto.fiapprj.menuguru.order.domain.model.Order;
import com.perisatto.fiapprj.menuguru.order.domain.model.OrderItem;
import com.perisatto.fiapprj.menuguru.order.domain.model.OrderStatus;
import com.perisatto.fiapprj.menuguru.order.port.in.ManageOrderUseCase;
import com.perisatto.fiapprj.menuguru.order.port.out.ManageOrderPort;
import com.perisatto.fiapprj.menuguru.order.port.out.OrderCustomerPort;
import com.perisatto.fiapprj.menuguru.order.port.out.OrderProductPort;
import com.perisatto.fiapprj.menuguru.product.domain.model.Product;

public class OrderService implements ManageOrderUseCase {

	static final Logger logger = LogManager.getLogger(OrderService.class);
	
	private OrderCustomerPort orderCustomerPort;
	private OrderProductPort orderProductPort;
	private ManageOrderPort manageOrderPort;
	
	public OrderService(OrderCustomerPort orderCustomerPort, OrderProductPort orderProductPort, ManageOrderPort manageOrderPort) {
		this.orderCustomerPort = orderCustomerPort;
		this.orderProductPort = orderProductPort;
		this.manageOrderPort = manageOrderPort;
	}

	@Override
	public Order createOrder(Long customerId, Set<OrderItem> orderItems) throws Exception {
		logger.info("Creating new order...");		
		if(customerId != null) {
			logger.info("Validating customer...");
			orderCustomerPort.getCustomer(customerId);
		}

		Set<OrderItem> items = new LinkedHashSet<OrderItem>();
		Double totalPrice = 0.0;
		
		logger.info("Validating items...");
		for(OrderItem item : orderItems) {
			Product product = orderProductPort.getProduct(item.getProductId());
			totalPrice = totalPrice + (product.getPrice() * item.getQuantity());
			item.setActualPrice(product.getPrice());
			items.add(item);
		}

		Order newOrder = new Order(OrderStatus.RECEBIDO, customerId, items);
		newOrder = manageOrderPort.createOrder(newOrder);
		logger.info("New order created.");
		return newOrder;
	}

	@Override
	public Order getOrder(Long orderId) throws Exception {
		Optional<Order> order = manageOrderPort.getOrder(orderId);
		if(order.isPresent()) {
			return order.get();
		} else {
			throw new NotFoundException("ordr-2001", "Customer not found");
		}	
	}
}
