package com.perisatto.fiapprj.menuguru;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.perisatto.fiapprj.menuguru.customer.domain.service.CustomerService;
import com.perisatto.fiapprj.menuguru.customer.port.in.ManageCustomerUseCase;
import com.perisatto.fiapprj.menuguru.customer.port.out.ManageCustomerPort;
import com.perisatto.fiapprj.menuguru.order.domain.service.OrderService;
import com.perisatto.fiapprj.menuguru.order.port.in.ManageOrderUseCase;
import com.perisatto.fiapprj.menuguru.order.port.out.ManageOrderPort;
import com.perisatto.fiapprj.menuguru.order.port.out.OrderCustomerPort;
import com.perisatto.fiapprj.menuguru.order.port.out.OrderProductPort;
import com.perisatto.fiapprj.menuguru.product.domain.service.ProductService;
import com.perisatto.fiapprj.menuguru.product.port.in.ManageProductUseCase;
import com.perisatto.fiapprj.menuguru.product.port.out.ManageProductPort;

@Configuration
public class MenuguruConfig {
	
	@Autowired
	private Environment env;
	
	@Bean
	public DataSource dataSource() {
	    DriverManagerDataSource dataSource = new DriverManagerDataSource();

	    dataSource.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));
	    dataSource.setUsername(env.getProperty("spring.datasource.username"));
	    dataSource.setPassword(env.getProperty("spring.datasource.password"));
	    dataSource.setUrl(env.getProperty("spring.datasource.url")); 
	    
	    return dataSource;
	}
	
	@Bean
	ManageCustomerUseCase manageCustomerUseCase(ManageCustomerPort manageCustomerPort) {
		return new CustomerService(manageCustomerPort);
	}
	
	@Bean
	ManageProductUseCase manageProductUseCase(ManageProductPort manageProductPort) {
		return new ProductService(manageProductPort);
	}
	
	@Bean
	ManageOrderUseCase manageOrderUseCase(OrderCustomerPort orderCustomerPort, OrderProductPort orderProductPort, ManageOrderPort manageOrderPort) {
		return new OrderService(orderCustomerPort, orderProductPort, manageOrderPort);
	}
}
