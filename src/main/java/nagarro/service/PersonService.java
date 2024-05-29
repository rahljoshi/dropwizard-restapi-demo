package nagarro.service;

import nagarro.dto.PersonDTO;

import java.util.List;

public interface PersonService {

    PersonDTO insertPerson(PersonDTO personDTO);

    List<PersonDTO> getAllPersons();

    PersonDTO getPersonById(int id);

    void updatePerson(PersonDTO personDTO);

    void deletePerson(int id);
}
