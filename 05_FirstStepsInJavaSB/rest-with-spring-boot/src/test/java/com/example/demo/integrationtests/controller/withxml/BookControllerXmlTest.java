package com.example.demo.integrationtests.controller.withxml;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.configs.TestConfigs;
import com.example.demo.integrationtests.testcontainers.AbstractIntegrationTest;
import com.example.demo.integrationtests.vo.AccountCredentialsVO;
import com.example.demo.integrationtests.vo.BookVO;
import com.example.demo.integrationtests.vo.TokenVO;
import com.example.demo.integrationtests.vo.pagedmodels.PagedModelBook;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class BookControllerXmlTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static XmlMapper objectMapper;

	private static BookVO book;

	@BeforeAll
	public static void setup() {
		objectMapper = new XmlMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		book = new BookVO();
	}

	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

		var accessToken = given()
				.basePath("/auth/signin")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_XML)
					.accept(TestConfigs.CONTENT_TYPE_XML)				
				.body(user)
					.when()
				.post()
					.then()
						.statusCode(200)
						.extract()
						.body()
							.as(TokenVO.class)
							.getAccessToken();

		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
				.setBasePath("/api/book/v1").setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL)).addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}

	@Test
	@Order(1)
	public void testCreate() throws JsonMappingException, JsonProcessingException {
		mockBook();

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
					.body(book)
					.when()
					.post()
				.then()
					.statusCode(200)
						.extract()
						.body()
							.asString();

		BookVO persistedBook = objectMapper.readValue(content, BookVO.class);
		book = persistedBook;

		assertNotNull(persistedBook);
		assertNotNull(persistedBook.getId());
		assertNotNull(persistedBook.getAuthor());
		assertNotNull(persistedBook.getPrice());
		assertNotNull(persistedBook.getTitle());

		assertTrue(persistedBook.getId() > 0);

		assertEquals("Nigel Poulton", persistedBook.getAuthor());
		assertEquals("Docker Deep Dive", persistedBook.getTitle());
		assertEquals(Double.valueOf(55.99), persistedBook.getPrice());
	}

	@Test
	@Order(2)
	public void testUpdate() throws JsonMappingException, JsonProcessingException {
		book.setAuthor("Piquet Souto Maior");

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.body(book).when().post()
				.then().statusCode(200).extract().body().asString();

		BookVO persistedBook = objectMapper.readValue(content, BookVO.class);
		book = persistedBook;

		assertNotNull(persistedBook);
		assertNotNull(persistedBook.getId());
		assertNotNull(persistedBook.getAuthor());
		assertNotNull(persistedBook.getTitle());
		assertNotNull(persistedBook.getPrice());

		assertTrue(persistedBook.getId() > 0);

		assertEquals("Piquet Souto Maior", persistedBook.getAuthor());
		assertEquals("Docker Deep Dive", persistedBook.getTitle());
		assertEquals(Double.valueOf(55.99), persistedBook.getPrice());
	}

	@Test
	@Order(3)
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		mockBook();

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.pathParam("id", book.getId()).when().get("{id}").then().statusCode(200).extract().body().asString();

		BookVO persistedBook = objectMapper.readValue(content, BookVO.class);
		book = persistedBook;

		assertNotNull(persistedBook);
		assertNotNull(persistedBook.getId());
		assertNotNull(persistedBook.getAuthor());
		assertNotNull(persistedBook.getTitle());
		assertNotNull(persistedBook.getPrice());

		assertEquals(book.getId(), persistedBook.getId());

		assertEquals("Piquet Souto Maior", persistedBook.getAuthor());
		assertEquals("Docker Deep Dive", persistedBook.getTitle());
		assertEquals(Double.valueOf(55.99), persistedBook.getPrice());

	}

	@Test
	@Order(4)
	public void testDelete() throws JsonMappingException, JsonProcessingException {
		given().spec(specification)
			.contentType(TestConfigs.CONTENT_TYPE_XML)
			.accept(TestConfigs.CONTENT_TYPE_XML)
			.pathParam("id", book.getId()).when()
				.delete("{id}").then().statusCode(204);
	}
	
	@Test
	@Order(5)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.queryParams("page", 1, "size", 6, "direction", "asc")
				.accept(TestConfigs.CONTENT_TYPE_XML)
					.when()
					.get()
				.then()
					.statusCode(200)
						.extract()
						.body()
							.asString();

		PagedModelBook wrapper = objectMapper.readValue(content, PagedModelBook.class);
		
		var books = wrapper.getContent();
		
		BookVO foundBookOne = books.get(0);

		assertNotNull(foundBookOne.getId());
		assertNotNull(foundBookOne.getAuthor());
		assertNotNull(foundBookOne.getTitle());
		assertNotNull(foundBookOne.getPrice());

		assertEquals(7, foundBookOne.getId());
		
		assertEquals("Eric Freeman, Elisabeth Freeman, Kathy Sierra, Bert Bates", foundBookOne.getAuthor());
		assertEquals("Head First Design Patterns", foundBookOne.getTitle());
		assertEquals(110.0, foundBookOne.getPrice());

		BookVO foundBookSix = books.get(5);

		assertNotNull(foundBookSix.getId());
		assertNotNull(foundBookSix.getAuthor());
		assertNotNull(foundBookSix.getTitle());
		assertNotNull(foundBookSix.getPrice());
		
		assertEquals(13, foundBookSix.getId());
		
		assertEquals("Richard Hunter e George Westerman", foundBookSix.getAuthor());
		assertEquals("O verdadeiro valor de TI", foundBookSix.getTitle());
		assertEquals(95.0, foundBookSix.getPrice());
	}
	
	@Test
	@Order(6)
	public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {

		RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
			.setBasePath("/api/book/v1").setPort(TestConfigs.SERVER_PORT)
			.addFilter(new RequestLoggingFilter(LogDetail.ALL)).addFilter(new ResponseLoggingFilter(LogDetail.ALL))
			.build();
		
		given().spec(specificationWithoutToken)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
					.when()
					.get()
				.then()
					.statusCode(403);
	}
	
	@Test
	@Order(7)
	public void testHATEOAS() throws JsonMappingException, JsonProcessingException {

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.queryParams("page", 1, "size", 6, "direction", "asc")
				.accept(TestConfigs.CONTENT_TYPE_XML)
					.when()
					.get()
				.then()
					.statusCode(200)
						.extract()
						.body()
							.asString();
		
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/book/v1/7</href></links>"));
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/book/v1/4</href></links>"));
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/book/v1/10</href></links>"));

		assertTrue(content.contains("<links><rel>first</rel><href>http://localhost:8888/api/book/v1?direction=asc&amp;page=0&amp;size=6&amp;sort=title,asc</href></links>"));
		assertTrue(content.contains("<links><rel>prev</rel><href>http://localhost:8888/api/book/v1?direction=asc&amp;page=0&amp;size=6&amp;sort=title,asc</href></links>"));
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/book/v1?page=1&amp;size=6&amp;direction=asc</href></links>"));
		assertTrue(content.contains("<links><rel>next</rel><href>http://localhost:8888/api/book/v1?direction=asc&amp;page=2&amp;size=6&amp;sort=title,asc</href></links>"));
		assertTrue(content.contains("<links><rel>last</rel><href>http://localhost:8888/api/book/v1?direction=asc&amp;page=2&amp;size=6&amp;sort=title,asc</href></links>"));

		assertTrue(content.contains("<page><size>6</size><totalElements>15</totalElements><totalPages>3</totalPages><number>1</number></page>"));

		
	}

	private void mockBook() {
        book.setTitle("Docker Deep Dive");
        book.setAuthor("Nigel Poulton");
        book.setPrice(Double.valueOf(55.99));
        book.setLaunchDate(new Date());
    }  

}
