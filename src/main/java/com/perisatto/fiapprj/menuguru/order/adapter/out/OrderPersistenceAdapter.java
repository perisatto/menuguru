package com.perisatto.fiapprj.menuguru.order.adapter.out;

import org.springframework.stereotype.Component;

import com.perisatto.fiapprj.menuguru.order.adapter.out.repository.OrderJpaEntity;
import com.perisatto.fiapprj.menuguru.order.adapter.out.repository.OrderRepository;
import com.perisatto.fiapprj.menuguru.order.domain.model.Order;
import com.perisatto.fiapprj.menuguru.order.port.out.ManageOrderPort;

@Component
public class OrderPersistenceAdapter implements ManageOrderPort{
	
	private OrderRepository orderRepository;
	
	public OrderPersistenceAdapter(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@Override
	public Order createOrder(Order order) throws Exception {
		OrderMapper orderMapper = new OrderMapper();
		OrderJpaEntity orderJpaEntity = orderMapper.mapToJpaEntity(order);
		orderJpaEntity = orderRepository.save(orderJpaEntity);
		Order createdOrder = orderMapper.mapToDomainEntity(orderJpaEntity);
		return createdOrder;
	}

}
