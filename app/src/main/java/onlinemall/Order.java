package onlinemall;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Date;
import java.util.Optional;

@Entity
@Table(name="Order_table")
public class Order {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long productId;
    private Integer qty;
    private String status;
    private Long unitPrice;
    private String adderss;
    private String customerId;
    private String productName;

    @PostPersist
    public void onPostPersist(){
        Ordered ordered = new Ordered();
        BeanUtils.copyProperties(this, ordered);
        ordered.publish();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        onlinemall.external.Pay pay = new onlinemall.external.Pay();
        // mappings goes here
        if(getUnitPrice()!=null){
        pay.setOrderId(getId());
        pay.setProductId(getProductId());
        pay.setQty(getQty());
        pay.setUnitPrice(getUnitPrice());
        pay.setStatus("PaymentApproved");

        AppApplication.applicationContext.getBean(onlinemall.external.PayService.class)
            .payment(pay);
        }


    }

    @PreRemove
    public void onPreRemove(){


        OrderCanceled orderCanceled = new OrderCanceled();
              
        BeanUtils.copyProperties(this, orderCanceled);
        orderCanceled.publish();
       
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Long unitPrice) {
        this.unitPrice = unitPrice;
    }
    public String getAdderss() {
        return adderss;
    }

    public void setAdderss(String adderss) {
        this.adderss = adderss;
    }
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }




}
