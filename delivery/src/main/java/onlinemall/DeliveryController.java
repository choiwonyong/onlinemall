package onlinemall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

 @RestController
 public class DeliveryController {
	 
	 @Autowired
	 DeliveryRepository deliveryRepository;
	 
	 @RequestMapping(value = "/deliveries/started",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	 
	 public boolean started(
			 
			 @RequestParam("orderId") Long orderId,
			 @RequestParam("status") String status, 
			 @RequestParam("customerId") String customerId,
			 @RequestParam("address") String address ) {

		 Delivery delivery =new Delivery();
		 delivery.setAddress(address);
	     delivery.setCustomerId(customerId);
	     delivery.setOrderId(orderId);
	     delivery.setStatus(status);
	     deliveryRepository.save(delivery);
	     
	     delivery.kafkaSend();
		 return true;
		 
	 }
	
	 @RequestMapping(value = "/deliveries/completed",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	 
	 public boolean completed(
			 
			 @RequestParam("orderId") Long orderId,
			 @RequestParam("status") String status, 
			 @RequestParam("customerId") String customerId,
			 @RequestParam("address") String address ) {

		 Delivery delivery =new Delivery();
		 delivery.setAddress(address);
	     delivery.setCustomerId(customerId);
	     delivery.setOrderId(orderId);
	     delivery.setStatus(status);
	     deliveryRepository.save(delivery);
	     
	     delivery.kafkaSend();
		 return true;
		 
	 }
 }
