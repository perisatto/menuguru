package com.perisatto.fiapprj.menuguru.infra.gateways;

import java.net.URI;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import com.perisatto.fiapprj.menuguru.application.interfaces.PaymentProcessor;
import com.perisatto.fiapprj.menuguru.domain.entities.order.OrderItem;
import com.perisatto.fiapprj.menuguru.domain.entities.payment.Payment;
import com.perisatto.fiapprj.menuguru.infra.gateways.dtos.RequestQrCodeDTO;
import com.perisatto.fiapprj.menuguru.infra.gateways.dtos.RequestQrCodeItemDTO;
import com.perisatto.fiapprj.menuguru.infra.gateways.dtos.ResponseQrCodeDTO;

public class PaymentWebApi implements PaymentProcessor {
	private final RestClient restClient;
	
	public PaymentWebApi(RestTemplateBuilder restTemplateBuilder) {
		this.restClient = RestClient.create();
	}

	@Override
	public Payment createPayment(Payment payment) {

		Set<RequestQrCodeItemDTO> items = new LinkedHashSet<RequestQrCodeItemDTO>();
		for(OrderItem orderItem : payment.getOrder().getItems()) {
			RequestQrCodeItemDTO requestItem = new RequestQrCodeItemDTO();
			requestItem.setTitle(orderItem.getProductId().toString());
			requestItem.setUnitPrice(orderItem.getActualPrice());
			requestItem.setQuantity(orderItem.getQuantity());
			requestItem.setUnityMeasure("unit");
			requestItem.setTotalAmount(orderItem.getActualPrice() * orderItem.getQuantity());
			
			items.add(requestItem);
		}		
		
		RequestQrCodeDTO request = new RequestQrCodeDTO();		
		request.setDescription("Pagamento Menuguru");
		request.setExternalReference(payment.getOrder().getId().toString());
		request.setExpirationDate("2024-07-08T16:34:56.559-04:00");
		request.setItems(items);
		request.setTitle("Pagamento Menuguru");
		request.setTotalAmount(payment.getOrder().getTotalPrice());
		
		String url = "https://api.mercadopago.com/instore/orders/qr/seller/collectors/1891840516/pos/SUC001POS001/qrs";
		ResponseEntity<ResponseQrCodeDTO> response = restClient.post()
				                               .uri(URI.create(url))
				                               .contentType(MediaType.APPLICATION_JSON)
				                               .header("Authorization", "Bearer APP_USR-8181028333104133-070721-241b964d5fe3126fabf1bdc44feaffde-1891840516")
				                               .body(request)
				                               .retrieve().toEntity(ResponseQrCodeDTO.class);
		payment.setId(response.getBody().getInStoreOrderId());
		payment.setPaymentLocation(response.getBody().getQrData());
		return payment;
	}

}
 	