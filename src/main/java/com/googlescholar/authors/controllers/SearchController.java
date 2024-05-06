package com.googlescholar.authors.controllers;

import com.googlescholar.authors.models.Author;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

import java.net.URI;
import java.net.http.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

@Controller
@RequestMapping("/search")
public class SearchController {

    @GetMapping({"", "/"})
    public String showSearchList(Model model) {
        String key = "key=15926fac5fb1a7b191a3e7016f757dd82c12cbb1db9525ae85ca84d9574fcc15";
        String query = "Universidad Nacional Autónoma de México";

        String queryEncoded = query.replace(" ", "+");

        String mauthors = "mauthors=" + queryEncoded;

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://serpapi.com/search?engine=google_scholar_profiles&mauthors=unam" + "&" + key))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Convertir la respuesta JSON en un objeto JsonNode
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.body());

            // Obtener la lista de autores del JSON
            List<Author> authors = getAuthorsFromJson(jsonResponse);

            // Agregar la lista de autores al modelo
            model.addAttribute("authors", authors);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return "search/index";
    }

    private List<Author> getAuthorsFromJson(JsonNode jsonResponse) {
        List<Author> authors = new ArrayList<>();

        // Obtener el nodo "profiles" del JSON
        JsonNode profilesNode = jsonResponse.get("profiles");

        if (profilesNode != null && profilesNode.isArray()) {

            // iterar sobre la lista de autores y obtener la data
            for (JsonNode profileNode : profilesNode) {
                Author author = new Author();
                author.setName(profileNode.get("name").asText());
                author.setLink(profileNode.get("link").asText());
                author.setAffiliation(profileNode.get("affiliations").asText());
                author.setEmail(profileNode.get("email").asText());
                author.setCitedBy(profileNode.get("cited_by").asInt());
                author.setThumbnail(profileNode.get("thumbnail").asText());

                authors.add(author);
            }
        }

        return authors;
    }
}