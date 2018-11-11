package com.example.aresserviceh2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AresServiceH2Application {

    public static void main(String[] args) {
        SpringApplication.run(AresServiceH2Application.class, args);
    }

/*	@Bean
    CommandLineRunner initData(HeroRepository heroRepository) {
	    return args -> {
	        heroRepository.save(new Hero("Tony", "Stark", "Ironman", "ironman@avengers.com", "Avengers"));
            heroRepository.save(new Hero("Bruce", "Banner", "Hulk", "hulk@avengers.com", "Avengers"));
            heroRepository.save(new Hero("Bruce", "Wayne", "Batman", "batman@jla.com", "Justice League"));
            heroRepository.save(new Hero("Clark", "Kent", "Superman", "superman@jla.com", "Justice League"));
            heroRepository.save(new Hero("James", "Logan", "Wolverine", "wolverine@avengers.com", "XMen"));
        };
    }*/

}
/*@Data
@Entity
class Employee {
    @Id @GeneratedValue Long id;
    String firstName;
    String lastName;
    String role;

    @ManyToOne
    Manager manager;

    private Employee() {}

    public Employee(String firstName, String lastName, String role, Manager manager) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.manager = manager;
    }
}*/

/*@RepositoryRestResource
interface EmployeeRepository extends CrudRepository<Employee, Long> {

    List<Employee> findByLastName(@Param("lastname") String lastName);
    List<Employee> findByRole(@Param("role") String role);

}*/

/*@Data
@Entity
class Manager {
    @Id @GeneratedValue Long id;
    String name;

    @OneToMany(mappedBy = "manager")
    List<Employee> employees;

    private Manager() {}

    public Manager(String name) {
        this.name = name;
    }
}

@RepositoryRestResource
interface ManagerRepository extends CrudRepository<Manager, Long> {

    List<Manager> findByEmployeesRoleContains(@Param("q") String role);

}*/