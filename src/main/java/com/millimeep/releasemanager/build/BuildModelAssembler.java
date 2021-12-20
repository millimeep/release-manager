package com.millimeep.releasemanager.build;

import com.millimeep.releasemanager.component.ReleaseComponentController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BuildModelAssembler implements RepresentationModelAssembler<Build, EntityModel<Build>> {

    @Override
    public EntityModel<Build> toModel(Build build) {
        return EntityModel.of(build,
                linkBuild(build).withSelfRel(),
                linkTo(methodOn(ReleaseComponentController.class)
                        .component(build.getReleaseComponent().getId()))
                        .withRel("component"));
    }

    public Link linkBuilds(Build build) {
        return linkBuild(build).withRel("builds");
    }

    private WebMvcLinkBuilder linkBuild(Build build) {
        return linkTo(methodOn(BuildController.class)
                .build(build.getId()));
    }
}
