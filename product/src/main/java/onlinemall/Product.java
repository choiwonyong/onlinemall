package onlinemall;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.Date;

@Entity
@Table(name="Product_table")
public class Product {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Integer stock;
    private Long orderId;
    private Long productId;
    private String productName;
    private String status;
    private Integer qty;



	@PostPersist
    public void onPostPersist(){
 
		if("OrderAccepted".equals(status)) {
		 OrderAccepted orderAccepted = new OrderAccepted();
	     BeanUtils.copyProperties(this, orderAccepted);  
	     orderAccepted.publishAfterCommit();
	     
        }
        if("ProductCancel".equals(status)){
		 
		    ProductCanceled productCanceled = new ProductCanceled();
	        BeanUtils.copyProperties(this, productCanceled);  
	        productCanceled.setStatus("ProductCanceled");
	        productCanceled.publishAfterCommit();
	     }
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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



}
