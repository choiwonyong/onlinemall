
package onlinemall;

public class PaymentCanceled extends AbstractEvent {

    private Long id;
    private Long orderId;
    private Long unitPrice;
    private String status;
    private Integer qty;
    private Long productId;

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

