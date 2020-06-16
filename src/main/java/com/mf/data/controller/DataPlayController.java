package com.mf.data.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mf.data.service.DataPlayService;
import com.mf.entity.data.ExceptionManager;

@RestController
@RequestMapping("/data/play")
public class DataPlayController {

	private static final Logger log = LoggerFactory.getLogger(DataPlayController.class);
	
	@Resource
	private DataPlayService dataPlayService;
	
	@RequestMapping("/list")
	public Map<String,Object> list(ExceptionManager exceptionManager, @RequestParam(value="page",required=false)Integer page,
			@RequestParam(value="rows",required=false)Integer rows, HttpServletRequest request)throws Exception{
		log.info("list exceptionManager: " + exceptionManager.toString());
		Map<String,Object> resultMap = new HashMap<>();
		List<ExceptionManager> customerList = dataPlayService.list(exceptionManager, page, rows, Direction.DESC, "id");
		/*String path = request.getSession().getServletContext().getRealPath("/");
		log.info("path: " + path);
		for(ExceptionManager exceptionDto : customerList) {
			dataPlayService.getFiles(exceptionDto.getFolderVideoPath(), path + Contants.EXCEPTION_OUT_VIDEO_PATH);
			dataPlayService.getFiles(exceptionDto.getFolderPicturePath(), path + Contants.EXCEPTION_OUT_IMG_PATH);
		}*/
		Long total = dataPlayService.getCount(exceptionManager);
		resultMap.put("rows", customerList);
		resultMap.put("total", total);
		return resultMap;
	}
	
	@RequestMapping("/updateFlag")
	public Map<String,Object> updateFlag(ExceptionManager exceptionManager) {
		Map<String,Object> resultMap = new HashMap<>();
		dataPlayService.updateFlag(exceptionManager);
		resultMap.put("success", true);
		return resultMap;
	}
	
	@RequestMapping("/delete")
	public Map<String,Object> delete(String ids)throws Exception{
		log.info("delete ids: " + ids);
		Map<String,Object> resultMap=new HashMap<>();
		String []idsStr=ids.split(",");
		for(int i=0;i<idsStr.length;i++){
			int id=Integer.parseInt(idsStr[i]);
			dataPlayService.delete(id);
		}
		resultMap.put("success", true);
		return resultMap;
	}
	
}
