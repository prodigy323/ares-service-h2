# ares-service-h2
Spring Data Rest with JPA connecting to in-memory H2 database

## ToDo
* 

----

### main dependencies and components
* JPA
* Data Rest
* H2
* Actuator
* Web
* Config client
* Eureka client
* DevTools
* Lombok
```xml
<properties>
    ...
    <spring-cloud.version>Greenwich.M1</spring-cloud.version>
</properties>
	
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-rest</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-config</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring-cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>

<repositories>
    <repository>
        <id>spring-milestones</id>
        <name>Spring Milestones</name>
        <url>https://repo.spring.io/milestone</url>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
</repositories>
```

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

### bootstrap.yml
```yaml
server:
  port: 8080

spring:
  application:
    name: ares-service-h2
  cloud:
    config:
      uri: http://localhost:8900
```

### ares-service-h2.yml
```yaml
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

### Dockerfile
```dockerfile
FROM openjdk:8-jdk-alpine
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
EXPOSE 8900
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
```

### Jenkinsfile
```groovy
def mvnTool
def prjName = "ares-service-h2"
def imageTag = "latest"

pipeline {
    agent { label 'maven' }
    options {
        buildDiscarder(logRotator(numToKeepStr: '2'))
        disableConcurrentBuilds()
    }
    stages {
        stage('Build && Test') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    script {
                        mvnTool = tool 'Maven'
                        sh "${mvnTool}/bin/mvn -B clean verify sonar:sonar -Prun-its,coverage"
                    }
                }
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    jacoco(execPattern: 'target/jacoco.exec')
                }
            }
        }
        stage('Quality Gate') {
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        stage('Release && Publish Artifact') {

        }
        stage('Create Image') {
            steps {
                sh "docker build --build-arg JAR_FILE=target/${prjName}-${releaseVersion}.jar -t ${prjName}:${releaseVersion}"
            }
        }
        stage('Publish Image') {
            steps {
                withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'JENKINS_ID', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
                    sh """
                        docker login -u ${USERNAME} -p ${PASSWORD} dockerRepoUrl
                        docker push ...
                    """
                }
            }
        }
    }
}
```
