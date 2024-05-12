package com.perisatto.fiapprj.menuguru.order.domain.service;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.json.UTF8DataInputJsonParser;
import com.perisatto.fiapprj.menuguru.handler.exceptions.NotFoundException;
import com.perisatto.fiapprj.menuguru.handler.exceptions.ValidationException;
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

		Order newOrder = new Order(OrderStatus.PENDENTE_PAGAMENTO, customerId, items);
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
			throw new NotFoundException("ordr-2001", "Order not found");
		}	
	}

	@Override
	public Set<Order> findAllOrders(Integer limit, Integer page) throws Exception {
		if(limit==null) {
			limit = 10;
		}

		if(page==null) {
			page = 1;
		}

		validateFindAll(limit, page);		

		Set<Order> findResult = manageOrderPort.findAll(limit, page - 1);		
		return findResult;
	}

	private void validateFindAll(Integer limit, Integer page) throws Exception {
		if (limit < 0 || limit > 50) {
			String message = "Invalid size parameter. Value must be greater than 0 and less than 50. Actual value: " + limit;
			logger.debug("\"validateFindAll\" | limit validation: " + message);
			throw new ValidationException("prdt-2002", message);			
		}

		if (page < 1) {
			String message = "Invalid page parameter. Value must be greater than 0. Actual value: " + page;
			logger.debug("\"validateFindAll\" | offset validation: " + message);
			throw new ValidationException("prdt-2003", message);	
		}
	}

	@Override
	public Order updateOrder(Long id, String status) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order checkoutOrder(Long id, String paymentIdentifier) throws Exception {
		logger.info("Checkouting order...");
		Optional<Order> order = manageOrderPort.getOrder(id);
		if(order.isPresent()) {
			
			Order checkoutOrder = order.get();
			
			if(checkoutOrder.getStatus() != OrderStatus.PENDENTE_PAGAMENTO) {
				throw new ValidationException("ordr-2005", "Order is already checkout");
			}
			
			try {
				UUID.fromString(paymentIdentifier);
			} catch (IllegalArgumentException e) {
				logger.warn("Payment identifier is not a UUID format");
				throw new ValidationException("ordr-2004", "Payment Identifier invalid");
			}
			
			
			checkoutOrder.setStatus(OrderStatus.RECEBIDO);
			checkoutOrder.setPaymentIdentifier(paymentIdentifier);
			
			Optional<Order> updatedOrder = manageOrderPort.updateOrder(checkoutOrder);
			if(updatedOrder.isPresent()) {
				logger.info("Order checkouted...");
				return updatedOrder.get();
			} else {
				throw new NotFoundException("ordr-2005", "Order not found");
			}
		} else {
			throw new NotFoundException("ordr-2005", "Order not found");
		}
	}

	@Override
	public Order cancelOrder(Long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
