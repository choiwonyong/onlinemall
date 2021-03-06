package onlinemall;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.Date;

import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Entity
@Table(name="Pay_table")
public class Pay {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long orderId;
    private Long unitPrice;
    private String status;
    private Integer qty;
    private Long productId;

    @PostPersist
    public void onPostPersist(){
        
           if("PaymentApproved".equals(status)) {   
           PaymentApproved paymentApproved = new PaymentApproved();          
           BeanUtils.copyProperties(this, paymentApproved);
           paymentApproved.publish();
           }else {
           PaymentCanceled paymentCanceled = new PaymentCanceled();
           BeanUtils.copyProperties(this, paymentCanceled);
           paymentCanceled.setUnitPrice(getUnitPrice());
           paymentCanceled.publish();
           }
               	   
                        
           try {
                Thread.currentThread().sleep((long) (400 + Math.random() * 220));
           } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    @PreRemove
    public void onPreRemove(){
        PaymentCanceled paymentCanceled = new PaymentCanceled();
        BeanUtils.copyProperties(this, paymentCanceled);
        paymentCanceled.publishAfterCommit();
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public Long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Long unitPrice) {
        this.unitPrice = unitPrice;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }




}
