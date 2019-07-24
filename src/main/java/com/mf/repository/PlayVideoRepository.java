package com.mf.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import com.mf.entity.camera.PlayVideo;

/**
 * @ClassName:PlayVideoRepository 
 * @Description:上传视频接口
 * @author zzy
 * @date 2018年12月10日
 * @version V1.0
 */
public interface PlayVideoRepository extends JpaRepository<PlayVideo, Integer>, JpaSpecificationExecutor<PlayVideo>{

	/**
	 * 根据视频标题模糊查询播放视频信息
	 * @param title
	 * @return
	 */
	@Query(value="select * from t_play_video where title like ?1", nativeQuery=true)
	public List<PlayVideo> findByTitle(String title);
	
	
}
