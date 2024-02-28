package com.example.clientContactMicroserviceExceptionHandling.controller;


import com.example.clientContactMicroserviceExceptionHandling.model.Person;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class PersonController {

    private final RestTemplate restTemplate;
    String url = "http://localhost:8080";

    public PersonController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping(value = "/people/{name}/{email}/{age}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Person>> addPerson(@PathVariable(name = "name")String name,
                                    @PathVariable(name = "email")String email,
                                    @PathVariable(name = "age") int age){
        Person person = new Person(name, email, age);
        try{
            restTemplate.postForLocation(url + "/contacts", person);
            Person[] people = restTemplate.getForObject(url + "/contacts", Person[].class);
            return new ResponseEntity<List<Person>>(Arrays.asList(people), HttpStatus.OK);

        }catch (HttpStatusCodeException exception){
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("error", exception.getResponseBodyAsString());
            return new ResponseEntity<List<Person>>(new ArrayList<Person>(), httpHeaders, exception.getStatusCode());
        }
    }
}
