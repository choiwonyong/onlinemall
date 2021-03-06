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
    @Autowired PayRepository payRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrderCanceled_PaymentCancel(@Payload OrderCanceled orderCanceled){

        if(!orderCanceled.validate()) return;

        System.out.println("\n\n##### listener PaymentCancel : " + orderCanceled.toJson() + "\n\n");

        // Sample Logic //
        Pay pay = new Pay();
        pay.setOrderId(orderCanceled.getId());
        pay.setStatus("PaymentCancel");
        pay.setProductId(orderCanceled.getProductId());
        pay.setQty(orderCanceled.getQty());
        pay.setUnitPrice(orderCanceled.getPrice());
        payRepository.save(pay);
            
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}


}
