package com.mf.detection.controller;

import java.util.ArrayList;
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

import com.mf.detection.service.DetectionManagerService;
import com.mf.entity.Log;
import com.mf.entity.detection.DetectionManager;
import com.mf.entity.photo.PhotoManager;
import com.mf.entity.project.ProjectManager;
import com.mf.project.service.ProjectManagerService;
import com.mf.service.LogService;
import com.mf.util.HttpClientUtil;
import com.mf.util.StringUtil;

/**
 * @ClassName:DetectionManagerController  
 * @Description:检测区域管理controller
 * @author zzy
 * @date 2019年2月18日
 * @version V1.0
 */
@RestController
@RequestMapping("/detection/manager")
public class DetectionManagerController {

	private static final Logger log = LoggerFactory.getLogger(DetectionManagerController.class);
			
	@Resource
	private DetectionManagerService detectionManagerService;
	
	@Resource
	private ProjectManagerService projectManagerService;
	
	@Resource
	private LogService logService;
	
	/**
	 * 分页查询检测区域信息
	 * @param detectionManager
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public Map<String,Object> list(DetectionManager detectionManager,@RequestParam(value="page",required=false)Integer page,@RequestParam(value="rows",required=false)Integer rows)throws Exception{
		log.info("list detectionManager: " + detectionManager.toString());
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<DetectionManager> customerList = detectionManagerService.list(detectionManager, page, rows, Direction.ASC, "id");
		Long total = detectionManagerService.getCount(detectionManager);
		resultMap.put("rows", customerList);
		resultMap.put("total", total);
		return resultMap;
	}
	
	/**
	 * 添加或者修改检测区域信息
	 * @param detectionManager
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	public Map<String,Object> save(DetectionManager detectionManager)throws Exception{
		log.info("save detectionManager: " + detectionManager.toString());
		Map<String,Object> resultMap=new HashMap<String,Object>();
		if(detectionManager.getId() != null){
			logService.save(new Log(Log.UPDATE_ACTION,"更新检测区域信息"+detectionManager));
		}else{
			logService.save(new Log(Log.ADD_ACTION,"添加检测区域信息"+detectionManager));
		}
		detectionManagerService.save(detectionManager);
		logService.save(new Log(Log.ADD_ACTION, "添加或者修改检测区域信息" + detectionManagerService.findById(detectionManager.getId())));
		resultMap.put("success", true);
		return resultMap;
	}
	
	/**
	 * 删除检测区域信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	public Map<String,Object> delete(String ids)throws Exception{
		log.info("delete ids: " + ids);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		if(StringUtil.isNotEmpty(ids)) {
			String [] idsStr = ids.split(",");
			for(int i=0;i<idsStr.length;i++){
				int id=Integer.parseInt(idsStr[i]);
				detectionManagerService.delete(id);
				logService.save(new Log(Log.DELETE_ACTION,"删除检测区域信息" + detectionManagerService.findById(id)));
			}	
		}
		resultMap.put("success", true);
		return resultMap;
	}
	
	@RequestMapping("/saveImg")
	public Map<String,Object> saveImg(DetectionManager detectionManager)throws Exception{
		log.info("saveImg detectionManager: " + detectionManager.toString());
		Map<String,Object> resultMap = new HashMap<String,Object>();
		if(detectionManager.getId() != null){
			DetectionManager dto = findById(detectionManager.getId()+"");
			if(dto != null) {
				//detectionManager.setDetectionName(dto.getDetectionName());
				logService.save(new Log(Log.UPDATE_ACTION,"更新检测区域信息"+detectionManager));
			} else {
				resultMap.put("success", false);
				resultMap.put("errorInfo", "不存在检测区域信息");
				return resultMap;
			}
		}else{
			/*if(!"新增检测目标".equals(detectionManager.getDetectionName())) {
				resultMap.put("success", false);
				resultMap.put("errorInfo", "请先选择新增检测目标");
				return resultMap;
			}*/
			logService.save(new Log(Log.ADD_ACTION,"添加检测区域信息"+detectionManager));
		}
		/*List<DetectionManager> list = findDetectionByProjectId(detectionManager.getProjectId()+"");
		if(list.isEmpty() || list.size() == 0) {
			detectionManager.setDetectionName("新增检测目标1");
		} else {
			if(detectionManager.getId() == null) {
				String detectionName = list.get(list.size()-1).getDetectionName();
				Integer index = Integer.parseInt(detectionName.substring(detectionName.length()-1, detectionName.length()));
				index+=1;
				String detectionNameNew = detectionName.substring(0, detectionName.length()-1)+index;
				detectionManager.setDetectionName(detectionNameNew);
			}
		}*/
		detectionManagerService.saveImg(detectionManager);
		resultMap.put("detectionManager", detectionManager);
		resultMap.put("success", true);
		return resultMap;
	}
	
	@RequestMapping("/find")
	public DetectionManager findById(String id)throws Exception{
		log.info("findById id: " + id);
		return detectionManagerService.findById(new Integer(id));
	}
	
	@RequestMapping("/comboList")
	public List<DetectionManager> findComboList(String projectId)throws Exception{
		log.info("findComboList projectId: " + projectId);
		List<DetectionManager> resultCombo = new ArrayList<DetectionManager>();
		List<DetectionManager> list = detectionManagerService.findDetectionByProjectId(new Integer(projectId));
		if((list.isEmpty() || list.size() == 0)) {
			DetectionManager dto = new DetectionManager();
			dto.setDetectionName("新增检测目标");
			resultCombo.add(dto);
			return resultCombo;
		} else {
			if(list.size()<4) {
				DetectionManager dto = new DetectionManager();
				dto.setDetectionName("新增检测目标");
				resultCombo.add(dto);
			}
			for(int i = 0; i<list.size(); i++) {
				resultCombo.add(list.get(i));
			}
		}
		return resultCombo;
	}
	
	@RequestMapping("/findInitData")
	public Map<String, String> findInitData(String projectId)throws Exception{
		log.info("findDetectionByProjectId projectId: " + projectId);
		ProjectManager projectManager= projectManagerService.findById(Integer.parseInt(projectId));
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("photoCode", projectManager.getPhotoCode());
		resultMap.put("photoName", projectManager.getPhotoName());
		resultMap.put("projectName", projectManager.getProjectName());
		List<DetectionManager> list = detectionManagerService.findDetectionManager();
		resultMap.put("detectionCode", getNewEquipmentNo(list.size()));
		return resultMap;
	}

	@RequestMapping("/capturePic")
	public Map<String, Object> capturePic(PhotoManager photoManager) throws Exception {
		log.info("capturePic photoManager: " + photoManager.toString());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("photo", photoManager.getPhotoCode());
			params.put("path", photoManager.getPhotoCode());//文件名称，使用photo_code保证唯一性
			//params.put("timer", DateUtil.currentDateIDString());
			String result = HttpClientUtil.sendGet("http://192.168.56.102:5000/photo/", params, 3000);
			log.info("capturePic HttpClientUtil result: " + result);
			if(StringUtil.isNotEmpty(result)) {
				resultMap.put("success", true);
			} else {
				resultMap.put("errorInfo", "获取图像失败，请稍后重试！");
				resultMap.put("success", false);
			}
        } catch (Exception e) {
			log.error(e.getMessage(), e);
			resultMap.put("errorInfo", "获取图像失败，请稍后重试！");
			resultMap.put("success", false);
		}
		return resultMap;
	}
	
    public static String getNewEquipmentNo(int equipmentNo){
        String newEquipmentNo = "0000";
        equipmentNo = equipmentNo+1;
        newEquipmentNo = String.format("A" + "%05d", equipmentNo);
        return newEquipmentNo;
    }

    @RequestMapping("/findDetectionByProjectId")
	public List<DetectionManager> findDetectionByProjectId(String projectId)throws Exception{
		log.info("findDetectionByProjectId projectId: " + projectId);
		return detectionManagerService.findDetectionByProjectId(new Integer(projectId));
	}
    
}