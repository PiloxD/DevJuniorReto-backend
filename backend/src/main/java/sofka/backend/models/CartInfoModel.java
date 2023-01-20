package sofka.backend.models;

import java.time.Instant;
import java.util.List;

public class CartInfoModel {

    private final Instant dateInstant = Instant.now();
    private String idType;
    private Integer documentId;
    private String clientName;
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
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    public List<ProductsSelectedModel> getClientSelection() {
        return clientSelection;
    }
    public void setClientSelection(List<ProductsSelectedModel> clientSelection) {
        this.clientSelection = clientSelection;
    }
    private List<ProductsSelectedModel> clientSelection;

    
}
