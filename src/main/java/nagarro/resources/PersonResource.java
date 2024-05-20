package nagarro.resources;


import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nagarro.model.Person;

import java.util.HashMap;
import java.util.Map;

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource {
    private final Map<Integer, Person> persons = new HashMap<>();

    @GET
    public Response getAllPersons() {
        return Response.ok(persons.values()).build();
    }

    @GET
    @Path("/{id}")
    public Response getPerson(@PathParam("id") int id) {
        final var person = persons.get(id);
        if (person == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(person).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPerson(Person person) {
        persons.put(person.getId(), person);
        return Response.status(Response.Status.CREATED).entity(person).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePerson(@PathParam("id") int id, Person person) {
        if (!persons.containsKey(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        persons.put(id, person);
        return Response.ok(person).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletePerson(@PathParam("id") int id) {
        Person person = persons.remove(id);
        if (person == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}