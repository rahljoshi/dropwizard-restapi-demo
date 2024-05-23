package nagarro.resources;


import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nagarro.db.PersonDAO;
import nagarro.model.Person;

import java.util.List;

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource {
    private final PersonDAO personDAO;

    public PersonResource(PersonDAO personDAO) {
        this.personDAO = personDAO;
        personDAO.createTable();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> getAllPersons() {
        return personDAO.getAllPersons();
    }

    @GET
    @Path("/{id}")
    public Person getPersonById(@PathParam("id") int id) {
        return personDAO.getPersonById(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPerson(Person person) {
        int newId = personDAO.insertPerson(person.getName(), person.getAge());
        person.setId(newId);
        return Response.status(Response.Status.CREATED).entity(person).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePerson(@PathParam("id") int id, Person person) {
        personDAO.updatePerson(id, person.getName(), person.getAge());
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletePerson(@PathParam("id") int id) {
        personDAO.deletePerson(id);
        return Response.noContent().build();
    }
}