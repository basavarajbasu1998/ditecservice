package com.ta.ditec.services.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.UIDAIErrorcodes;




@Repository
public interface UIDAIerrorRepo extends JpaRepository<UIDAIErrorcodes, Integer>{

public	UIDAIErrorcodes findByErrorcode(String s);

}
