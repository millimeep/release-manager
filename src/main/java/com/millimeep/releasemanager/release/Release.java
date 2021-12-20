package com.millimeep.releasemanager.release;

import com.millimeep.releasemanager.component.ReleaseComponent;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "release")
public class Release {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToMany()
    private List<ReleaseComponent> components;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}