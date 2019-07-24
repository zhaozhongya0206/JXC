package com.mf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.mf.entity.detection.DetectionManager;

public interface DetectionManagerRepository extends JpaRepository<DetectionManager, Integer>,JpaSpecificationExecutor<DetectionManager>{
	
	@Query(value="select * from detection_manager ", nativeQuery=true)
	public List<DetectionManager> findDetectionManager();
	
}
