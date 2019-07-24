package com.mf.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import com.mf.entity.sample.SampleManager;

public interface SampleManagerRepository extends JpaRepository<SampleManager, Integer>,JpaSpecificationExecutor<SampleManager>{

	@Query(value="select * from sample_manager ", nativeQuery=true)
	public List<SampleManager> findSampleName();
	
	@Query(value="select * from sample_manager where project_id = ?1", nativeQuery=true)
	public SampleManager findSample(String projectId);
	
}
