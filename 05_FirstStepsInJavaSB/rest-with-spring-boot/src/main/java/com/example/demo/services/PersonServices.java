package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.example.demo.model.Person;

@Service
public class PersonServices {
	
	private final AtomicLong counter =  new AtomicLong();
	private Logger logger = Logger.getLogger(PersonServices.class.getName());

	
	public List<Person> findAll() {
		logger.info("Finding all Person!");
		List<Person> persons = new ArrayList<>();
		for (int i = 0; i < 7; i++) {
			Person person = mockPerson(i);
			persons.add(person);
		}
		return persons;
	}

	public Person findById(String id) {
		logger.info("Finding one Person!");
		Person person = new Person();
		person.setId(counter.incrementAndGet());
		person.setFirstName("Antonio");
		person.setLastName("Bento");
		person.setAddress("Recife - PE");
		person.setGender("Male");
		return person;
	}
	
	public Person create(Person person) {
		logger.info("Creating one person!");
		return person;
	}

	public Person update(Person person) {
		logger.info("Updating one person!");
		return person;
	}
	
	public void delete(String id) {
		logger.info("Deleting one person");
	}
	
	private Person mockPerson(int i) {
		Person person = new Person();
		person.setId(counter.incrementAndGet());
		person.setFirstName("Person " + i);
		person.setLastName("Last name" + i);
		person.setAddress("Brazil");
		person.setGender(i % 2 == 0 ? "Male" : "Feale");
		return person;
	}

}
