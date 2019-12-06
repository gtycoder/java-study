package com.gty.dao;

import com.gty.domain.Person;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TestMapping {

    int addPerson(Person person);

    List<Person> getAllPerson();

}
