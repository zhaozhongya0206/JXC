package com.mf.data.service;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import com.mf.entity.data.ExceptionManager;

public interface DataPlayService {
    
	public List<ExceptionManager> list(ExceptionManager exceptionManager,Integer page,Integer pageSize,Direction direction, String...properties);

	public Long getCount(ExceptionManager detectionManager);
	
	public LinkedHashMap<String,String> getFiles(String path, String exceptionPath) throws IOException;
	
	public void updateFlag(ExceptionManager detectionManager);
	
	/**
	 * 根据id删除
	 * @param id
	 */
	public void delete(Integer id);
	
}
