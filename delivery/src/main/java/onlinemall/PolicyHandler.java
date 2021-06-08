package onlinemall;

import onlinemall.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{
    @Autowired DeliveryRepository deliveryRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrderAccepted_DeliveryPrepare(@Payload OrderAccepted orderAccepted){

        if(!orderAccepted.validate()) return;

        System.out.println("\n\n##### listener DeliveryPrepare : " + orderAccepted.toJson() + "\n\n");

        // Sample Logic //
        Delivery delivery = new Delivery();
        delivery.setOrderId(orderAccepted.getOrderId());
        delivery.setStatus("DeliveryPrepare");
        deliveryRepository.save(delivery);
            
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverProductCanceled_DeliveryCancel(@Payload ProductCanceled productCanceled){

        if(!productCanceled.validate()) return;

        System.out.println("\n\n##### listener DeliveryCancel : " + productCanceled.toJson() + "\n\n");

        // Sample Logic //
        Delivery delivery = new Delivery();
        delivery.setOrderId(productCanceled.getOrderId());
        delivery.setStatus("DeliveryCancel");
        deliveryRepository.save(delivery);
            
    }
    
    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}


}
