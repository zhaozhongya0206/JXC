package com.mf.sample.service;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import com.mf.entity.sample.SampleManager;

public interface SampleManagerService {
	
	public List<SampleManager> findProjectName();
	
	/**
	 * 根据条件分页查询信息
	 * @param sampleManager
	 * @param page
	 * @param pageSize
	 * @param direction
	 * @param properties
	 * @return
	 */
	public List<SampleManager> list(SampleManager sampleManager,Integer page,Integer pageSize,Direction direction,String...properties);
	
	/**
	 * 获取总记录数
	 * @param sampleManager
	 * @return
	 */
	public Long getCount(SampleManager sampleManager);
	
	/**
	 * 添加或者修改信息
	 * @param sampleManager
	 */
	public void save(SampleManager sampleManager) throws Exception;
	
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
	public SampleManager findById(Integer id);
	
	public SampleManager findSample(String projectId);
	
	public void updateProjectId(Integer id, String projectId);
	
}
