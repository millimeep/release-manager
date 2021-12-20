package com.millimeep.releasemanager.build;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildRepository extends JpaRepository<Build, Long> {
}