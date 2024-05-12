package com.perisatto.fiapprj.menuguru.order.port.in;

import java.util.Set;

import com.perisatto.fiapprj.menuguru.order.domain.model.Order;
import com.perisatto.fiapprj.menuguru.order.domain.model.OrderItem;
import com.perisatto.fiapprj.menuguru.product.domain.model.Product;

public interface ManageOrderUseCase {
	Order createOrder(Long customerId, Set<OrderItem> orderItems) throws Exception;
	
	Order getOrder(Long orderId) throws Exception;
	
	Set<Order> findAllOrders(Integer limit, Integer page) throws Exception;
}
