package com.perisatto.fiapprj.menuguru.order.port.in;

import java.util.Set;

import com.perisatto.fiapprj.menuguru.order.domain.model.Order;
import com.perisatto.fiapprj.menuguru.order.domain.model.OrderItem;

public interface ManageOrderUseCase {
	Order createOrder(Long customerId, Set<OrderItem> orderItems) throws Exception;
}
