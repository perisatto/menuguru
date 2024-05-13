package com.perisatto.fiapprj.menuguru.order.adapter.out;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.perisatto.fiapprj.menuguru.order.adapter.out.repository.OrderItemJpaEntity;
import com.perisatto.fiapprj.menuguru.order.adapter.out.repository.OrderJpaEntity;
import com.perisatto.fiapprj.menuguru.order.adapter.out.repository.OrderRepository;
import com.perisatto.fiapprj.menuguru.order.domain.model.Order;
import com.perisatto.fiapprj.menuguru.order.domain.model.OrderItem;
import com.perisatto.fiapprj.menuguru.order.domain.model.OrderStatus;
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

	@Override
	public Set<Order> findAll(Integer limit, Integer page) throws Exception {	
		Pageable pageable = PageRequest.of(page, limit, Sort.by("idOrder"));
		Page<OrderJpaEntity> orders;
		
		orders = orderRepository.findAll(pageable);
		
		Set<Order> orderSet = new LinkedHashSet<Order>();
		
			
		for (OrderJpaEntity order : orders) {
			Set<OrderItem> orderItems = new LinkedHashSet<OrderItem>();
			for(OrderItemJpaEntity orderItemsJpa : order.getItems()) {
				OrderItem orderItem = new OrderItem(orderItemsJpa.getIdProduct(),orderItemsJpa.getActualPrice(),orderItemsJpa.getQuantity());
				orderItem.setProductId(orderItemsJpa.getIdProduct());
				orderItems.add(orderItem);
			}
			
			Order retrievedOrder = new Order(OrderStatus.values()[(int) (order.getIdOrderStatus() - 1)], order.getIdCustomer(), orderItems);
			retrievedOrder.setId(order.getIdOrder());
			orderSet.add(retrievedOrder);
		}
		return orderSet;
	}

	@Override
	public Optional<Order> updateOrder(Order order) throws Exception {
		OrderMapper orderMapper = new OrderMapper();
		OrderJpaEntity orderJpaEntity = orderMapper.mapToJpaEntity(order);
		orderJpaEntity = orderRepository.save(orderJpaEntity);
		Order updatedOrder = orderMapper.mapToDomainEntity(orderJpaEntity);
		return Optional.of(updatedOrder);
	}

	@Override
	public Set<Order> listPreparationQueue(Integer limit, Integer page) throws Exception {
		Pageable pageable = PageRequest.of(page, limit, Sort.by("readyToPrepare"));
		Page<OrderJpaEntity> orders = orderRepository.findByIdOrderStatusBetween(OrderStatus.RECEBIDO.getId(), OrderStatus.EM_PREPARACAO.getId(), pageable);
		
		Set<Order> orderSet = new LinkedHashSet<Order>();
		
			
		for (OrderJpaEntity order : orders) {
			Set<OrderItem> orderItems = new LinkedHashSet<OrderItem>();
			for(OrderItemJpaEntity orderItemsJpa : order.getItems()) {
				OrderItem orderItem = new OrderItem(orderItemsJpa.getIdProduct(),orderItemsJpa.getActualPrice(),orderItemsJpa.getQuantity());
				orderItem.setProductId(orderItemsJpa.getIdProduct());
				orderItems.add(orderItem);
			}
			
			Order retrievedOrder = new Order(OrderStatus.values()[(int) (order.getIdOrderStatus() - 1)], order.getIdCustomer(), orderItems);
			retrievedOrder.setId(order.getIdOrder());
			retrievedOrder.setReadyToPrepare(order.getReadyToPrepare());
			orderSet.add(retrievedOrder);
		}
		return orderSet;
	}
}
