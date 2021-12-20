package com.millimeep.releasemanager.component;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReleaseComponentApplicationTests {
    private static final Logger log = LoggerFactory.getLogger(ReleaseComponentApplicationTests.class);

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        ReleaseComponent[] expected = new ReleaseComponent[]{
                new ReleaseComponent("Config", "https://github.com/millimeep/release-manager.git"),
                new ReleaseComponent("Payments", "https://github.com/millimeep/release-manager.git")
        };
        ResponseEntity<CollectionModel<EntityModel<ReleaseComponent>>> response =
                restTemplate.exchange("http://localhost:" + port + "/components",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });
        CollectionModel<EntityModel<ReleaseComponent>> body = response.getBody();
        assert body != null;
        List<ReleaseComponent> releaseComponents = body.getContent().stream()
                .map(EntityModel::getContent)
                .toList();
        Assertions.assertArrayEquals(expected, releaseComponents.toArray());
    }

    @Test
    @DirtiesContext
    void add() {
        ResponseEntity<EntityModel<ReleaseComponent>> postResponse =
                restTemplate.exchange(
                        RequestEntity.post("http://localhost:" + port + "/components").body(new ReleaseComponent("Credit", "https://github.com/millimeep/release-manager.git")),
                        new ParameterizedTypeReference<>() {
                        });
        EntityModel<ReleaseComponent> payload = postResponse.getBody();
        assert payload != null;
        var newLink = payload.getRequiredLink(IanaLinkRelations.SELF).getHref();

        ResponseEntity<EntityModel<ReleaseComponent>> getResponse =
                restTemplate.exchange(newLink,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });

        Assertions.assertEquals("Credit", getResponse.getBody().getContent().getName());
//        Assertions.assertArrayEquals(expected, postResponse.getBody());
    }

    @Test
    @DirtiesContext
    void duplicate() {
                restTemplate.exchange(
                        RequestEntity.post("http://localhost:" + port + "/components").body(new ReleaseComponent("Credit", "https://github.com/millimeep/release-manager.git")),
                        new ParameterizedTypeReference<>() {
                        });

        ResponseEntity<String> postResponse =
                restTemplate.exchange(
                        RequestEntity.post("http://localhost:" + port + "/components").body(new ReleaseComponent("Credit", "https://github.com/millimeep/release-manager.git")),
                        String.class);
        Assertions.assertEquals(CONFLICT, postResponse.getStatusCode());
    }
}
