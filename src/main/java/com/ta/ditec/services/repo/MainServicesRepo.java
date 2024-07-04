package com.ta.ditec.services.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.MainServices;

@Repository
public interface MainServicesRepo extends JpaRepository<MainServices, Long> {

}
