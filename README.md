# ares-service-h2
Spring Data Rest with JPA connecting to in-memory H2 database

## ToDo
* 

----

### data.sql
> To pre-populate the in-memory database on startup
```sql
insert into hero (first_name, last_name, code_name, email, team) values	
('Tony', 'Stark', 'Ironman', 'ironman@avengers.com', 'Avengers'),
('Bruce', 'Banner', 'Hulk', 'hulk@avengers.com', 'Avengers'),
('Bruce', 'Wayne', 'Batman', 'batman@avengers.com', 'JLA'),
('Clark', 'Kent', 'Superman', 'superman@avengers.com', 'JLA'),
('Scott', 'Summers', 'Cyclops', 'cyclops@avengers.com', 'XMen'),
('James', 'Logan', 'Wolverine', 'wolverine@avengers.com', 'XMen');
```

### application.properties (or yml)
```
spring.application.name=ares-service-h2	
server.port=8080
```

### Create the Entity object
```java
@Entity	
@Data
public class Hero {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String firstName;
	private String lastName;
	private String codeName;
	private String email;
	private String team;
	
  	public Hero() {}
	
  	public Hero(String firstName, String lastName, String codeName, String email, String team) {
	  this.firstName = firstName;
	  this.lastName = lastName;
	  this.codeName = codeName;
	  this.email = email;
	  this.team = team;
	}
}
```

### Create a JPA Repository that extends CrudRepository class
```java
@RepositoryRestResource
public interface HeroRepository extends CrudRepository<Hero, Long> {

    List<Hero> findByFirstName(@Param("firstname") String firstName);
    List<Hero> findByLastName(@Param("lastname") String lastName);
    List<Hero> findByCodeName(@Param("codename") String codeName);
    List<Hero> findByTeam(@Param("team") String team);

}
```

### Connfigure the Controller methods to use the Repository for CRUD actions
```java
@RestController	
@Slf4j
@RequestMapping(path="/admin")
public class HeroController {
	@Autowired
	private HeroRepository heroRepository;

  	@RequestMapping (value = "/add", method = RequestMethod.POST)
	public ResponseEntity<Hero> createHero (@RequestBody Hero hero) {
	  log.info("POST hero");
	  heroRepository.save(hero);
	  return new ResponseEntity<>(hero, HttpStatus.OK);
	}
  
	@RequestMapping (value = "/addList", method = RequestMethod.POST)
	public ResponseEntity<List<Hero>> createHeroes (@RequestBody List<Hero> heroes) {
	  log.info("POST list of heroes");
	  heroRepository.saveAll(heroes);
	  return new ResponseEntity<>(heroes, HttpStatus.OK);
	}
  
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseEntity<Hero> updateUser(@RequestBody Hero hero) {
	  log.info("PUT hero");
	  heroRepository.save(hero);
	  return new ResponseEntity<>(hero, HttpStatus.OK);
	}
  
	@RequestMapping (value = "/updateList", method = RequestMethod.PUT)
	public ResponseEntity<List<Hero>> updateHeroes (@RequestBody List<Hero> heroes) {
	  log.info("PUT list of heroes");
	  heroRepository.saveAll(heroes);
	  return new ResponseEntity<>(heroes, HttpStatus.OK);
	}
  
	@RequestMapping (value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Optional<Hero>> deleteHero (@PathVariable("id") Long id) {
	  log.info("DELETE hero");
	  Optional<Hero> deletedHero = heroRepository.findById(id);
	  heroRepository.deleteById(id);
	  return new ResponseEntity<>(deletedHero, HttpStatus.OK);
	}
  
	@RequestMapping (value = "/deleteAll", method = RequestMethod.DELETE)
	public ResponseEntity<Iterable<Hero>> deleteAll () {
	  log.info("DELETE hero");
	  Iterable<Hero> deletedHeroes = heroRepository.findAll();
	  heroRepository.deleteAll();
	  return new ResponseEntity<>(deletedHeroes, HttpStatus.OK);
	}
}
```
