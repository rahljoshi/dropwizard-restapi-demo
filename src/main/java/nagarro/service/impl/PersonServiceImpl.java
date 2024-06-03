package nagarro.service.impl;

import nagarro.dao.PersonDAO;
import nagarro.dto.PersonDTO;
import nagarro.entity.Person;
import nagarro.exception.PersonNotFoundException;
import nagarro.service.PersonService;
import org.jvnet.hk2.annotations.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements PersonService {
    private final PersonDAO personDAO;

    public PersonServiceImpl(final PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    public final PersonDTO createPerson(final PersonDTO personDTO) {
        validatePersonDTO(personDTO);
        final var person = new Person(0, personDTO.getName(), personDTO.getAge());
        final int generatedId = personDAO.createPerson(person);
        person.setId(generatedId);
        return convertPersonToPersonDTO(person);
    }

    @Override
    public final List<PersonDTO> getAllPersons() {
        return personDAO.getAllPersons().stream().map(person -> new PersonDTO(person.getId(), person.getName(), person.getAge())).collect(Collectors.toList());
    }

    @Override
    public final PersonDTO getPersonById(final int id) throws PersonNotFoundException {
        Optional<Person> person = personDAO.getPersonById(id);
        if (person.isPresent()) {
            return convertPersonToPersonDTO(person.get());
        } else {
            throw new PersonNotFoundException("Person with id " + id + " not found");
        }
    }

    @Override
    public final void updatePerson(final int id, final PersonDTO personDTO) {
        validatePersonDTO(personDTO);
        if (personDAO.getPersonById(id).isEmpty()) {
            throw new PersonNotFoundException("Person with id " + id + " not found");
        }
        final var person = new Person(id, personDTO.getName(), personDTO.getAge());
        personDAO.updatePerson(person);
    }

    @Override
    public final void deletePerson(final int id) throws PersonNotFoundException {
        Optional<Person> personToDelete = personDAO.getPersonById(id);
        if (personToDelete.isPresent()) {
            personDAO.deletePerson(id);
        } else {
            throw new PersonNotFoundException("Person with id " + id + " not found");
        }
    }

    private final PersonDTO convertPersonToPersonDTO(final Person person) {
        return new PersonDTO(person.getId(), person.getName(), person.getAge());
    }

    private void validatePersonDTO(final PersonDTO personDTO) {
        if (personDTO.getName() == null || personDTO.getName().isEmpty() || personDTO.getAge() < 0) {
            throw new IllegalArgumentException("Invalid person data");
        }
    }
}
