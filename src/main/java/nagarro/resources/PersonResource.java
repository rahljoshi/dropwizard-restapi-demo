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
    public Response getAllPersons() {
        return Response.ok(personService.getAllPersons()).build();
    }

    @GET
    @Path("/{id}")
    public Response getPersonById(@PathParam("id") int id) {
        final var personDTO = personService.getPersonById(id);
        if (personDTO == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(personDTO).build();
    }

    @POST
    public Response createPerson(PersonDTO personDTO) {
        final var createdPerson = personService.insertPerson(personDTO);
        return Response.status(Response.Status.CREATED).entity(createdPerson).build();
    }

    @PUT
    @Path("/{id}")
    public Response updatePerson(@PathParam("id") int id,PersonDTO personDTO) {
        personDTO.setId(id);
        personService.updatePerson(personDTO);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletePerson(@PathParam("id") int id) {
        personService.deletePerson(id);
        return Response.noContent().build();
    }
}