package com.soprasteria.bookservice;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.soprasteria.bookservice.model.Book;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
@RequestMapping("")
public class BookServiceApplication {
	
	@Autowired
	private EurekaClient discoveryClient;
	
	public String serviceUrl() {
	    InstanceInfo instance = discoveryClient.getNextServerFromEureka("rating-service", false);
	    String ratingServiceUrl = instance.getHomePageUrl();
	    return ratingServiceUrl;
	}

	public static void main(String[] args) {
		SpringApplication.run(BookServiceApplication.class, args);
	}

	private List<Book> bookList = Arrays.asList(new Book(1L, "Baeldung goes to the market", "Tim Schimandle"),
			new Book(2L, "Baeldung goes to the park", "Slavisa"));

	@GetMapping("/")
	public List<Book> findAllBooks() {
		return bookList;
	}

	@GetMapping("/{bookId}")
	public Book findBook(@PathVariable Long bookId) {
		System.out.println(serviceUrl());
		return bookList.stream().filter(b -> b.getId().equals(bookId)).findFirst().orElse(null);
	}

}
