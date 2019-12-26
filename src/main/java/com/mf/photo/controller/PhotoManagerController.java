package com.mf.photo.controller;

import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.mf.entity.Log;
import com.mf.entity.photo.PhotoManager;
import com.mf.photo.service.PhotoManagerService;
import com.mf.service.LogService;
import com.mf.util.StringUtil;

/**
 * @ClassName:PhotoManagerController  
 * @Description:相机设置controller
 * @author zzy
 * @date 2019年1月21日
 * @version V1.0
 */
@RestController
@RequestMapping("/photo/manager")
public class PhotoManagerController {

	private static final Logger log = LoggerFactory.getLogger(PhotoManagerController.class);
			
	@Resource
	private PhotoManagerService photoManagerService;
	
	@Resource
	private LogService logService;
	

	/**
	 * 下拉框模糊查询
	 * @param q
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/comboList")
	public List<PhotoManager> comboList()throws Exception{
		return photoManagerService.findByTitle();
	}
	
	@RequestMapping("/findSuccessStatus")
	public List<PhotoManager> findSuccessStatus()throws Exception{
		return photoManagerService.findSuccessStatus();
	}
	
	@RequestMapping("/findSuccessByPhotoCode")
	public PhotoManager findSuccessByPhotoCode(String photoCode) throws Exception{
		return photoManagerService.findSuccessByPhotoCode(photoCode);
	}
	
	/**
	 * 分页查询客户信息
	 * @param photoManager
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	@RequiresPermissions(value="相机管理")
	public Map<String,Object> list(PhotoManager photoManager,@RequestParam(value="page",required=false)Integer page,@RequestParam(value="rows",required=false)Integer rows)throws Exception{
		log.info("list photoManager: " + photoManager.toString());
		Map<String,Object> resultMap = new HashMap<>();
		List<PhotoManager> customerList = photoManagerService.list(photoManager, page, rows, Direction.ASC, "id");
		Long total = photoManagerService.getCount(photoManager);
		resultMap.put("rows", customerList);
		resultMap.put("total", total);
		return resultMap;
	}
	
	/**
	 * 添加或者修改相机设置信息
	 * @param photoManager
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/save", method = RequestMethod.POST)
	@RequiresPermissions(value="相机管理")
	public Map<String,Object> save(@RequestParam(value="fileStream", required=false) MultipartFile fileStream, PhotoManager photoManager)throws Exception{
		log.info("save photoManager: " + photoManager.toString() + ",fileStream:" + fileStream);
		Map<String,Object> resultMap = new HashMap<>();
		PhotoManager resultDto = null;
		if(StringUtil.isNotEmpty(photoManager.getPhotoCode())) {
			resultDto = photoManagerService.findSuccessByPhotoCode(photoManager.getPhotoCode());
		} else {
			resultMap.put("success", false);
			resultMap.put("errorInfo", "相机编号不能为空，请重新输入！");
			return resultMap;
		}
		if(photoManager.getId() == null) {
			if(resultDto != null) {
				resultMap.put("success", false);
				resultMap.put("errorInfo", "已存在相同相机编号，请重新输入！");
				return resultMap;
			}
			List<PhotoManager> list = photoManagerService.findByTitle();
			if(list.size() >= 4) {
				resultMap.put("success", false);
				resultMap.put("errorInfo", "添加相机数量不能超过四个！");
				return resultMap;
			}
			if(StringUtil.isEmpty(photoManager.getPhotoStatus())) {
				photoManager.setPhotoStatus("0");
			}
			photoManager.setCreateTime(new Timestamp(System.currentTimeMillis()));
			logService.save(new Log(Log.ADD_ACTION,"添加相机设置信息"+photoManager));
		}
		if("H264文件流".equals(photoManager.getCodeType()) && fileStream != null) {
			/*String file = "测试相机视频1";
			if(list.isEmpty() || list.size() == 0) {
				photoManager.setfile(file+map4);
			} else {
				String fileOld = list.get(list.size()-1).getfile();
				fileOld = fileOld.substring(0, fileOld.length()-4);
				log.info("save photo file: " + fileOld);
				Integer index = Integer.parseInt(fileOld.substring(fileOld.length()-1, fileOld.length()));
				index+=1;
				String fileNew = fileOld.substring(0, fileOld.length()-1)+index;
				photoManager.setfile(fileNew+map4);
			}*/
			try {
				CommonsMultipartFile cf = (CommonsMultipartFile)fileStream;
				if (cf != null && cf.getSize() != 0) {
					// 获取上传时候的文件名
					String filename = cf.getOriginalFilename();
					photoManager.setFile(filename);
					// 获取文件后缀名格式 MP4
					String filename_extension = filename.substring(filename.lastIndexOf(".") + 1);
					log.info("saveCustomer 视频的后缀名: " + filename_extension);
					//判断是否为MP4格式文件上传
					if("mp4".equals(filename_extension) || "MP4".equals(filename_extension)) {
						photoManager.setFilePath(photoManagerService.MP4(cf, filename));
					} else {
						resultMap.put("success", false);
						resultMap.put("errorInfo", "请选择  MP4 文件类型上传！");
						return resultMap;
					}
				}
			} catch (FileNotFoundException e1) {
				log.error("saveCustomer FileNotFoundException: " + e1.getMessage(), e1);
				resultMap.put("success", false);
				resultMap.put("errorInfo", "此文件已经上传，请选择其他文件！");
				return resultMap;
			} catch (Exception e) {
				log.error("saveCustomer Exception: " + e.getMessage(), e);
				throw new Exception(e);
			}
		} else if("H264文件流".equals(photoManager.getCodeType())){
			photoManager.setFile(resultDto.getFile());
		}
		photoManagerService.save(photoManager);
		logService.save(new Log(Log.ADD_ACTION, "添加或者修改相机设置信息" + photoManagerService.findById(photoManager.getId())));
		resultMap.put("success", true);
		return resultMap;
	}
	
	/**
	 * 删除相机设置信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	@RequiresPermissions(value="相机管理")
	public Map<String,Object> delete(String ids)throws Exception{
		log.info("delete ids: " + ids);
		Map<String,Object> resultMap=new HashMap<>();
		String []idsStr=ids.split(",");
		for(int i=0;i<idsStr.length;i++){
			int id=Integer.parseInt(idsStr[i]);
			PhotoManager photoManager = photoManagerService.findById(id);
			if(StringUtil.isEmpty(photoManager.getPhotoStatus()) || "0".equals(photoManager.getPhotoStatus())) {
				if("H264文件流".equals(photoManager.getCodeType())) {
					photoManagerService.deleteFile(photoManager);
					photoManagerService.delete(id);
				} else {
					photoManagerService.delete(id);
				}
				logService.save(new Log(Log.DELETE_ACTION,"删除相机设置信息" + photoManagerService.findById(id)));
			} else {
				resultMap.put("success", false);
				resultMap.put("errorInfo", "相机使用中，请先结束任务再操作！");
				return resultMap;
			}
		}
		resultMap.put("success", true);
		return resultMap;
	}
	
	@RequestMapping("/findByPhotoCode")
	public Map<String,Object> findByPhotoCode(String photoCode)throws Exception{
		log.info("list photoCode: " + photoCode);
		Map<String,Object> resultMap = new HashMap<>();
		PhotoManager photoManager = photoManagerService.findSuccessByPhotoCode(photoCode);
		resultMap.put("photoManager", photoManager);
		resultMap.put("success", true);
		return resultMap;
	}
	
}