package com.mf.detection.service;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import com.mf.entity.detection.DetectionManager;

public interface DetectionManagerService {
	
	/**
	 * 根据条件分页查询信息
	 * @param detectionManager
	 * @param page
	 * @param pageSize
	 * @param direction
	 * @param properties
	 * @return
	 */
	public List<DetectionManager> list(DetectionManager detectionManager,Integer page,Integer pageSize,Direction direction,String...properties);
	
	/**
	 * 获取总记录数
	 * @param detectionManager
	 * @return
	 */
	public Long getCount(DetectionManager detectionManager);
	
	/**
	 * 添加或者修改信息
	 * @param detectionManager
	 */
	public void save(DetectionManager detectionManager);
	
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
	public DetectionManager findById(Integer id);
	
	public List<DetectionManager> findDetectionByProjectId(Integer projectId);
	
	public List<DetectionManager> findDetectionManager();
	
	/**
	 * 保存绘制信息
	 * @param id
	 * @param listX
	 * @param listY
	 * @param listWidth
	 * @param listHeight
	 */
	public void saveImg(DetectionManager detectionManager);
	
}