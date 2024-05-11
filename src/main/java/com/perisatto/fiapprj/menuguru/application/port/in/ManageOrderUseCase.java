package com.perisatto.fiapprj.menuguru.application.port.in;

import java.util.Set;

import com.perisatto.fiapprj.menuguru.application.domain.model.Order;

public interface ManageOrderUseCase {
	Order createOrder(String name, String type, String description, Double price, String image) throws Exception;
	
	Order getOrder(Long id) throws Exception;
	
	Order updateOrder(Long id, String name, String type, String description, Double price, String image) throws Exception;
	
	Boolean deleteOrder(Long id) throws Exception;
	
	Set<Order> findAllOrders(Integer limit, Integer page, String type) throws Exception;
}
