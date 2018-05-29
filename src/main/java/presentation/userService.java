package presentation;

import dao.UserDao;
import domain.user;
import presentation.dto.request.loginRequest;
import presentation.dto.response.loginResponse;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("")
public class userService {

    @Inject
    UserDao userDao;

    @Path("/login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(loginRequest request) {
        user user = userDao.login(request.user, request.password);

        loginResponse response = new loginResponse(user.token, user.voornaam + " " + user.achternaam);

        return Response.status(200).entity(response).build();
    }
}
