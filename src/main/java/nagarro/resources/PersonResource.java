package nagarro.resources;


import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nagarro.dto.ErrorMessage;
import nagarro.dto.PersonDTO;
import nagarro.exception.CustomServiceException;
import nagarro.exception.DatabaseOperationException;
import nagarro.exception.PersonNotFoundException;
import nagarro.service.PersonService;

import static jakarta.ws.rs.core.Response.Status.*;
import static jakarta.ws.rs.core.Response.ok;
import static jakarta.ws.rs.core.Response.status;

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonResource {
    private final PersonService personService;

    public PersonResource(final PersonService personService) {
        this.personService = personService;
    }

    @GET
    public Response getAllPersons() {
        try {
            return ok(personService.getAllPersons()).build();
        } catch (DatabaseOperationException e) {
            return status(INTERNAL_SERVER_ERROR).entity(new ErrorMessage("An unexpected database error occurred while retrieving all persons.")).build();
        }
    }

    @POST
    public Response createPerson(final PersonDTO personDTO) {
        try {
            if (personDTO == null) {
                throw new CustomServiceException(BAD_REQUEST, "Person data for creation cannot be null");
            }
            final var createdPerson = personService.createPerson(personDTO);
            return status(CREATED).entity(createdPerson).build();
        } catch (CustomServiceException e) {
            return status(e.getStatus()).entity(new ErrorMessage(e.getMessage())).build();
        } catch (DatabaseOperationException e) {
            return status(INTERNAL_SERVER_ERROR).entity(new ErrorMessage("An unexpected database error occurred while creating the person.")).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updatePerson(@PathParam("id") final int id, final PersonDTO personDTO) {
        try {
            personService.updatePerson(id, personDTO);
            return status(OK).build();
        } catch (PersonNotFoundException e) {
            return status(NOT_FOUND).entity(new ErrorMessage(e.getMessage())).build();
        } catch (DatabaseOperationException e) {
            return status(INTERNAL_SERVER_ERROR)
                    .entity(new ErrorMessage("An unexpected database error occurred while updating the person."))
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletePerson(@PathParam("id") final int id) {
        try {
            personService.deletePerson(id);
            return Response.noContent().build();
        } catch (PersonNotFoundException e) {
            return status(NOT_FOUND).entity(new ErrorMessage(e.getMessage())).build();
        } catch (DatabaseOperationException e) {
            return status(INTERNAL_SERVER_ERROR)
                    .entity(new ErrorMessage("An unexpected database error occurred while deleting the person."))
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getPersonById(@PathParam("id") final int id) {
        try {
            final var person = personService.getPersonById(id);
            return ok(person).build();
        } catch (PersonNotFoundException e) {
            return status(NOT_FOUND).entity(new ErrorMessage(e.getMessage())).build();
        } catch (DatabaseOperationException e) {
            return status(INTERNAL_SERVER_ERROR)
                    .entity(new ErrorMessage("An unexpected database error occurred while retrieving the person."))
                    .build();
        }
    }
}