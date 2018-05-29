package presentation.dto.response;

import java.math.BigDecimal;

public class abonnementDeleteResponse {
    public int id;
    public String aanbieder;
    public String dienst;
    public String prijs;
    public String startDatum;
    public String verdubbeling;
    public Boolean deelbaar;
    public String status;

    public abonnementDeleteResponse(int id, String aanbieder, String dienst, BigDecimal prijs, String startDatum, String verdubbeling, Boolean deelbaar, String status) {
        this.id = id;
        this.aanbieder = aanbieder;
        this.dienst = dienst;
        this.prijs = "€" + prijs + " per maand";
        this.startDatum = startDatum;
        this.verdubbeling = verdubbeling;
        this.deelbaar = deelbaar;
        this.status = status;
    }
}
