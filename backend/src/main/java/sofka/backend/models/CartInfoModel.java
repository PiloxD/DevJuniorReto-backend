package sofka.backend.models;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;

public class CartInfoModel {
    @Id
    private String cartId;
    private Instant dateInstant;
    private String idType;
    private Integer documentId;
    private String clientName;
    private List<ProductsSelectedModel> clientSelection;
    
    public void setDateInstant(Instant dateInstant) {
        this.dateInstant = Instant.now();
    }

    public Instant getDateInstant() {
        return dateInstant;
    }
    public String getIdType() {
        return idType;
    }
    public void setIdType(String idType) {
        this.idType = idType;
    }
    public Integer getDocumentId() {
        return documentId;
    }
    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }
    public String getClientName() {
        return clientName;
    }
    public String getCartId() {
        return cartId;
    }
    public void setCartId(String cartId) {
        this.cartId = cartId;
    }
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    public List<ProductsSelectedModel> getClientSelection() {
        return clientSelection;
    }
    public void setClientSelection(List<ProductsSelectedModel> clientSelection) {
        this.clientSelection = clientSelection;
    }

    
}
