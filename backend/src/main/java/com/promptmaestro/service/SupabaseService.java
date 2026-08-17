package com.promptmaestro.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promptmaestro.entity.MateriaPrima;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class SupabaseService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${supabase.url:https://db.jzdyzdevcydamitjcipz.supabase.co}")
    private String supabaseUrl;

    @Value("${supabase.anon-key:}")
    private String anonKey;

    public SupabaseService(ObjectMapper objectMapper) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = objectMapper;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (anonKey != null && !anonKey.isEmpty()) {
            headers.set("apikey", anonKey);
            headers.set("Authorization", "Bearer " + anonKey);
        }
        return headers;
    }

    public List<MateriaPrima> getAll() {
        String url = supabaseUrl + "/rest/v1/materia_prima?select=*";
        HttpEntity<String> entity = new HttpEntity<>(getHeaders());
        ResponseEntity<MateriaPrima[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, MateriaPrima[].class);
        return Arrays.asList(response.getBody() != null ? response.getBody() : new MateriaPrima[0]);
    }

    public MateriaPrima create(MateriaPrima item) {
        String url = supabaseUrl + "/rest/v1/materia_prima";
        HttpEntity<MateriaPrima> entity = new HttpEntity<>(item, getHeaders());
        ResponseEntity<MateriaPrima> response = restTemplate.exchange(url, HttpMethod.POST, entity, MateriaPrima.class);
        return response.getBody();
    }

    public Optional<MateriaPrima> update(Long id, MateriaPrima item) {
        String url = supabaseUrl + "/rest/v1/materia_prima?id=eq." + id;
        HttpEntity<MateriaPrima> entity = new HttpEntity<>(item, getHeaders());
        restTemplate.exchange(url, HttpMethod.PATCH, entity, String.class);
        return getById(id);
    }

    public void delete(Long id) {
        String url = supabaseUrl + "/rest/v1/materia_prima?id=eq." + id;
        HttpEntity<String> entity = new HttpEntity<>(getHeaders());
        restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
    }

    public Optional<MateriaPrima> getById(Long id) {
        String url = supabaseUrl + "/rest/v1/materia_prima?id=eq." + id + "&select=*";
        HttpEntity<String> entity = new HttpEntity<>(getHeaders());
        ResponseEntity<MateriaPrima[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, MateriaPrima[].class);
        MateriaPrima[] items = response.getBody();
        if (items != null && items.length > 0) {
            return Optional.of(items[0]);
        }
        return Optional.empty();
    }
}
