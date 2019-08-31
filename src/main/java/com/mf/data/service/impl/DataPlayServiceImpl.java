package com.mf.data.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mf.data.service.DataPlayService;
import com.mf.entity.data.ExceptionManager;
import com.mf.repository.DataPlayRepository;
import com.mf.service.LogService;
import com.mf.util.StringUtil;

@Service
public class DataPlayServiceImpl implements DataPlayService {

	private static final Logger log = LoggerFactory.getLogger(DataPlayServiceImpl.class);

	@Resource
	private DataPlayRepository dataPlayRepository;
	
	@Resource
	private LogService logService;
	
	@Autowired
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<ExceptionManager> list(ExceptionManager exceptionManager, Integer page, Integer pageSize, Direction direction, String... properties) {
		Pageable pageable = new PageRequest(page-1,pageSize);
		Page<ExceptionManager> pageCustomer = dataPlayRepository.findAll(new Specification<ExceptionManager>() {
			@Override
			public Predicate toPredicate(Root<ExceptionManager> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(exceptionManager!=null){
					if(StringUtil.isNotEmpty(exceptionManager.getProjectName())) {
						predicate.getExpressions().add(cb.equal(root.get("projectName"), exceptionManager.getProjectName()));
					}
					if(StringUtil.isNotEmpty(exceptionManager.getProjectId())) {
						predicate.getExpressions().add(cb.equal(root.get("projectId"), exceptionManager.getProjectId()));
					}
					if(StringUtil.isNotEmpty(exceptionManager.getSampleCode())) {
						predicate.getExpressions().add(cb.equal(root.get("sampleCode"), exceptionManager.getSampleCode()));
					}
					if(StringUtil.isNotEmpty(exceptionManager.getSampleName())) {
						predicate.getExpressions().add(cb.equal(root.get("sampleName"), exceptionManager.getSampleName()));
					}
					if(StringUtil.isNotEmpty(exceptionManager.getDetectionName())) {
						predicate.getExpressions().add(cb.like(root.get("detectionName"), "%"+exceptionManager.getDetectionName()+"%"));
					}
					if(StringUtil.isNotEmpty(exceptionManager.getPhotoName())) {
						predicate.getExpressions().add(cb.like(root.get("photoName"), "%"+exceptionManager.getPhotoName()+"%"));
					}
					//起始日期,大于或等于
                    if (StringUtil.isNotEmpty(exceptionManager.getCreateTimeStart())) {
                    	predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("createTime").as(String.class), exceptionManager.getCreateTimeStart()));
                    }
					//结束日期,小于或等于
                    if (StringUtil.isNotEmpty(exceptionManager.getCreateTimeEnd())) {
                    	predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("createTime").as(String.class), exceptionManager.getCreateTimeEnd()));
					}
				}
				return predicate;
			}
		}, pageable);
		
		return pageCustomer.getContent();
	}

	
	@Override
	public Long getCount(ExceptionManager exceptionManager) {
		Long count = dataPlayRepository.count(new Specification<ExceptionManager>() {
			@Override
			public Predicate toPredicate(Root<ExceptionManager> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(exceptionManager!=null){
					if(StringUtil.isNotEmpty(exceptionManager.getProjectName())) {
						predicate.getExpressions().add(cb.like(root.get("projectName"), "%"+exceptionManager.getProjectName()+"%"));
					}
					if(StringUtil.isNotEmpty(exceptionManager.getSampleName())) {
						predicate.getExpressions().add(cb.like(root.get("sampleName"), "%"+exceptionManager.getSampleName()+"%"));
					}
					if(StringUtil.isNotEmpty(exceptionManager.getDetectionName())) {
						predicate.getExpressions().add(cb.like(root.get("detectionName"), "%"+exceptionManager.getDetectionName()+"%"));
					}
					if(StringUtil.isNotEmpty(exceptionManager.getPhotoName())) {
						predicate.getExpressions().add(cb.like(root.get("photoName"), "%"+exceptionManager.getPhotoName()+"%"));
					}
					//起始日期,大于或等于
                    if (StringUtil.isNotEmpty(exceptionManager.getCreateTimeStart())) {
                    	predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("createTime").as(String.class), exceptionManager.getCreateTimeStart()));
                    }
					//结束日期,小于或等于
                    if (StringUtil.isNotEmpty(exceptionManager.getCreateTimeEnd())) {
                    	predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("createTime").as(String.class), exceptionManager.getCreateTimeEnd()));
					}
				}
				return predicate;
			}
		});
		return count;
	}
	
	/**
     * 读取某个目录下所有文件、文件夹
     * @param path
     * @return LinkedHashMap<String,String>
	 * @throws IOException 
     */
    public LinkedHashMap<String,String> getFiles(String path, String exceptionPath) throws IOException {
    	LinkedHashMap<String,String> files = new LinkedHashMap<String,String>();
        File file = new File(path);
        if(file.exists()) {
	        File[] tempList = file.listFiles();
	        for (int i = 0; i < tempList.length; i++) {
	            if (!tempList[i].isDirectory()) {
	            	files.put(tempList[i].getName(), tempList[i].getPath());
	            }
	            FileInputStream fis = null;
	            BufferedInputStream bis = null;
	            FileOutputStream fos = null;
	            BufferedOutputStream bos = null;
	            try {
					fis = new FileInputStream(tempList[i].getPath());
					bis = new BufferedInputStream(fis);
					fos = new FileOutputStream(exceptionPath + tempList[i].getName());
					bos = new BufferedOutputStream(fos);
					int len = 0;
					long startTime = System.currentTimeMillis();
					while ((len = bis.read()) != -1) {
						bos.write(len);
					}
					long endTime = System.currentTimeMillis();
					log.info(tempList[i].getName() + " 耗时：" + (endTime - startTime));
	            } catch (Exception e) {
	            	log.error(e.getMessage(), e);
	            } finally {
	            	if(bis != null) {
	            		bis.close();
	            	}
	            	if(bos != null) {
	            		bos.close();
	            	}
	            }
	        }
        }
        return files;
    }

	@Override
	@Transactional
	public void updateFlag(ExceptionManager detectionManager) {
		if(detectionManager.getId() != null) {
			String jpql = "update exceptions set flag =:flag where id =:id";
		    Query query = entityManager.createNativeQuery(jpql);
		    query.setParameter("flag", "1").setParameter("id",detectionManager.getId());
		    query.executeUpdate();
		}
	}

	@Override
	public void delete(Integer id) {
		ExceptionManager dto = dataPlayRepository.findOne(id);
		//删除对应的图片文件
		String path = dto.getFolderPicturePath()+"\\"+dto.getPicturePath();
		File file = new File(path);
		if (file.isFile()) {
			file.delete();
		}
		//删除对应生成的视频文件
		String videoPath = dto.getFolderVideoPath()+"\\"+dto.getVideoPath();
		File videoFile = new File(videoPath);
		if (videoFile.isFile()) {
			videoFile.delete();
		}
		dataPlayRepository.delete(id);
	}
    
}