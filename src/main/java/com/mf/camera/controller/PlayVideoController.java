package com.mf.camera.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import com.mf.camera.service.PlayVideoService;
import com.mf.entity.camera.PlayVideo;

/**
 * @ClassName:PlayVideoController
 * @Description:上传视频controller哈哈哈哈
 * @author zzy
 * @date 2018年12月10日
 * @version V1.0
 */
@RestController
@RequestMapping("/admin/playvideo")
public class PlayVideoController {

	private static final Logger log = LoggerFactory.getLogger(PlayVideoController.class);
	
	@Resource
	private PlayVideoService playVideoService;
	
	/**
	 * 下拉框模糊查询bbbbbbbbb
	 * @param q
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/comboList")
	@RequiresPermissions(value={"上传视频"},logical=Logical.OR)
	public List<PlayVideo> comboList(String q)throws Exception{
		if(q==null){
			q="";
		}
		return playVideoService.findByTitle("%"+q+"%");
	}
	
	/**
	 * 分页查询上传视频信息
	 * @param test
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	@RequiresPermissions(value="上传视频")
	public Map<String,Object> list(PlayVideo playVideo,@RequestParam(value="page",required=false)Integer page,@RequestParam(value="rows",required=false)Integer rows)throws Exception{
		log.info("list playVideo: " + playVideo.toString());
		Map<String,Object> resultMap = new HashMap<>();
		List<PlayVideo> customerList = playVideoService.list(playVideo, page, rows, Direction.ASC, "id");
		Long total = playVideoService.getCount(playVideo);
		resultMap.put("rows", customerList);
		resultMap.put("total", total);
		return resultMap;
	}
	
	/**
	 * 添加上传视频信息
	 * @param test
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/upload", headers = "content-type=multipart/*", method = RequestMethod.POST)
	@RequiresPermissions(value="上传视频")
	public Map<String,Object> save(@RequestParam("file") CommonsMultipartFile file, @RequestParam("title") String title)throws Exception{
		Map<String,Object> resultMap = new HashMap<>();
		log.info("save 上传视频: " + file + " title: " + title);
		try {
			playVideoService.save(file, title);
			resultMap.put("success", true);
		} catch(Exception e) {
			log.error("save:" + e.getMessage(), e);
			resultMap.put("success", false);
		}
		return resultMap;
	}
	
	/**
	 * 删除上传视频记录信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	@RequiresPermissions(value="上传视频")
	public Map<String, Object> delete(@RequestParam("id") Integer id)throws Exception {
		log.info("delete id: " + id);
		Map<String,Object> resultMap = new HashMap<>();
		try {
			playVideoService.delete(id);
			resultMap.put("success", true);
		} catch(Exception e) {
			log.error("delete:" + e.getMessage(), e);
			resultMap.put("success", false);
		}
		return resultMap;
	}
	
}
