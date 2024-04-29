package com.googlescholar.authors.services;

import com.googlescholar.authors.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Integer>{

}