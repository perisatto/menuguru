package com.perisatto.fiapprj.menuguru.order.port.out;

import java.util.Optional;

import com.perisatto.fiapprj.menuguru.order.adapter.out.repository.OrderJpaEntity;
import com.perisatto.fiapprj.menuguru.order.domain.model.Order;

public interface ManageOrderPort {
	Order createOrder(Order order) throws Exception;
	
	Optional<Order> getOrder(Long orderId) throws Exception;
}
