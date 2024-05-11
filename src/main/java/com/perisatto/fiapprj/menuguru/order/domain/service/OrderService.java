package com.perisatto.fiapprj.menuguru.order.domain.service;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.perisatto.fiapprj.menuguru.order.domain.model.Order;
import com.perisatto.fiapprj.menuguru.order.domain.model.OrderItem;
import com.perisatto.fiapprj.menuguru.order.domain.model.OrderStatus;
import com.perisatto.fiapprj.menuguru.order.port.in.ManageOrderUseCase;
import com.perisatto.fiapprj.menuguru.order.port.out.OrderCustomerPort;
import com.perisatto.fiapprj.menuguru.order.port.out.OrderProductPort;
import com.perisatto.fiapprj.menuguru.product.domain.model.Product;

public class OrderService implements ManageOrderUseCase {

	static final Logger logger = LogManager.getLogger(OrderService.class);
	
	private OrderCustomerPort orderCustomerPort;
	private OrderProductPort orderProductPort;	
	
	public OrderService(OrderCustomerPort productCustomerPort, OrderProductPort orderProductPort) {
		this.orderCustomerPort = productCustomerPort;
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
			totalPrice = totalPrice + product.getPrice();
			item.setActualPrice(product.getPrice());
			items.add(item);
		}

		Order order = new Order(OrderStatus.RECEBIDO, customerId, items);

		
		return order;
	}
}
