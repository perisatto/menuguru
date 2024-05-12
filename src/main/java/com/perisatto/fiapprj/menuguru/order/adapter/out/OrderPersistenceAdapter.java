package com.perisatto.fiapprj.menuguru.order.adapter.out;

import java.util.Optional;

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

	@Override
	public Optional<Order> getOrder(Long orderId) throws Exception {
		Order order;
		Optional<OrderJpaEntity> orderJpaEntity = orderRepository.findById(orderId);
		
		if(orderJpaEntity.isPresent()) {
			OrderMapper orderMapper = new OrderMapper();
			order = orderMapper.mapToDomainEntity(orderJpaEntity.get());
		}else {
			return Optional.empty();
		}
		return Optional.of(order);
	}
}
