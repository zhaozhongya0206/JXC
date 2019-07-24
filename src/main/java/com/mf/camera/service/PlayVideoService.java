package com.mf.camera.service;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.mf.entity.camera.PlayVideo;

/**
 * @ClassName:PlayVideoService 
 * @Description:上传视频service
 * @author zzy
 * @date 2018年12月10日
 * @version V1.0
 */
public interface PlayVideoService {

    /**
     * 保存上传视频
     * @throws Exception
     */
    public void save(CommonsMultipartFile multipartRequest, String title)throws Exception;
    
    /**
     * 查询播放视频信息
     * @param id
     * @throws Exception
     */
    public String operationPlay(Integer id)throws Exception;
    
    /**
	 * 模糊查询视频标题信息
	 * @param title
	 * @return
	 */
	public List<PlayVideo> findByTitle(String title);
	
	/**
	 * 根据条件分页查询视频信息
	 * @param playVideo
	 * @param page
	 * @param pageSize
	 * @param direction
	 * @param properties
	 * @return
	 */
	public List<PlayVideo> list(PlayVideo playVideo,Integer page,Integer pageSize,Direction direction,String...properties);
	
	/**
	 * 获取总记录数
	 * @param test
	 * @return
	 */
	public Long getCount(PlayVideo playVideo);
    
	/**
	 * 根据id删除
	 * @param id
	 */
	public void delete(Integer id) throws Exception;
	
	/**
	 * 根据id查询实体
	 * @param id
	 * @return
	 */
	public PlayVideo findById(Integer id);
	
}
