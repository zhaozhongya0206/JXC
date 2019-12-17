package com.mf.project.controller;

import java.io.File;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
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
 * @ClassName:ProjectManagerController  
 * @Description:任务管理controller
 * @author zzy
 * @date 2019年1月23日
 * @version V1.0
 */
@RestController
@RequestMapping("/project/manager")
public class ProjectManagerController {

	private static final Logger log = LoggerFactory.getLogger(ProjectManagerController.class);
			
	@Resource
	private ProjectManagerService projectManagerService;
	
	@Resource
	private LogService logService;
	
	@Resource
	private SampleManagerService sampleManagerService;
	
	/**
	 * 下拉框模糊查询
	 * @param q
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/comboList")
	public List<ProjectManager> comboList()throws Exception{
		return projectManagerService.findProjectName();
	}
	
	/**
	 * 分页查询项目信息
	 * @param ProjectManager
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	@RequiresPermissions(value="任务管理")
	public Map<String,Object> list(ProjectManager projectManager,@RequestParam(value="page",required=false)Integer page,@RequestParam(value="rows",required=false)Integer rows)throws Exception{
		log.info("list ProjectManager: " + projectManager.toString());
		Map<String,Object> resultMap = new HashMap<>();
		List<ProjectManager> customerList = projectManagerService.list(projectManager, page, rows, Direction.ASC, "id");
		Long total = projectManagerService.getCount(projectManager);
		resultMap.put("rows", customerList);
		resultMap.put("total", total);
		return resultMap;
	}
	
	/**
	 * 添加或者修改项目设置信息
	 * @param ProjectManager
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	@RequiresPermissions(value="任务管理")
	public Map<String,Object> save(ProjectManager projectManager)throws Exception{
		log.info("save ProjectManager: " + projectManager.toString());
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put("success", false);
		if(StringUtil.isEmpty(projectManager.getProjectCode().trim())) {
			resultMap.put("errorInfo", "委托单号为空，请重新重新输入！");
			return resultMap;
		} else {
			log.info("save ProjectManager: " + match(projectManager.getProjectCode().replaceAll(" ", "")));
			if(match(projectManager.getProjectCode().replaceAll(" ", ""))){
				resultMap.put("errorInfo", "委托单号包含中文，请重新重新输入！");
				return resultMap;
			}
		}
		projectManager.setProjectCode(projectManager.getProjectCode().trim());
		if(StringUtil.isEmpty(projectManager.getProjectName().trim())) {
			resultMap.put("errorInfo", "委托单名称为空，请重新重新输入！");
			return resultMap;
		}
		projectManager.setProjectName(projectManager.getProjectName().trim());
		if(StringUtil.isEmpty(projectManager.getPhotoCode())) {
			resultMap.put("errorInfo", "相机编号为空，请重新重新输入！");
			return resultMap;
		}
		if(StringUtil.isEmpty(projectManager.getSampleId())) {
			resultMap.put("errorInfo", "样品编号为空，请重新重新输入！");
			resultMap.put("success", false);
			return resultMap;
		}
		projectManager.setCreateTime(new Timestamp(System.currentTimeMillis()));
		projectManager.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		if(projectManager.getId() != null){
			ProjectManager dto = projectManagerService.findById(projectManager.getId());
			if(1 == dto.getProjectFlag()) {
				resultMap.put("errorInfo", "该任务处于开始状态不能修改！");
				resultMap.put("success", false);
				return resultMap;
			}
			logService.save(new Log(Log.UPDATE_ACTION,"更新项目设置信息"+projectManager));
		} else {
			//根据项目编码查询是否已存在数据
			ProjectManager dto = new ProjectManager();
			dto.setProjectCode(projectManager.getProjectCode());
			Long count = projectManagerService.getCount(dto);
			log.info("projectManagerService getCount: " + count);
			if(count > 0) {
				resultMap.put("errorInfo", "此委托单已存在！");
				return resultMap;
			}
			//判断样品信息是否存在
			/*SampleManager sampleManager = sampleManagerService.findSampleBySampleCode(projectManager.getSampleCode());
			if(sampleManager != null) {
				resultMap.put("errorInfo", "该样品已存在！");
				resultMap.put("success", false);
				return resultMap;
			}*/
			//新增时设置默认值
			projectManager.setProjectFlag(0);
			logService.save(new Log(Log.ADD_ACTION,"添加项目设置信息"+projectManager));
		}
		projectManagerService.save(projectManager);
		//修改相机状态为占用状态
		projectManagerService.updatePhotoStatus(projectManager.getPhotoCode(), "1");
		//回填项目id
		sampleManagerService.updateProjectId(Integer.parseInt(projectManager.getSampleId()), String.valueOf(projectManager.getId()));
		logService.save(new Log(Log.ADD_ACTION, "添加或者修改项目设置信息" + projectManagerService.findById(projectManager.getId())));
		resultMap.put("success", true);
		return resultMap;
	}
	
	/**
	* @param regex 正则表达式字符串
	* @param str 要匹配的字符串
	* @return 如果 str 符合 regex的正则表达式格式,返回true, 否则返回 false;
	*/
	private static boolean match(String str) {
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}
	
	/**
	 * 删除项目设置信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	@RequiresPermissions(value="任务管理")
	public Map<String,Object> delete(String ids)throws Exception{
		log.info("delete ids: " + ids);
		Map<String,Object> resultMap=new HashMap<>();
		String []idsStr=ids.split(",");
		for(int i=0;i<idsStr.length;i++){
			int id=Integer.parseInt(idsStr[i]);
			ProjectManager dto = projectManagerService.findById(id);
			if(1 == dto.getProjectFlag() || 2 == dto.getProjectFlag()) {
				resultMap.put("errorInfo", "相机处于开始或暂停状态不能删除！");
				resultMap.put("success", false);
				return resultMap;
			}
			projectManagerService.delete(id);
			//删除样品信息
			SampleManager resultSampleManager = sampleManagerService.findSample(String.valueOf(dto.getId()));
			if(resultSampleManager != null) {
				sampleManagerService.delete(resultSampleManager.getId());
			}
			//修改相机状态为闲置状态
			projectManagerService.updatePhotoStatus(dto.getPhotoCode(), "0");
			logService.save(new Log(Log.DELETE_ACTION,"删除项目设置信息" + projectManagerService.findById(id)));
		}
		resultMap.put("success", true);
		return resultMap;
	}
	
	/**
	 * 修改项目标识
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/updateFlag")
	@RequiresPermissions(value="任务管理")
	public Map<String,Object> updateFlag(ProjectManager projectManager)throws Exception{
		log.info("updateFlag id: " + projectManager.getId() + ",projectFlag: " + projectManager.getProjectFlag()+ "PhotoCode: ," + projectManager.getPhotoCode());
		Map<String,Object> resultMap=new HashMap<>();
		ProjectManager dto = projectManagerService.findById(projectManager.getId());
		if(1 == projectManager.getProjectFlag() || 2== projectManager.getProjectFlag()) {
			SampleManager sampleManager = sampleManagerService.findSample(String.valueOf(dto.getId()));
			if(sampleManager == null) {
				resultMap.put("errorInfo", "样品信息为空！");
				resultMap.put("success", false);
				return resultMap;
			}
		}
		projectManagerService.updateFlag(projectManager.getId(), projectManager.getProjectFlag());
		if(projectManager.getProjectFlag() == 3) {
			projectManagerService.updatePhotoStatus(dto.getPhotoCode(), "0");
			//删除对应的相机截图图片
			String webappRoot = this.getClass().getResource("/").getPath().replaceFirst("/", "").replaceAll("WEB-INF/classes", "");
			String fileName = webappRoot+"/detection/"+dto.getPhotoCode()+".png";
			File file = new File(fileName);
			// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
			if (file.exists() && file.isFile()) {
				file.delete();
			}
		}
		resultMap.put("success", true);
		return resultMap;
	}
	
	
	@RequestMapping("/findById")
	public ProjectManager findById(String id)throws Exception{
		log.info("findById ids: " + id);
		return projectManagerService.findById(Integer.parseInt(id));
	}
	
}
