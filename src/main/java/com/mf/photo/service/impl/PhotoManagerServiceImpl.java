package com.mf.photo.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.mf.entity.photo.PhotoManager;
import com.mf.photo.service.PhotoManagerService;
import com.mf.repository.PhotoManagerRepository;
import com.mf.util.Contants;
import com.mf.util.StringUtil;

@Service
public class PhotoManagerServiceImpl implements PhotoManagerService {

	private static final Logger log = LoggerFactory.getLogger(PhotoManagerServiceImpl.class);
	
	@Resource
	private PhotoManagerRepository photoManagerRepository;
	
	@Override
	public List<PhotoManager> list(PhotoManager photoManager, Integer page, Integer pageSize,Direction direction, String... properties) {
		Pageable pageable=new PageRequest(page-1,pageSize);
		Page<PhotoManager> pageCustomer=photoManagerRepository.findAll(new Specification<PhotoManager>() {
			@Override
			public Predicate toPredicate(Root<PhotoManager> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(photoManager!=null){
					if(StringUtil.isNotEmpty(photoManager.getPhotoCode())) {
						predicate.getExpressions().add(cb.like(root.get("photoCode"), "%"+photoManager.getPhotoCode()+"%"));
					}
					if(StringUtil.isNotEmpty(photoManager.getPhotoName())) {
						predicate.getExpressions().add(cb.like(root.get("photoName"), "%"+photoManager.getPhotoName()+"%"));
					}
					//起始日期,大于或等于
                    if (StringUtil.isNotEmpty(photoManager.getCreateTimeStart())) {
                    	predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("createTime").as(String.class), photoManager.getCreateTimeStart()));
                    }
					//结束日期,小于或等于
                    if (StringUtil.isNotEmpty(photoManager.getCreateTimeEnd())) {
                    	predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("createTime").as(String.class), photoManager.getCreateTimeEnd()));
					}
				}
				return predicate;
			}
		}, pageable);
		return pageCustomer.getContent();
	}

	@Override
	public Long getCount(PhotoManager photoManager) {
		Long count = photoManagerRepository.count(new Specification<PhotoManager>() {
			@Override
			public Predicate toPredicate(Root<PhotoManager> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(photoManager!=null){
					if(StringUtil.isNotEmpty(photoManager.getPhotoCode())) {
						predicate.getExpressions().add(cb.like(root.get("photoCode"), "%"+photoManager.getPhotoCode()+"%"));
					}
					if(StringUtil.isNotEmpty(photoManager.getPhotoName())) {
						predicate.getExpressions().add(cb.like(root.get("photoName"), "%"+photoManager.getPhotoName()+"%"));
					}
					//起始日期,大于或等于
                    if (StringUtil.isNotEmpty(photoManager.getCreateTimeStart())) {
                    	predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("createTime").as(String.class), photoManager.getCreateTimeStart()));
                    }
					//结束日期,小于或等于
                    if (StringUtil.isNotEmpty(photoManager.getCreateTimeEnd())) {
                    	predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("createTime").as(String.class), photoManager.getCreateTimeEnd()));
					}
				}
				return predicate;
			}
			
		});
		return count;
	}

	@Override
	public void save(PhotoManager photoManager) {
		photoManagerRepository.save(photoManager);
	}

	@Override
	public void delete(Integer id) {
		photoManagerRepository.delete(id);
	}

	@Override
	public PhotoManager findById(Integer id) {
		return photoManagerRepository.findOne(id);
	}

	@Override
	public List<PhotoManager> findByTitle() {
		return photoManagerRepository.findByTitle();
	}
	
	@Override
	public List<PhotoManager> findSuccessStatus() {
		return photoManagerRepository.findSuccessStatus();
	}
	
	@Override
	public PhotoManager findSuccessByPhotoCode(String photoCode) {
		return photoManagerRepository.findSuccessByPhotoCode(photoCode);
	}
	
	/**
	 * 只对MP4格式上传视频进行处理
	 * @param multipartRequest
	 * @param title
	 * @throws Exception
	 * @throws IOException
	 */
	@Override
	public String MP4(CommonsMultipartFile multipartRequest, String filename) throws FileNotFoundException, IOException, Exception {
		// MP4文件格式直接上传保存路径
		String webappRoot = this.getClass().getResource("/").getPath().replaceFirst("/", "").replaceAll("WEB-INF/classes", "");
		//String path = "src/main/webapp/index/video/";
		String path = webappRoot + Contants.PLAY_VIDEO_PATH;
		File TempFile = new File(path);
		if (!TempFile.exists()) {
			log.info("文件夹不存在，创建该文件夹。");
			if(!TempFile.mkdir()) {
				log.error("创建文件夹目录失败，" + path);
				throw new Exception("创建文件夹目录失败，" + path);
			}
		}
		// 获取上传时候的文件名
		log.info("saveCustomer 上传视频文件名: " + filename);
		// 需要生成源视频地址+重命名后的视频名+视频后缀
		String newVideopath = (path  + filename);
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
			throw new FileNotFoundException("saveCustomer FileNotFoundException:" + e.getMessage());
		} catch (IOException e) {
			log.error("saveCustomer IOException: ", e);
			throw new IOException(e);
		}
		log.info("写入本地磁盘/服务器结束！");
		return newVideopath;
	}
	
	@Override
	public void deleteFile(PhotoManager photoManager) throws Exception {
		// 删除上传文件
		String webappRoot = this.getClass().getResource("/").getPath().replaceFirst("/", "").replaceAll("WEB-INF/classes", "");
		String path = webappRoot + Contants.PLAY_VIDEO_PATH;
		//String path = "src/main/webapp/index/video/";
		File file = new File(path+photoManager.getFile());
		if (!file.isFile()) {
			throw new Exception("没有该视频文件: " + (path+photoManager.getFile()));
		}
		// 删除视频文件
		file.delete();
	}
	
}
