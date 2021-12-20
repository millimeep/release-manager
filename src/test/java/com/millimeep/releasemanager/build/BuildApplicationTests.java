package com.millimeep.releasemanager.build;

import com.millimeep.releasemanager.component.ReleaseComponent;
import com.millimeep.releasemanager.component.ReleaseComponentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BuildApplicationTests {
    private static final Logger log = LoggerFactory.getLogger(BuildApplicationTests.class);

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ReleaseComponentRepository releaseComponentRepository;

    @Test
    @DirtiesContext
    void getBuild() {
        var releaseComponent = new ReleaseComponent("Test", "https://github.com/millimeep/release-manager.git");
        Build build = new Build("url", "8.5.2");
        var newComponent = releaseComponentRepository.save(releaseComponent);
        newComponent.addBuild(build);
        newComponent = releaseComponentRepository.save(newComponent);
        Assertions.assertFalse(newComponent.getBuilds().isEmpty());
        var buildId = newComponent.getBuilds().get(0).getId();
        ResponseEntity<EntityModel<Build>> response =
                restTemplate.exchange(
                        RequestEntity.get("http://localhost:%d/build/%d".formatted(port, buildId)).build(),
                        new ParameterizedTypeReference<>() {
                        });
        EntityModel<Build> body = response.getBody();
        assert body != null;
        Build actual = body.getContent();
        Assertions.assertEquals(build, actual);
    }
}
