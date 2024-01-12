package com.example.demo.unittests.mapper.mocks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.demo.data.vo.v1.BookVO;
import com.example.demo.model.Book;

public class MockBook {


    public Book mockEntity() {
        return mockEntity(0);
    }
    
    public BookVO mockVO() {
        return mockVO(0);
    }
    
    public List<Book> mockEntityList() {
        List<Book> persons = new ArrayList<Book>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockEntity(i));
        }
        return persons;
    }

    public List<BookVO> mockVOList() {
        List<BookVO> persons = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockVO(i));
        }
        return persons;
    }
    
    public Book mockEntity(Integer number) {
        Book person = new Book();
        person.setAuthor("Author Test" + number);
        person.setLaunchDate(new Date(10000000L));
        person.setPrice(10.00);
        person.setId(number.longValue());
        person.setTitle("Title Test" + number);
        return person;
    }

    public BookVO mockVO(Integer number) {
        BookVO person = new BookVO();
        person.setAuthor("Author Test" + number);
        person.setLaunchDate(new Date(10000000L));
        person.setPrice(10.00);
        person.setKey(number.longValue());
        person.setTitle("Title Test" + number);
        return person;
    }

}
