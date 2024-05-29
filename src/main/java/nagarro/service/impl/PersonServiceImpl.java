package nagarro.service.impl;

import nagarro.dao.PersonDAO;
import nagarro.dto.PersonDTO;
import nagarro.entity.Person;
import nagarro.service.PersonService;
import org.jvnet.hk2.annotations.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements PersonService {
    private final PersonDAO personDAO;

    public PersonServiceImpl(PersonDAO personDAO){
        this.personDAO = personDAO;
    }

    @Override
    public final PersonDTO insertPerson(PersonDTO personDTO){
        var person = new Person(0, personDTO.getName(), personDTO.getAge());
        int generatedId = personDAO.insertPerson(person);
        person.setId(generatedId);
        return convertPersonToPersonDTO(person);
    }

    @Override
    public final List<PersonDTO> getAllPersons(){
        return personDAO.getAllPersons().stream()
                .map(person -> new PersonDTO(person.getId(), person.getName(), person.getAge()))
                .collect(Collectors.toList());
    }

    @Override
    public final PersonDTO getPersonById(int id){
        final Optional<Person> person = Optional.ofNullable(personDAO.getPersonById(id));
        if(person.isPresent()){
            return convertPersonToPersonDTO(person.get());
        }else{
            return null;
        }
    }

    @Override
    public final void updatePerson(PersonDTO personDTO){
        final var person = new Person(personDTO.getId(), personDTO.getName(), personDTO.getAge());
        System.out.println(person.getAge()+" "+person.getName()+" "+person.getId());
        personDAO.updatePerson(person);
    }

    @Override
    public final void deletePerson(int id){
        personDAO.deletePerson(id);
    }

    public final PersonDTO convertPersonToPersonDTO(Person person){
        final var personDTO = new PersonDTO(person.getId(), person.getName(), person.getAge());
        return personDTO;
    }
}
