package com.perisatto.fiapprj.menuguru.order.adapter.out;

import java.util.LinkedHashSet;
import java.util.Set;

import com.perisatto.fiapprj.menuguru.order.adapter.out.repository.OrderItemJpaEntity;
import com.perisatto.fiapprj.menuguru.order.adapter.out.repository.OrderJpaEntity;
import com.perisatto.fiapprj.menuguru.order.domain.model.Order;
import com.perisatto.fiapprj.menuguru.order.domain.model.OrderItem;
import com.perisatto.fiapprj.menuguru.order.domain.model.OrderStatus;

public class OrderMapper {
	
	Order mapToDomainEntity(OrderJpaEntity orderJpaEntity) throws Exception {
		Set<OrderItem> items = new LinkedHashSet<OrderItem>();
		for(OrderItemJpaEntity itemEntity : orderJpaEntity.getItems()) {
			OrderItem item = new OrderItem(itemEntity.getIdProduct(), itemEntity.getActualPrice(), itemEntity.getQuantity());
			item.setOrderItemId(itemEntity.getIdOrderItem());
			items.add(item);
		}
		Order order = new Order(OrderStatus.values()[(int) (orderJpaEntity.getIdOrderStatus() - 1)], orderJpaEntity.getIdCustomer(), items);		
		order.setId(orderJpaEntity.getIdOrder());
		order.setPaymentIdentifier(orderJpaEntity.getPaymentIdentifier());
		order.setReadyToPrepare(orderJpaEntity.getReadyToPrepare());
		return order;
	}
	
	OrderJpaEntity mapToJpaEntity(Order order) {		
		Set<OrderItemJpaEntity> jpaItems = new LinkedHashSet<OrderItemJpaEntity>();
		for(OrderItem item : order.getItems()) {
			OrderItemJpaEntity itemEntity = new OrderItemJpaEntity();
			itemEntity.setIdOrderItem(item.getOrderItemId());
			itemEntity.setIdProduct(item.getProductId());
			itemEntity.setActualPrice(item.getActualPrice());
			itemEntity.setQuantity(item.getQuantity());
			jpaItems.add(itemEntity);
		}
		
		OrderJpaEntity orderJpaEntity = new OrderJpaEntity();
		orderJpaEntity.setIdOrder(order.getId());
		orderJpaEntity.setIdCustomer(order.getCustomerId());
		orderJpaEntity.setIdOrderStatus(order.getStatus().getId());
		orderJpaEntity.setTotalPrice(order.getTotalPrice());
		orderJpaEntity.setItems(jpaItems);
		orderJpaEntity.setPaymentIdentifier(order.getPaymentIdentifier());
		orderJpaEntity.setReadyToPrepare(order.getReadyToPrepare());
		return orderJpaEntity;
		
	}
}
