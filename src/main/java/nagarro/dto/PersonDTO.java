package nagarro.dto;

import nagarro.entity.Person;

public class PersonDTO {

    private int id;
    private String name;
    private int age;

    public PersonDTO(int id, String name, int age){
        this.age = age;
        this.name = name;
        this.id = id;
    }
    public PersonDTO(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
