package com.example.demo.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Service;

import com.example.demo.controllers.PersonController;
import com.example.demo.data.vo.v1.PersonVO;
import com.example.demo.exceptions.RequiredObjectIsNullException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.mapper.DozerMapper;
import com.example.demo.model.Person;
import com.example.demo.repositories.PersonRepository;

import jakarta.transaction.Transactional;

@Service
public class PersonServices {
	
	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	@Autowired
	PersonRepository repository;
	
	public List<PersonVO> findAll() {
		logger.info("Finding all PersonVO!");
		
		var persons =  DozerMapper.parseListObjects(repository.findAll(), PersonVO.class);
		persons
			.stream()
			.forEach(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
		return persons;
	}

	public PersonVO findById(Long id) {
		logger.info("Finding one PersonVO!");

		Person entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this IDs!"));
		
		var vo = DozerMapper.parseObject(entity, PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		return vo;
	}
	
	public PersonVO create(PersonVO person) {
		if(person == null) throw new RequiredObjectIsNullException(); 
		
		logger.info("Creating one person!");
		
		var entity = DozerMapper.parseObject(person, Person.class);
		var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}

	public PersonVO update(PersonVO person) {
		if(person == null) throw new RequiredObjectIsNullException(); 
		
		logger.info("Updating one person!");
		
		var entity = repository.findById(person.getKey())
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this IDs!"));
		
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
		
		var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}
	
	@Transactional
	public PersonVO disbablePerson(Long id) {
		logger.info("Disabling one PersonVO!");

		repository.disbablePerson(id);
		
		Person entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this IDs!"));
		
		var vo = DozerMapper.parseObject(entity, PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		return vo;
	}
	
	public void delete(Long id) {
		logger.info("Deleting one person" + id);
		
		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this IDs!"));
		
		repository.delete(entity);
	}

}
