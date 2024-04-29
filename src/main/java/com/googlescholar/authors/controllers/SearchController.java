package com.googlescholar.authors.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

@Controller
@RequestMapping("/search")
public class SearchController {

    @GetMapping({"", "/"})
    public String showSearchList(Model model) {
        String key = "15926fac5fb1a7b191a3e7016f757dd82c12cbb1db9525ae85ca84d9574fcc15";

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://serpapi.com/search?engine=google_scholar_profiles&mauthors=unam&key=" + key))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Convertir la respuesta JSON en un objeto JsonNode
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.body());

            // Obtener la lista de autores del JSON
            List<String> authors = getAuthorsFromJson(jsonResponse);

            // Agregar la lista de autores al modelo
            model.addAttribute("authors", authors);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return "search/index";
    }

    // Método para extraer los nombres de los autores del JSON
    private List<String> getAuthorsFromJson(JsonNode jsonResponse) {
        List<String> authors = new ArrayList<>();

        // Obtener el nodo "profiles" del JSON
        JsonNode profilesNode = jsonResponse.get("profiles");

        if (profilesNode != null && profilesNode.isArray()) {

            // iterar sobre el arreglo de perfiles y obtener el nombre
            for (JsonNode profileNode : profilesNode) {
                String name = profileNode.get("name").asText();
                authors.add(name);
            }
        }

        return authors;
    }
}