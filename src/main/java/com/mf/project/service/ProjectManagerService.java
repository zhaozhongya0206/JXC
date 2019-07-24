package com.mf.project.service;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import com.mf.entity.project.ProjectManager;

public interface ProjectManagerService {
	
	public List<ProjectManager> findProjectName();
	
	/**
	 * 根据条件分页查询信息
	 * @param ProjectManager
	 * @param page
	 * @param pageSize
	 * @param direction
	 * @param properties
	 * @return
	 */
	public List<ProjectManager> list(ProjectManager ProjectManager,Integer page,Integer pageSize,Direction direction,String...properties);
	
	/**
	 * 获取总记录数
	 * @param ProjectManager
	 * @return
	 */
	public Long getCount(ProjectManager ProjectManager);
	
	/**
	 * 添加或者修改信息
	 * @param ProjectManager
	 */
	public void save(ProjectManager ProjectManager) throws Exception;
	
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
	public ProjectManager findById(Integer id);
	
	/**
	 * 修改项目标识
	 * @param id
	 * @param projectFlag
	 */
	public void updateFlag(Integer id, Integer projectFlag);
	
	
	public void updatePhotoStatus(String photoCode, String photoStatus);
	
}
