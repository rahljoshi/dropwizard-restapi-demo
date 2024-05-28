package nagarro.resources;

import java.util.List;
import java.util.Optional;

import io.dropwizard.hibernate.UnitOfWork;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nagarro.model.User;
import nagarro.db.UserDAO;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserDAO userDAO;

    public UserResource(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @POST
    @UnitOfWork
    public Response createUser(User user) {
        User newUser = userDAO.create(user);
        return Response.ok(newUser).build();
    }

    @GET
    @UnitOfWork
    @Path("/{id}")
    public Response getUser(@PathParam("id") Long id) {
        Optional<User> user = userDAO.findById(id);
        if (user.isPresent()) {
            return Response.ok(user.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @UnitOfWork
    public List<User> listUsers() {
        return userDAO.findAll();
    }

    @PUT
    @UnitOfWork
    @Path("/{id}")
    public Response updateUser(@PathParam("id") Long id, User user) {
        Optional<User> existingUser = userDAO.findById(id);
        if (existingUser.isPresent()) {
            User updateUser = existingUser.get();
            updateUser.setName(user.getName());
            userDAO.update(updateUser);
            return Response.ok(updateUser).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @UnitOfWork
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        Optional<User> user = userDAO.findById(id);
        if (user.isPresent()) {
            userDAO.delete(user.get());
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
