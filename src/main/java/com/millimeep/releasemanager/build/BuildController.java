package com.millimeep.releasemanager.build;

import com.millimeep.releasemanager.component.ReleaseComponent;
import com.millimeep.releasemanager.component.ReleaseComponentRepository;
import com.millimeep.releasemanager.release.DuplicateItem;
import com.millimeep.releasemanager.release.ItemNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.millimeep.releasemanager.release.DuplicateItem.duplicateBuild;
import static com.millimeep.releasemanager.release.ItemNotFound.buildNotFound;

@RestController
public class BuildController {
    private static final Logger LOG = LoggerFactory.getLogger(BuildController.class);

    private final BuildRepository buildRepository;
    private final BuildModelAssembler buildModelAssembler;

    private final ReleaseComponentRepository releaseComponentRepository;

    public BuildController(BuildRepository buildRepository,
                           BuildModelAssembler buildModelAssembler,
                           ReleaseComponentRepository releaseComponentRepository) {
        this.buildRepository = buildRepository;
        this.buildModelAssembler = buildModelAssembler;
        this.releaseComponentRepository = releaseComponentRepository;
    }

    @GetMapping("build/{id}")
    public EntityModel<Build> build(@PathVariable Long id) {
        Build build = buildRepository.findById(id).orElseThrow(() ->  buildNotFound(id));
        return buildModelAssembler.toModel(build);
    }

    @PostMapping("build/component/{componentId}")
    ResponseEntity<?> newBuild(@PathVariable Long componentId, @RequestBody Build build) {
        ReleaseComponent releaseComponent = releaseComponentRepository.findById(componentId)
                .orElseThrow(() -> new ItemNotFound(componentId));
        releaseComponent.addBuild(build);

        try {
            ReleaseComponent newBuild = releaseComponentRepository.save(releaseComponent);
            List<Build> builds = newBuild.getBuilds();
            if (builds.isEmpty()) {
                throw new ItemNotFound("No build added?");
            }
            Build build1 = builds.get(builds.size() - 1);
            EntityModel<Build> entityModel = buildModelAssembler.toModel(build1);
            return ResponseEntity
                    .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(entityModel);
        } catch (DataIntegrityViolationException e) {
            DuplicateItem error = duplicateBuild(build);
            LOG.warn(e.getMessage());
            throw error;
        }
    }}
