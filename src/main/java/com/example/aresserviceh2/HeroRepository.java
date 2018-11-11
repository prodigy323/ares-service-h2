package com.example.aresserviceh2;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface HeroRepository extends CrudRepository<Hero, Long> {

    List<Hero> findByFirstName(@Param("firstname") String firstName);
    List<Hero> findByLastName(@Param("lastname") String lastName);
    List<Hero> findByCodeName(@Param("codename") String codeName);
    List<Hero> findByTeam(@Param("team") String team);

}