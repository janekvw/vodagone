package presentation;

import dao.AbonnementDao;
import dao.UserDao;
import domain.abonnement;
import domain.user;
import presentation.dto.request.abonnementRequest;
import presentation.dto.request.upgradeRequest;
import presentation.dto.response.*;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;

@Path("")
public class abonnementenService {
    @Inject
    AbonnementDao abonnementDao;
    @Inject
    UserDao userDao;

    @Path("/abonnementen")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAbbosforUser(@FormParam("token") String token) {

        List<abonnement> abonnementen = abonnementDao.findAllByToken(token);

        BigDecimal totalPrice = BigDecimal.valueOf(0);

        for(abonnement abonnement : abonnementen) {
            totalPrice = totalPrice.add(abonnement.prijs);
        }

        abonnementenResponse response = new abonnementenResponse(abonnementen, totalPrice);

        return Response.status(200).entity(response).build();
    }

    @Path("/abonnementen/all")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAbbos(@FormParam("token") String token, @FormParam("filter") String filter) {

        user user = userDao.findByToken(token);

        List<abonnement> abonnementen = abonnementDao.findAll(user.id, filter);

        return Response.status(200).entity(abonnementen).build();
    }

    @Path("/abonnementen/{id}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAbbosById(@FormParam("token") String token, @PathParam("id") int id) {

        user user = userDao.findByToken(token);

        //abonnement abonnement = abonnementDao.findById(id);

        abonnement abonnement = abonnementDao.findAllByTokenAndId(token, id);

        //userabbo userabbo = abonnementDao.findStatus(id, user.id);

        abonnementResponse response = new abonnementResponse(abonnement.id, abonnement.aanbieder,
                abonnement.dienst, abonnement.prijs, abonnement.startDatum, abonnement.verdubbeling,
                abonnement.deelbaar, abonnement.status);

        return Response.status(200).entity(response).build();
    }

    @Path("/abonnementen/{id}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAbbos(@FormParam("token") String token, @PathParam("id") int id) {

        user user = userDao.findByToken(token);

        //delete
        abonnementDao.findByIdAndToken(id, user.id);

        abonnement abonnement = abonnementDao.findAllByTokenAndId(token, id);

        abonnementResponse response = new abonnementResponse(abonnement.id, abonnement.aanbieder,
                abonnement.dienst, abonnement.prijs, abonnement.startDatum, abonnement.verdubbeling,
                abonnement.deelbaar, abonnement.status);

        return Response.status(200).entity(response).build();
    }

    @Path("/abonnementen")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addAbbo(abonnementRequest request, @QueryParam("token") String token) {

        user user = userDao.findByToken(token);

        //add
        abonnementDao.addAbbo(user.id, request.id, token);

        List<abonnement> abonnementen = abonnementDao.findAllByToken(token);

        BigDecimal totalPrice = BigDecimal.valueOf(0);

        for(abonnement abonnement : abonnementen) {
            totalPrice.add(abonnement.prijs);
        }

        abonnementenResponse response = new abonnementenResponse(abonnementen, totalPrice);

        return Response.status(200).entity(response).build();
    }

    @Path("/abonnementen/{id}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addAbbo(upgradeRequest request, @PathParam("id") int id, @QueryParam("token") String token) {

        user user = userDao.findByToken(token);

        abonnementDao.upgradeVerdubbeling(request.verdubbeling, id, token);

        abonnement abonnement = abonnementDao.findAllByTokenAndId(token, id);


        abonnementResponse response = new abonnementResponse(abonnement.id, abonnement.aanbieder,
                abonnement.dienst, abonnement.prijs, abonnement.startDatum, abonnement.verdubbeling,
                abonnement.deelbaar, abonnement.status);

        return Response.status(200).entity(response).build();
    }
}
