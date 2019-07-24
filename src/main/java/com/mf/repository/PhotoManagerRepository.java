package com.mf.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import com.mf.entity.photo.PhotoManager;

public interface PhotoManagerRepository extends JpaRepository<PhotoManager, Integer>,JpaSpecificationExecutor<PhotoManager>{

	@Query(value="select * from photo_manager a order by a.id asc", nativeQuery=true)
	public List<PhotoManager> findByTitle();
	
	@Query(value="select * from photo_manager where photo_status='0'", nativeQuery=true)
	public List<PhotoManager> findSuccessStatus();
	
	
	@Query(value="select * from photo_manager where photo_code=?1", nativeQuery=true)
	public PhotoManager findSuccessByPhotoCode(String photoCode);
}
