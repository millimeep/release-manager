package com.millimeep.releasemanager.component;

import com.millimeep.releasemanager.build.BuildModelAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ReleaseComponentModelAssembler implements RepresentationModelAssembler<ReleaseComponent, EntityModel<ReleaseComponent>> {
    private final BuildModelAssembler buildModelAssembler;

    public ReleaseComponentModelAssembler(BuildModelAssembler buildModelAssembler) {
        this.buildModelAssembler = buildModelAssembler;
    }

    @Override
    public EntityModel<ReleaseComponent> toModel(ReleaseComponent releaseComponent) {
        EntityModel<ReleaseComponent> releaseComponentEntityModel =
                EntityModel.of(releaseComponent,
                        linkTo(methodOn(ReleaseComponentController.class)
                                .component(releaseComponent.getId()))
                                .withSelfRel(),
                        linkTo(methodOn(ReleaseComponentController.class)
                                .all())
                                .withRel("components"));

        releaseComponent.getBuilds().stream()
                .map(buildModelAssembler::linkBuilds)
                .forEach(releaseComponentEntityModel::add);

        return releaseComponentEntityModel;
    }
}
