package com.millimeep.releasemanager.component;

import com.millimeep.releasemanager.release.DuplicateItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.millimeep.releasemanager.release.DuplicateItem.duplicateComponent;
import static com.millimeep.releasemanager.release.ItemNotFound.componentNotFound;

@RestController
public class ReleaseComponentController {
    private static final Logger LOG = LoggerFactory.getLogger(ReleaseComponentController.class);
    private final ReleaseComponentRepository releaseComponentRepository;
    private final ReleaseComponentModelAssembler modelAssembler;

    public ReleaseComponentController(ReleaseComponentRepository releaseComponentRepository, ReleaseComponentModelAssembler modelAssembler) {
        this.releaseComponentRepository = releaseComponentRepository;
        this.modelAssembler = modelAssembler;
    }

    @GetMapping("/components")
    public CollectionModel<EntityModel<ReleaseComponent>> all() {
        return modelAssembler.toCollectionModel(releaseComponentRepository.findAll());
    }

    @GetMapping("component/{id}")
    public EntityModel<ReleaseComponent> component(@PathVariable Long id) {
        ReleaseComponent releaseComponent = releaseComponentRepository.findById(id).orElseThrow(() -> componentNotFound(id));
        return modelAssembler.toModel(releaseComponent);
    }

    @PostMapping("/components")
    ResponseEntity<?> newComponent(@RequestBody ReleaseComponent releaseComponent) {
        try {
            EntityModel<ReleaseComponent> entityModel = modelAssembler.toModel
                    (releaseComponentRepository.save(releaseComponent));

            return ResponseEntity
                    .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(entityModel);
        } catch (DataIntegrityViolationException e) {
            DuplicateItem error = duplicateComponent(releaseComponent);
            LOG.warn(error.getMessage());
            throw error;
        }
    }
}
