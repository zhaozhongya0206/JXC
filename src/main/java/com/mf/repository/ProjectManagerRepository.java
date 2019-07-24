package com.mf.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.mf.entity.project.ProjectManager;

public interface ProjectManagerRepository extends JpaRepository<ProjectManager, Integer>,JpaSpecificationExecutor<ProjectManager>{

	@Query(value="select * from project_manager ", nativeQuery=true)
	public List<ProjectManager> findProjectName();
	
}
