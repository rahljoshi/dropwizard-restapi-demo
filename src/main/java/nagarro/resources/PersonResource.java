package nagarro.resources;


import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nagarro.dto.PersonDTO;
import nagarro.service.PersonService;

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonResource {
    private final PersonService personService;

    public PersonResource(PersonService personService) {
        this.personService = personService;
    }

    @GET
    public final Response getAllPersons() {
        return Response.ok(personService.getAllPersons()).build();
    }

    @GET
    @Path("/{id}")
    public final Response getPersonById(@PathParam("id") final int id) {
        var person = personService.getPersonById(id);
        if (person == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Person not found").build();
        }
        return Response.ok(person).build();
    }

    @POST
    public final Response createPerson(final PersonDTO personDTO) {
        final var createdPerson = personService.createPerson(personDTO);
        return Response.status(Response.Status.CREATED).entity(createdPerson).build();
    }

    @PUT
    @Path("/{id}")
    public final Response updatePerson(@PathParam("id") final int id, final PersonDTO personDTO) {
        personService.updatePerson(id, personDTO);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public final Response deletePerson(@PathParam("id") final int id) {
        personService.deletePerson(id);
        return Response.noContent().build();
    }
}