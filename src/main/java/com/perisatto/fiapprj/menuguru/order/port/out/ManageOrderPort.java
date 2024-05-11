package com.perisatto.fiapprj.menuguru.order.port.out;

import com.perisatto.fiapprj.menuguru.order.domain.model.Order;

public interface ManageOrderPort {
	Order createOrder(Order order) throws Exception;
}
