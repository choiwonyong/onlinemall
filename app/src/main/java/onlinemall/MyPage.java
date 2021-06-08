package onlinemall;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="MyPage_table")
public class MyPage {

        @Id
        @GeneratedValue(strategy=GenerationType.AUTO)
        private Long id;
        private Integer qty;
        private Long productId;
        private String productName;
        private Long unitPrice;
        private String customerId;
        private String status;
        private Long orderId;


        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
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
        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }
        public Long getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(Long unitPrice) {
            this.unitPrice = unitPrice;
        }
        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }
        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
        public Long getOrderId() {
            return orderId;
        }

        public void setOrderId(Long orderId) {
            this.orderId = orderId;
        }

}
