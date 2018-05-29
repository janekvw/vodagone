package presentation.dto.response;

import domain.abonnement;

import java.math.BigDecimal;
import java.util.List;

public class abonnementenResponse {
    public List<abonnement> abonnementen;
    public BigDecimal totalPrice;

    public abonnementenResponse(List<abonnement> abonnementen, BigDecimal totalPrice) {
        this.abonnementen = abonnementen;
        this.totalPrice = totalPrice;
    }
}
