package presentation;

import dao.AbonnementDao;
import dao.UserDao;
import domain.abonnement;
import domain.user;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("")
public class abonneeService {
    @Inject
    private UserDao userDao;
    @Inject
    private AbonnementDao abonnementDao;

    @Path("/abonnees")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAbbosforUser(@QueryParam("token") String token) {
        List<user> users = userDao.findOtherUsers(token);

        return Response.status(200).entity(users).build();
    }

    @Path("/abonnees/{id}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response shareAbbo(@PathParam("id") int id, @QueryParam("token") String token) {
        abonnement abonnement = abonnementDao.findById(id);
        List<user> users = userDao.findOtherUsers(token);

        return Response.status(200).build();
    }
}
