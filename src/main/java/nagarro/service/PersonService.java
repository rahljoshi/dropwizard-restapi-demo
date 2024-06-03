package nagarro.service;

import nagarro.dto.PersonDTO;

import java.util.List;

public interface PersonService {

    PersonDTO createPerson(final PersonDTO personDTO);

    List<PersonDTO> getAllPersons();

    PersonDTO getPersonById(final int id);

    void updatePerson(final int id, final PersonDTO personDTO);

    void deletePerson(final int id);
}
