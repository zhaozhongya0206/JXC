package com.mf.sample.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mf.entity.Log;
import com.mf.entity.project.ProjectManager;
import com.mf.entity.sample.SampleManager;
import com.mf.project.service.ProjectManagerService;
import com.mf.sample.service.SampleManagerService;
import com.mf.service.LogService;
import com.mf.util.StringUtil;

/**
 * @ClassName:SampleManagerController  
 * @Description:样品管理controller
 * @author zzy
 * @date 2019年1月23日
 * @version V1.0
 */
@RestController
@RequestMapping("/sample/manager")
public class SampleManagerController {

	private static final Logger log = LoggerFactory.getLogger(SampleManagerController.class);
			
	@Resource
	private SampleManagerService sampleManagerService;
	
	@Resource
	private ProjectManagerService projectManagerService;
	
	@Resource
	private LogService logService;
	
	/**
	 * 下拉框模糊查询
	 * @param q
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/comboList")
	public List<SampleManager> comboList()throws Exception{
		return sampleManagerService.findProjectName();
	}
	
	/**
	 * 分页查询样品管理信息
	 * @param sampleManager
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public Map<String,Object> list(SampleManager sampleManager, @RequestParam(value="page",required=false)Integer page,@RequestParam(value="rows",required=false)Integer rows)throws Exception{
		log.info("list sampleManager: " + sampleManager.toString());
		Map<String,Object> resultMap = new HashMap<>();
		List<SampleManager> customerList = sampleManagerService.list(sampleManager, page, rows, Direction.ASC, "id");
		Long total = sampleManagerService.getCount(sampleManager);
		resultMap.put("rows", customerList);
		resultMap.put("total", total);
		return resultMap;
	}
	
	/**
	 * 添加或者修改样品管理信息
	 * @param sampleManager
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	public Map<String,Object> save(SampleManager sampleManager)throws Exception{
		log.info("save sampleManager: " + sampleManager.toString());
		Map<String,Object> resultMap=new HashMap<>();
		if(sampleManager.getId() != null){
			logService.save(new Log(Log.UPDATE_ACTION,"更新样品管理信息"+sampleManager));
		} else {
			logService.save(new Log(Log.ADD_ACTION,"添加样品管理信息"+sampleManager));
		}
		sampleManagerService.save(sampleManager);
		logService.save(new Log(Log.ADD_ACTION, "添加或者修改样品管理信息" + sampleManagerService.findById(sampleManager.getId())));
		resultMap.put("id", sampleManager.getId());
		resultMap.put("success", true);
		return resultMap;
	}
	
	/**
	 * 删除样品管理信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	public Map<String,Object> delete(String ids)throws Exception{
		log.info("delete ids: " + ids);
		Map<String,Object> resultMap=new HashMap<>();
		String []idsStr=ids.split(",");
		for(int i=0;i<idsStr.length;i++){
			int id=Integer.parseInt(idsStr[i]);
			sampleManagerService.delete(id);
			logService.save(new Log(Log.DELETE_ACTION,"删除样品管理信息" + sampleManagerService.findById(id)));
		}
		resultMap.put("success", true);
		return resultMap;
	}
	
	@RequestMapping("/findProjectSample")
	public Map<String,Object> findProjectSample(String projectId, String isUpdateBool)throws Exception {
		Map<String,Object> resultMap = new HashMap<>();
		log.info("findProjectSample projectId: " + projectId + ",isUpdateBool:" + isUpdateBool);
		ProjectManager projectManager = projectManagerService.findById(Integer.parseInt(projectId));
		if(projectManager != null) {
			resultMap.put("projectManager", projectManager);
			log.info("findProjectSample projectId: " + projectId + ",ProjectCode:" + projectManager.toString());
			if(StringUtil.isNotEmpty(projectManager.getSampleId())) {
				SampleManager sampleManager = sampleManagerService.findById(Integer.parseInt(projectManager.getSampleId()));
				if(sampleManager != null) {
					log.info("findProjectSample projectId: " + projectId + ",SampleCode:" + sampleManager.toString());
					resultMap.put("sampleManager", sampleManager);
					if("0".equals(isUpdateBool)) {
						projectManager.setId(null);
						projectManager.setProjectFlag(null);
						/*projectManager.setProjectCode("");
						projectManager.setProjectName("");
						projectManager.setPhotoCode("");*/
						sampleManager.setId(null);
						//sampleManager.setSampleCode("");
						//sampleManager.setSampleName("");
					}
				}
			}
			resultMap.put("success", true);
		} else {
			resultMap.put("success", false);
		}
		return resultMap;
	}
	
}
