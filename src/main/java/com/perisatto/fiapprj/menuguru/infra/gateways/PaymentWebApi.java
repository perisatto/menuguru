package com.perisatto.fiapprj.menuguru.infra.gateways;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import com.perisatto.fiapprj.menuguru.application.interfaces.PaymentProcessor;
import com.perisatto.fiapprj.menuguru.domain.entities.order.OrderItem;
import com.perisatto.fiapprj.menuguru.domain.entities.payment.Payment;
import com.perisatto.fiapprj.menuguru.handler.exceptions.ValidationException;
import com.perisatto.fiapprj.menuguru.infra.gateways.dtos.RequestQrCodeDTO;
import com.perisatto.fiapprj.menuguru.infra.gateways.dtos.RequestQrCodeItemDTO;
import com.perisatto.fiapprj.menuguru.infra.gateways.dtos.ResponseQrCodeDTO;


public class PaymentWebApi implements PaymentProcessor {
	
	static final Logger logger = LogManager.getLogger(PaymentWebApi.class);	
	
	private final RestClient restClient;
	private final Environment env;
	
	public PaymentWebApi(RestTemplateBuilder restTemplateBuilder, Environment env) {
		this.restClient = RestClient.create();
		this.env = env;
	}

	@Override
	public Payment createPayment(Payment payment) throws ValidationException {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT-3:00"));
		Calendar currentTimeNow = Calendar.getInstance();
		currentTimeNow.add(Calendar.MINUTE, 30);
		String expirationDate = dateFormatter.format(currentTimeNow.getTime());
		
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
		request.setExternalReference(payment.getOrder().getId().toString()+"_orderid");
		request.setExpirationDate(expirationDate);
		request.setItems(items);
		request.setNotificationUrl(env.getProperty("spring.payment.hostWebhook") + env.getProperty("server.servlet.context-path") + "/orders/" + payment.getOrder().getId().toString() + "/confirmPayment");
		request.setTitle("Pagamento Menuguru");
		request.setTotalAmount(payment.getOrder().getTotalPrice());
		
		String url = "https://api.mercadopago.com/instore/orders/qr/seller/collectors/" + env.getProperty("spring.payment.userId") + "/pos/SUC001POS001/qrs";
		ResponseEntity<ResponseQrCodeDTO> response = restClient.post()
				                               .uri(URI.create(url))
				                               .contentType(MediaType.APPLICATION_JSON)
				                               .header("Authorization", env.getProperty("spring.payment.accessToken"))
				                               .body(request)
				                               .retrieve().toEntity(ResponseQrCodeDTO.class);
		if(response.getStatusCode() != HttpStatus.CREATED) {
			logger.error("Payment URL: " + url);
			logger.error("HTTP Status Code: " + response.getStatusCode());
			throw new ValidationException("pymt-1000", "Error during payment processes. Please refer to log application for details.");
		}
		
		payment.setPaymentLocation(response.getBody().getQrData());
		
		return payment;
	}

}
 	