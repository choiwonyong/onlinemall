package onlinemall;

import onlinemall.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{
    @Autowired ProductRepository productRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPaymentApproved_OrderPrepare(@Payload PaymentApproved paymentApproved){

        if(!paymentApproved.validate()) return;

        System.out.println("\n\n##### listener OrderPrepare : " + paymentApproved.toJson() + "\n\n");

        // Sample Logic //

        Product product = new Product();
        
        product.setOrderId(paymentApproved.getOrderId());
        product.setProductId(paymentApproved.getProductId());
        product.setQty(paymentApproved.getQty());
        product.setStatus("OrderPrepare");
         
                  
        productRepository.save(product);
            
    }
   
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPaymentCanceled_ProductCancel(@Payload PaymentCanceled paymentCanceled){

        if(!paymentCanceled.validate()) return;

        System.out.println("\n\n##### listener ProductCancel : " + paymentCanceled.toJson() + "\n\n");

        // Sample Logic //
        Product product = new Product();
        product.setOrderId(paymentCanceled.getOrderId());
        product.setProductId(paymentCanceled.getProductId());
        product.setQty(paymentCanceled.getQty());
        product.setStatus("ProductCancel");
        productRepository.save(product);
            
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}


}
