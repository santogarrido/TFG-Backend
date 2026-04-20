package com.example.demo.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Court;
import java.util.List;

@Repository("courtRepository")
public interface CourtRepository extends JpaRepository<Court, Serializable> {

	List<Court> findByFacilityId(int id);

}
