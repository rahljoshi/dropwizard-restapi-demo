package nagarro.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Person {
    private int id;
    private String name;
    private int age;
}