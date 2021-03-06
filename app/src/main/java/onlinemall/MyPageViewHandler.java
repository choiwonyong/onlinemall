package onlinemall;

import onlinemall.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class MyPageViewHandler {


    @Autowired
    private MyPageRepository myPageRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenOrdered_then_CREATE_1 (@Payload Ordered ordered) {
        try {

            if (ordered.isMe()){

            // view 객체 생성
            MyPage myPage = new MyPage();
            // view 객체에 이벤트의 Value 를 set 함
            myPage.setOrderId(ordered.getId());
            myPage.setProductId(ordered.getProductId());
            myPage.setQty(ordered.getQty());
            myPage.setProductName(ordered.getProductName());
            myPage.setUnitPrice(ordered.getPrice());
            myPage.setCustomerId(ordered.getCustomerId());
            myPage.setStatus(ordered.getStatus());
            // view 레파지 토리에 save
            myPageRepository.save(myPage);
        }
        
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whenPaymentApproved_then_UPDATE_1(@Payload PaymentApproved paymentApproved) {
        try {
            if (!paymentApproved.validate()) return;
                // view 객체 조회
            Optional<MyPage> opt = myPageRepository.findByOrderId(paymentApproved.getOrderId());
            if( opt.isPresent()) {
            	MyPage myPage =opt.get();
            	myPage.setStatus(paymentApproved.getEventType());
                myPageRepository.save(myPage);
            }
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenOrderAccepted_then_UPDATE_2(@Payload OrderAccepted orderAccepted) {
        try {
            if (!orderAccepted.validate()) return;
                // view 객체 조회
            Optional<MyPage> opt = myPageRepository.findByOrderId(orderAccepted.getOrderId());
            if(opt.isPresent()) {
            	MyPage myPage =opt.get();
            	myPage.setStatus(orderAccepted.getEventType());
                myPageRepository.save(myPage);
            }
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenDeliveryStarted_then_UPDATE_3(@Payload DeliveryStarted deliveryStarted) {
        try {
            if (!deliveryStarted.validate()) return;
                // view 객체 조회
            Optional<MyPage> opt = myPageRepository.findByOrderId(deliveryStarted.getOrderId());
            if( opt.isPresent()) {
            	MyPage myPage =opt.get();
            	myPage.setStatus(deliveryStarted.getEventType());
                myPageRepository.save(myPage);
            }
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenDeliveryCompleted_then_UPDATE_4(@Payload DeliveryCompleted deliveryCompleted) {
        try {
            if (!deliveryCompleted.validate()) return;
                // view 객체 조회
            Optional<MyPage> opt = myPageRepository.findByOrderId(deliveryCompleted.getOrderId());
            if( opt.isPresent()) {
            	MyPage myPage =opt.get();
            	myPage.setStatus(deliveryCompleted.getEventType());
                myPageRepository.save(myPage);
            }
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenOrderCanceled_then_UPDATE_5(@Payload OrderCanceled orderCanceled) {
        try {
            if (!orderCanceled.validate()) return;
                // view 객체 조회
            Optional<MyPage> opt = myPageRepository.findByOrderId(orderCanceled.getId());
            if( opt.isPresent()) {
            	MyPage myPage =opt.get();
            	myPage.setStatus(orderCanceled.getEventType());
                myPageRepository.save(myPage);
            }
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}