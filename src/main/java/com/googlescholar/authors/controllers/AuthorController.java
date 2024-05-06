package com.googlescholar.authors.controllers;

import com.googlescholar.authors.models.Author;
import com.googlescholar.authors.services.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;

    @GetMapping({"", "/"})
    public String ShowAuthorsList(Model model) {
        List<Author> authors = authorRepository.findAll();

        model.addAttribute("authors", authors);

        return "authors/index";
    }
}
