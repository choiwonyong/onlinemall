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
    @Autowired OrderRepository orderRepository;

   /* @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPaymentApproved_OrderStatus(@Payload PaymentApproved paymentApproved){

        if(!paymentApproved.validate()) return;

        System.out.println("\n\n##### listener OrderStatus : " + paymentApproved.toJson() + "\n\n");

        // Sample Logic //
        Order order = new Order();
        orderRepository.save(order);
            
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPaymentCanceled_OrderStatus(@Payload PaymentCanceled paymentCanceled){

        if(!paymentCanceled.validate()) return;

        System.out.println("\n\n##### listener OrderStatus : " + paymentCanceled.toJson() + "\n\n");

        // Sample Logic //
        Order order = new Order();
        orderRepository.save(order);
            
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverDeliveryStarted_OrderStatus(@Payload DeliveryStarted deliveryStarted){

        if(!deliveryStarted.validate()) return;

        System.out.println("\n\n##### listener OrderStatus : " + deliveryStarted.toJson() + "\n\n");

        // Sample Logic //
        Order order = new Order();
        orderRepository.save(order);
            
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverDeliveryCompleted_OrderStatus(@Payload DeliveryCompleted deliveryCompleted){

        if(!deliveryCompleted.validate()) return;

        System.out.println("\n\n##### listener OrderStatus : " + deliveryCompleted.toJson() + "\n\n");

        // Sample Logic //
        Order order = new Order();
        orderRepository.save(order);
            
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverDeliveryCanceled_OrderStatus(@Payload DeliveryCanceled deliveryCanceled){

        if(!deliveryCanceled.validate()) return;

        System.out.println("\n\n##### listener OrderStatus : " + deliveryCanceled.toJson() + "\n\n");

        // Sample Logic //
        Order order = new Order();
        orderRepository.save(order);
            
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrderAccepted_OrderStatus(@Payload OrderAccepted orderAccepted){

        if(!orderAccepted.validate()) return;

        System.out.println("\n\n##### listener OrderStatus : " + orderAccepted.toJson() + "\n\n");

        // Sample Logic //
        Order order = new Order();
        orderRepository.save(order);
            
    }
*/

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}


}
