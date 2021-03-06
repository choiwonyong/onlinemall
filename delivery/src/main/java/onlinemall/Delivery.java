package onlinemall;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.mapping.Document;

import onlinemall.*;

import java.util.List;
import java.util.Date;

//@Entity
//@Table(name="Delivery_table")
@Document(collection = "Delivery_table")
public class Delivery {

    //@Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private String id;
    private Long orderId;
    private String status;
    private String customerId;
    private String address;


    @PostPersist
    public void onPostPersist(){
    	
        /*if("DeliveryStarted".equals(status)) {
    	    DeliveryStarted deliveryStarted = new DeliveryStarted();
            deliveryStarted.setOrderId(getOrderId());
            BeanUtils.copyProperties(this, deliveryStarted);
            //deliveryStarted.setStatus("DeliveryStarted");
            deliveryStarted.publishAfterCommit();
    
        }if("DeliveryCancel".equals(status)) {
    	DeliveryCanceled deliveryCanceled = new DeliveryCanceled();
        BeanUtils.copyProperties(this, deliveryCanceled);
        deliveryCanceled.setStatus("DeliveryCanceled");
        deliveryCanceled.publish();
        }else{

        }*/

    }
    
    public void kafkaSend(){

        if("DeliveryStarted".equals(status)) {
    	    DeliveryStarted deliveryStarted = new DeliveryStarted();
            BeanUtils.copyProperties(this, deliveryStarted);
            deliveryStarted.publish();    
        }if("DeliveryCancel".equals(status)) {
        	DeliveryCanceled deliveryCanceled = new DeliveryCanceled();
        	BeanUtils.copyProperties(this, deliveryCanceled);
        	deliveryCanceled.setStatus("DeliveryCanceled");
        	deliveryCanceled.publish();
        }if("DeliveryCompleted".equals(status)) {
        	DeliveryCompleted deliveryCompleted = new DeliveryCompleted();
            BeanUtils.copyProperties(this, deliveryCompleted);
            deliveryCompleted.publish();

        }

    }
   


    @PostUpdate
    public void onPostUpdate(){
        
        DeliveryCompleted deliveryCompleted = new DeliveryCompleted();
        BeanUtils.copyProperties(this, deliveryCompleted);
        deliveryCompleted.publishAfterCommit();


        DeliveryCanceled deliveryCanceled = new DeliveryCanceled();
        BeanUtils.copyProperties(this, deliveryCanceled);
        deliveryCanceled.publishAfterCommit();


    }


    public String  getId() {
       return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }




}
