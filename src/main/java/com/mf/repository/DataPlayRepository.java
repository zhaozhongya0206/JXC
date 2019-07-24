package com.mf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.mf.entity.data.ExceptionManager;

public interface DataPlayRepository extends JpaRepository<ExceptionManager, Integer>,JpaSpecificationExecutor<ExceptionManager>{
	
	
}
