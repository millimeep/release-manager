package com.millimeep.releasemanager.component;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.millimeep.releasemanager.build.Build;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
@Table(name = "component",
        uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class ReleaseComponent {
    @OneToMany(mappedBy = "releaseComponent", cascade = ALL)
    @JsonManagedReference
    private final List<Build> builds = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String scmUrl;

    public ReleaseComponent() {
    }

    public ReleaseComponent(String name, String scmUrl) {
        this.name = name;
        this.scmUrl = scmUrl;
    }

    public List<Build> getBuilds() {
        return builds;
    }

    public void addBuild(Build build) {
        builds.add(build);
        build.setReleaseComponent(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScmUrl() {
        return scmUrl;
    }

    public void setScmUrl(String scmUrl) {
        this.scmUrl = scmUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReleaseComponent releaseComponent = (ReleaseComponent) o;

        return name.equals(releaseComponent.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}