package com.mf.camera.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.mf.camera.service.PlayVideoService;
import com.mf.entity.Log;
import com.mf.entity.camera.PlayVideo;
import com.mf.repository.PlayVideoRepository;
import com.mf.service.LogService;
import com.mf.service.UserService;
import com.mf.util.Contants;
import com.mf.util.ConverVideo;
import com.mf.util.StringUtil;

/**
 * @ClassName:CustomerServiceImpl
 * @Description:上传视频实现类impl
 * @author zzy
 * @date 2018年12月10日
 * 
 * @version V1.0
 */
@Service
public class PlayVideoServiceImpl implements PlayVideoService {

	private static final Logger log = LoggerFactory.getLogger(PlayVideoServiceImpl.class);

	@Resource
	private ConverVideo converVideo;
	
	@Resource
	private PlayVideoRepository playVideoRepository;
	
	@Resource
	private LogService logService;
	
	@Resource
	private UserService userService;
	
	@Override
	public void save(CommonsMultipartFile multipartRequest, String title) throws Exception {
		try {
			if (multipartRequest.getSize() != 0) {
				// 获取上传时候的文件名
				String filename = multipartRequest.getOriginalFilename();
				
				// 获取文件后缀名格式 MP4
				String filename_extension = filename.substring(filename.lastIndexOf(".") + 1);
				log.info("saveCustomer 视频的后缀名: " + filename_extension);
				//判断是否为MP4格式文件上传
				if("mp4".equals(filename_extension)) {
					MP4(multipartRequest, title);
				} else {
					beginConverMP4(multipartRequest, title);
				}
			}
		} catch (Exception e) {
			log.error("saveCustomer Exception: " + e.getMessage(), e);
			throw new Exception(e);
		}
	}

