package com.perisatto.fiapprj.menuguru.order.port.in;

import java.util.Set;

import com.perisatto.fiapprj.menuguru.order.domain.model.Order;
import com.perisatto.fiapprj.menuguru.order.domain.model.OrderItem;

public interface ManageOrderUseCase {
	Order createOrder(Long customerId, Set<OrderItem> orderItems) throws Exception;
	
	Order getOrder(Long orderId) throws Exception;
	
	Set<Order> findAllOrders(Integer limit, Integer page) throws Exception;
	
	Order updateOrder(Long id, String status) throws Exception;
	
	Order checkoutOrder(Long id, String paymentIdentifier) throws Exception;
	
	Order cancelOrder(Long id) throws Exception;
	
	Set<Order> listPreparationQueue(Integer limit, Integer page) throws Exception;
}
