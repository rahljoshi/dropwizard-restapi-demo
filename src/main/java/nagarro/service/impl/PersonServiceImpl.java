package nagarro.service.impl;

import nagarro.dao.PersonDAO;
import nagarro.dto.PersonDTO;
import nagarro.entity.Person;
import nagarro.exception.DatabaseOperationException;
import nagarro.exception.PersonNotFoundException;
import nagarro.service.PersonService;
import org.jvnet.hk2.annotations.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class PersonServiceImpl implements PersonService {
    private static final String PERSON_NOT_FOUND_MESSAGE = "Person with id %d not found";
    private final PersonDAO personDAO;

    public PersonServiceImpl(final PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    public PersonDTO createPerson(final PersonDTO personDTO) {
        try {
            personDTO.validateForCreation();
            final var person = new Person(0, personDTO.getName(), personDTO.getAge());
            final var generatedId = personDAO.createPerson(person);
            person.setId(generatedId);
            return convertPersonToPersonDTO(person);
        } catch (DatabaseOperationException e) {
            throw e;
        }
    }

    @Override
    public List<PersonDTO> getAllPersons() {
        try {
            return personDAO.getAllPersons().stream().map(this::convertPersonToPersonDTO).collect(toList());
        } catch (DatabaseOperationException e) {
            throw e;
        }
    }

    @Override
    public PersonDTO getPersonById(final int id) throws PersonNotFoundException {
        final var person = personDAO.getPersonById(id);
        return person.map(this::convertPersonToPersonDTO).orElseThrow(() -> new PersonNotFoundException(String.format(PERSON_NOT_FOUND_MESSAGE, id)));
    }

    @Override
    public void updatePerson(final int id, final PersonDTO personDTO) throws PersonNotFoundException {
        try {
            personDTO.validateForUpdate();
            final var existingPerson = personDAO.getPersonById(id);
            if (existingPerson.isPresent()) {
                final var personToUpdate = existingPerson.get();
                personToUpdate.setName(personDTO.getName());
                personToUpdate.setAge(personDTO.getAge());
                personDAO.updatePerson(personToUpdate);
            } else {
                throw new PersonNotFoundException(String.format(PERSON_NOT_FOUND_MESSAGE, id));
            }
        } catch (DatabaseOperationException e) {
            throw e;
        }
    }

    @Override
    public void deletePerson(final int id) throws PersonNotFoundException {
        try {
            final var personToDelete = personDAO.getPersonById(id);
            if (personToDelete.isPresent()) {
                personDAO.deletePerson(id);
            } else {
                throw new PersonNotFoundException(String.format(PERSON_NOT_FOUND_MESSAGE, id));
            }
        } catch (DatabaseOperationException e) {
            throw e;
        }
    }

    private PersonDTO convertPersonToPersonDTO(final Person person) {
        return new PersonDTO(person.getId(), person.getName(), person.getAge());
    }
}