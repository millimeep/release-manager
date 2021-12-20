package com.millimeep.releasemanager.build;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.millimeep.releasemanager.component.ReleaseComponent;

import javax.persistence.*;

@Entity
@Table(name = "build",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"artifactUrl", "version"})})
public class Build {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne()
    @JsonBackReference
    @JoinColumn(name = "release_component_id")
    private ReleaseComponent releaseComponent;
    @Column(nullable = false)
    private String artifactUrl;
    @Column(nullable = false)
    private String version;

    public Build() {
    }

    public Build(String artifactUrl, String version) {
        this.artifactUrl = artifactUrl;
        this.version = version;
    }

    public String getArtifactUrl() {
        return artifactUrl;
    }

    public void setArtifactUrl(String artifactUrl) {
        this.artifactUrl = artifactUrl;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ReleaseComponent getReleaseComponent() {
        return releaseComponent;
    }

    public void setReleaseComponent(ReleaseComponent releaseComponent) {
        this.releaseComponent = releaseComponent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Build build = (Build) o;

//        if (!releaseComponent.equals(build.releaseComponent)) return false;
        if (!artifactUrl.equals(build.artifactUrl)) return false;
        return version.equals(build.version);
    }

    @Override
    public int hashCode() {
//        int result = releaseComponent.hashCode();
        int result =  artifactUrl.hashCode();
        result = 31 * result + version.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Build artifact %s, version %s".formatted(artifactUrl, version);
    }
}