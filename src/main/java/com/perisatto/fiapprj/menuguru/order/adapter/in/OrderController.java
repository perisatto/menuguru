package com.perisatto.fiapprj.menuguru.order.adapter.in;

import java.net.URI;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perisatto.fiapprj.menuguru.order.adapter.in.dto.CreateOrderRequestDTO;
import com.perisatto.fiapprj.menuguru.order.adapter.in.dto.CreateOrderResponseDTO;
import com.perisatto.fiapprj.menuguru.order.adapter.in.dto.OrderItemDTO;
import com.perisatto.fiapprj.menuguru.order.domain.model.Order;
import com.perisatto.fiapprj.menuguru.order.domain.model.OrderItem;
import com.perisatto.fiapprj.menuguru.order.port.in.ManageOrderUseCase;

@RestController
@RequestMapping("/menuguru/v1")
public class OrderController {

	@Autowired
	private ManageOrderUseCase manageOrderUseCase;
	
	@Autowired
	private Properties requestProperties;
	
	@PostMapping(value = "/orders", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CreateOrderResponseDTO> createOrder(@RequestBody CreateOrderRequestDTO createRequest) throws Exception {
		requestProperties.setProperty("resourcePath", "/orders");
		ModelMapper orderMapper = new ModelMapper();
		Set<OrderItem> orderItems = new LinkedHashSet<OrderItem>();
		for(OrderItemDTO requestItem : createRequest.getItems()) {
			OrderItem item = new OrderItem(requestItem.getId(), null, requestItem.getQuantity());
			orderItems.add(item);
		}		
		Order order = manageOrderUseCase.createOrder(createRequest.getCustomerId(), orderItems);
		CreateOrderResponseDTO response = orderMapper.map(order, CreateOrderResponseDTO.class);
		URI location = new URI("/orders/" + response.getId());
		return ResponseEntity.status(HttpStatus.CREATED).location(location).body(response);
	}
}
