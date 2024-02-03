package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{
	
	@Modifying
	@Query("UPDATE Person p SET p.enabled = false WHERE p.id = :id")
	void disbablePerson(@Param("id") Long id);
}
