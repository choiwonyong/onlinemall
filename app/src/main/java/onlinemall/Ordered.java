package onlinemall;

public class Ordered extends AbstractEvent {

    private Long id;
    private Long ProductId;
    private Integer qty;
    private String status;
    private Long unitPrice;
    private String adderss;
    private String customerId;
    private String productName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getProductId() {
        return ProductId;
    }

    public void setProductId(Long ProductId) {
        this.ProductId = ProductId;
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
    public Long getPrice() {
        return unitPrice;
    }

    public void setPrice(Long unitPrice) {
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