	/**
	 * 只对MP4格式上传视频进行处理
	 * @param multipartRequest
	 * @param title
	 * @throws Exception
	 * @throws IOException
	 */
	private void MP4(CommonsMultipartFile multipartRequest, String title) throws Exception, IOException {
		// MP4文件格式直接上传保存路径
		String path = Contants.TARGET_FOLDER;
		File TempFile = new File(path);
		if (!TempFile.exists()) {
			log.info("文件夹不存在，创建该文件夹。");
			if(!TempFile.mkdir()) {
				log.error("创建文件夹目录失败，" + path);
				throw new Exception("创建文件夹目录失败，" + path);
			}
		}
		// 获取上传时候的文件名
		String filename = multipartRequest.getOriginalFilename();
		log.info("saveCustomer 上传视频文件名: " + filename);
		
		// 时间戳做新的文件名，避免中文乱码-重新生成filename
		String newFilename = Long.toString(new Date().getTime()) + ".mp4";

		// 需要生成源视频地址+重命名后的视频名+视频后缀
		String newVideopath = (path  + newFilename);
		log.info("saveCustomer 新视频路径为:" + newVideopath);

		// 上传到本地磁盘/服务器
		try {
			log.info("写入本地磁盘/服务器");
			InputStream is = multipartRequest.getInputStream();
			OutputStream os = new FileOutputStream(new File(newVideopath));
			int len = 0;
			byte[] buffer = new byte[2048];
			while ((len = is.read(buffer)) != -1) {
				os.write(buffer, 0, len);
			}
			os.close();
			os.flush();
			is.close();
		} catch (FileNotFoundException e) {
			log.error("saveCustomer FileNotFoundException: ", e);
			throw new Exception(e);
		} catch (IOException e) {
			log.error("saveCustomer IOException: ", e);
			throw new IOException(e);
		}
		log.info("写入本地磁盘/服务器结束！");
		//保存到数据库t_play_video
		PlayVideo playVideo = new PlayVideo();
		playVideo.setTitle(title);
		playVideo.setSuffix(filename);
		playVideo.setNewSuffix(newFilename);
		playVideo.setSrc(newVideopath);
		playVideo.setUserName(userService.findByUserName((String) SecurityUtils.getSubject().getPrincipal()).getUserName()); // 设置操作用户
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());  
		playVideo.setCreateTime(timestamp);
		log.info("playVideo : " + playVideo.toString());
		playVideoRepository.save(playVideo);
		logService.save(new Log(Log.ADD_ACTION, "上传视频信息：" + filename));
		log.info("上传视频成功！");
	}
	
	/**
	 * 只对非MP4格式的视频进行处理
	 * @param multipartRequest
	 * @param title
	 * @throws Exception
	 * @throws IOException
	 */
	private void beginConverMP4(CommonsMultipartFile multipartRequest, String title) throws Exception, IOException {
		// 上传的多格式的视频文件-作为临时路径保存，转码以后删除-路径不能写//
		String path = Contants.VIDEO_FOLDER_TEMP;
		File TempFile = new File(path);
		if (!TempFile.exists()) {
			log.info("文件夹不存在，创建该文件夹。");
			TempFile.mkdir();
		}
		// 获取上传时候的文件名
		String filename = multipartRequest.getOriginalFilename();
		
		//获取文件的文件名称，用于存放数据库
		String tableFilename = filename.substring(0, filename.lastIndexOf("."));
		log.info("读取到的文件名：" + filename + " 上传文件原文件名：" + tableFilename);
		
		// 获取文件后缀名格式 MP4
		String filename_extension = filename.substring(filename.lastIndexOf(".") + 1);
		log.info("saveCustomer 视频的后缀名: " + filename_extension);
		
		// 时间戳做新的文件名，避免中文乱码-重新生成filename
		long filename1 = new Date().getTime();
		filename = Long.toString(filename1) + "." + filename_extension;
		// 去掉后缀的文件名
		String filename2 = filename.substring(0, filename.lastIndexOf("."));
		log.info("saveCustomer 新视频名为当前时间: " + filename2);

		// 需要生成源视频地址+重命名后的视频名+视频后缀
		String yuanPATH = (path + filename);
		log.info("saveCustomer 源视频路径为:" + yuanPATH);

		// 上传到本地磁盘/服务器
		try {
			log.info("写入本地磁盘/服务器");
			InputStream is = multipartRequest.getInputStream();
			OutputStream os = new FileOutputStream(new File(path, filename));
			int len = 0;
			byte[] buffer = new byte[2048];
			while ((len = is.read(buffer)) != -1) {
				os.write(buffer, 0, len);
			}
			os.close();
			os.flush();
			is.close();
		} catch (FileNotFoundException e) {
			log.error("saveCustomer FileNotFoundException: ", e);
			throw new Exception(e);
		} catch (IOException e) {
			log.error("saveCustomer IOException: ", e);
			throw new IOException(e);
		}

		log.info("========上传完成，开始调用转码工具类=======");
		boolean converBoolean = false;
		// 调用转码机制flv mp4 f4v m3u8 webm ogg放行直接播放，
		// asx，asf，mpg，wmv，3gp，mov，avi，wmv9，rm，rmvb等进行其他转码为mp4
		if (filename_extension.equals("avi")
				|| filename_extension.equals("rm")
				|| filename_extension.equals("rmvb")
				|| filename_extension.equals("wmv")
				|| filename_extension.equals("3gp")
				|| filename_extension.equals("mov")
				|| filename_extension.equals("flv")
				|| filename_extension.equals("ogg")) {
			converBoolean = converVideo.beginConver(yuanPATH); // 调用转码
			log.info("converBoolean " + converBoolean + "=================转码过程彻底结束=====================");
		}
		if (!"mp4".equals(filename_extension) && converBoolean != true) {
			throw new Exception(filename_extension + "  转码失败！" + yuanPATH);
		}
		// 获取转码后的mp4文件名
		filename2 = filename2 + ".mp4";
		String NewVideopath = Contants.TARGET_FOLDER + filename2;
		log.info("新视频的url:" + NewVideopath);
		
		// 删除临时文件
		File file2 = new File(path);
		if (!file2.exists()) {
			throw new Exception("没有该文件: " + path);
		}
		if (!file2.isDirectory()) {
			throw new Exception("没有该文件夹: " + path);
		}
		String[] tempList = file2.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile() || temp.isDirectory()) {
				temp.delete(); // 删除文件夹里面的文件
			}
		}
		log.info("所有的临时视频文件删除成功");
		
		//保存到数据库t_play_video
		PlayVideo playVideo = new PlayVideo();
		playVideo.setTitle(title);
		playVideo.setSuffix(tableFilename);
		playVideo.setSrc(NewVideopath);
		playVideo.setUserName(userService.findByUserName((String) SecurityUtils.getSubject().getPrincipal()).getUserName()); // 设置操作用户
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());  
		playVideo.setCreateTime(timestamp);
		log.info("playVideo : " + playVideo.toString());
		playVideoRepository.save(playVideo);
		logService.save(new Log(Log.ADD_ACTION, "上传视频信息：" + filename2));
	}
	
	@Override
	public List<PlayVideo> list(PlayVideo playVideo, Integer page, Integer pageSize, Direction direction, String... properties) {
		Pageable pageable = new PageRequest(page-1,pageSize);
		Page<PlayVideo> pageCustomer= playVideoRepository.findAll(new Specification<PlayVideo>() {
			@Override
			public Predicate toPredicate(Root<PlayVideo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(playVideo!=null){
					if(StringUtil.isNotEmpty(playVideo.getTitle())) {
						predicate.getExpressions().add(cb.like(root.get("title"), "%"+playVideo.getTitle()+"%"));
					}
				}
				return predicate;
			}
		}, pageable);
		return pageCustomer.getContent();
	}

	@Override
	public Long getCount(PlayVideo playVideo) {
		Long count = playVideoRepository.count(new Specification<PlayVideo>() {
			@Override
			public Predicate toPredicate(Root<PlayVideo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(playVideo!=null){
					if(StringUtil.isNotEmpty(playVideo.getTitle())){
						predicate.getExpressions().add(cb.like(root.get("title"), "%" + playVideo.getTitle()+"%"));
					}
				}
				return predicate;
			}
		});
		return count;
	}
	
	@Override
	public List<PlayVideo> findByTitle(String title) {
		return playVideoRepository.findByTitle(title);
	}

	@Override
	public void delete(Integer id) throws Exception {
		try {
			if(id == null) {
				throw new Exception("传入删除id不能为空！");
			}
			PlayVideo playVideo = playVideoRepository.findOne(id);
			log.info("delete id: " + id + " Src:" + playVideo.getSrc());
			// 删除上传文件
			File file = new File(playVideo.getSrc());
			if (!file.isFile()) {
				throw new Exception("没有该视频文件: " + (playVideo.getSrc()));
			}
			// 删除视频文件
			if(file.delete()) {
				//删除数据库对应数据
				playVideoRepository.delete(id);
				logService.save(new Log(Log.DELETE_ACTION, "删除上传视频标题: " + playVideo.getTitle() + " 视频上传地址: " +  playVideo.getSrc()));	
			}
		} catch(Exception e) {
			log.error("PlayVideoService delete Exception:" + e.getMessage(), e);
			throw new Exception(e);
		}
	}

	@Override
	public PlayVideo findById(Integer id) {
		return playVideoRepository.findOne(id);
	}

	/**
	 * 播放视频信息
	 */
	@Override
	public String operationPlay(Integer id) throws Exception {
		PlayVideo playVideo = playVideoRepository.findOne(id);
		
		return null;
	}
	
}
