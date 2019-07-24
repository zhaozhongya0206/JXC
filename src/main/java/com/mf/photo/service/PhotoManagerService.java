package com.mf.photo.service;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.mf.entity.photo.PhotoManager;

public interface PhotoManagerService {
	
	public List<PhotoManager> findByTitle();
	
	/**
	 * 根据条件分页查询信息
	 * @param photoManager
	 * @param page
	 * @param pageSize
	 * @param direction
	 * @param properties
	 * @return
	 */
	public List<PhotoManager> list(PhotoManager photoManager,Integer page,Integer pageSize,Direction direction,String...properties);
	
	/**
	 * 获取总记录数
	 * @param photoManager
	 * @return
	 */
	public Long getCount(PhotoManager photoManager);
	
	/**
	 * 添加或者修改信息
	 * @param photoManager
	 */
	public void save(PhotoManager photoManager);
	
	/**
	 * 根据id删除
	 * @param id
	 */
	public void delete(Integer id);
	
	/**
	 * 根据id查询实体
	 * @param id
	 * @return
	 */
	public PhotoManager findById(Integer id);
	
	public List<PhotoManager> findSuccessStatus();
	
	public PhotoManager findSuccessByPhotoCode(String photoCode);
	
	public String MP4(CommonsMultipartFile multipartRequest, String filename) throws Exception;
	
	public void deleteFile(PhotoManager photoManager) throws Exception;
	
}
