package com.mf.project.service.impl;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mf.entity.project.ProjectManager;
import com.mf.project.service.ProjectManagerService;
import com.mf.repository.ProjectManagerRepository;
import com.mf.util.StringUtil;

@Service
public class ProjectManagerServiceImpl implements ProjectManagerService {

	private static final Logger log = LoggerFactory.getLogger(ProjectManagerServiceImpl.class);
	
	@Autowired
	@PersistenceContext
	private EntityManager entityManager;

	@Resource
	private ProjectManagerRepository projectManagerRepository;
	
	@Override
	public List<ProjectManager> list(ProjectManager projectManager, Integer page, Integer pageSize,Direction direction, String... properties) {
		
        StringBuilder selectSql = new StringBuilder();
        selectSql.append("SELECT a.id, a.project_code, a.project_name, a.photo_code, a.video_source, a.video_set, a.video_time, a.exception_event_time, a.project_url"
        		+ ", a.video_file_url, a.sample_code as sampleCode, a.sample_name as sampleName"
        		+ ", a.photo_name as photoName, a.project_flag, a.create_time, a.update_time FROM project_manager a where 1=1 ");
        
        List<String> listParams = new ArrayList<String>();
        StringBuilder whereSql = new StringBuilder();
		if (StringUtil.isNotEmpty(projectManager.getProjectCode())) {
			whereSql.append(" and a.project_code like ? ");
			listParams.add(projectManager.getProjectCode());
		}
		if (StringUtil.isNotEmpty(projectManager.getProjectName())) {
			whereSql.append(" and a.project_name like ? ");
			listParams.add(projectManager.getProjectName());
		}
		if (StringUtil.isNotEmpty(projectManager.getPhotoCode())) {
			whereSql.append(" and a.photo_code like ? ");
			listParams.add(projectManager.getPhotoCode());
		}
		whereSql.append(" order by a.update_time desc ");
        String querySql = new StringBuilder().append(selectSql).append(whereSql).toString();
        log.info("ProjectManagerServiceImpl querySql:"+ querySql);
        
		Query query = entityManager.createNativeQuery(querySql);
		for (int i = 1; i<= listParams.size(); i++) {
			query.setParameter(i, "%"+listParams.get(i-1)+"%");
		}
        int startIndex = pageSize*(page-1);
        query.setFirstResult(startIndex);
        query.setMaxResults(pageSize);
        List<Object[]> result = (List<Object[]>)query.getResultList();
        List<ProjectManager> returnList = new ArrayList<ProjectManager>();
        for(Object[] obj : result) {
        	ProjectManager pm = new ProjectManager();
        	pm.setId((Integer)obj[0]);
        	pm.setProjectCode((String)obj[1]);
        	pm.setProjectName((String)obj[2]);
        	pm.setPhotoCode((String)obj[3]);
        	pm.setVideoSource((String)obj[4]);
        	String videoSet = (String)obj[5];
        	pm.setVideoSet(videoSet);
        	if("1".equals(videoSet)) {
        		pm.setVideoSetName("不录像");
        	} else {
        		pm.setVideoSetName("全天录像");
        	}
        	pm.setVideoTime((String)obj[6]);
        	pm.setExceptionEventTime((String)obj[7]);
        	pm.setProjectUrl((String)obj[8]);
        	pm.setVideoFileUrl((String)obj[9]);
        	pm.setSampleCode((String)obj[10]);
        	pm.setSampleName((String)obj[11]);
        	pm.setPhotoName((String)obj[12]);
        	Integer projectFlag = ((Integer)obj[13]);
        	pm.setProjectFlag(projectFlag);
        	if(projectFlag == 0) {
        		pm.setProjectFlagName("未启用");
        	} else if(projectFlag == 1) {
        		pm.setProjectFlagName("进行中");
        	} else if (projectFlag == 2) {
        		pm.setProjectFlagName("暂停");
        	} else {
        		pm.setProjectFlagName("结束");
        	}
        	pm.setCreateTime((Timestamp)obj[14]);
        	pm.setUpdateTime((Timestamp)obj[15]);
        	returnList.add(pm);
        }
		return returnList;
	}

	@Override
	public Long getCount(ProjectManager projectManager) {
		
		StringBuilder countSelectSql = new StringBuilder();
		countSelectSql.append("SELECT count(1) FROM project_manager a where 1=1 ");
		
        List<String> listParams = new ArrayList<String>();
        StringBuilder whereSql = new StringBuilder();
		if (StringUtil.isNotEmpty(projectManager.getProjectCode())) {
			whereSql.append(" and a.project_code like ? ");
			listParams.add(projectManager.getProjectCode());
		}
		if (StringUtil.isNotEmpty(projectManager.getProjectName())) {
			whereSql.append(" and a.project_name like ? ");
			listParams.add(projectManager.getProjectName());
		}
		if (StringUtil.isNotEmpty(projectManager.getPhotoCode())) {
			whereSql.append(" and a.photo_code like ? ");
			listParams.add(projectManager.getPhotoCode());
		}
		String countSql = new StringBuilder().append(countSelectSql).append(whereSql).toString();
		
		Query countQuery = entityManager.createNativeQuery(countSql);
		for (int i= 1; i<= listParams.size(); i++) {
			countQuery.setParameter(i, "%"+listParams.get(i-1)+"%");
		}
		BigInteger totalCount = (BigInteger) countQuery.getSingleResult();
		return totalCount.longValue();
		
	}

	@Override
	@Transactional
	public void save(ProjectManager projectManager) throws Exception {
		/*String projectUrl = projectManager.getProjectUrl();
		File file = new File(projectUrl);
		if (!file.exists()) {
			if(!file.mkdir()) {
				log.error("创建文件夹目录失败，" + projectUrl);
				throw new Exception("创建文件夹目录失败，" + projectUrl);
			} else {
				// trueVideo    exceptionVideo    exceptionPic
				String trueVideoUrl = projectUrl + "/trueVideo";
				File trueVideoFile = new File(trueVideoUrl);
				if (!trueVideoFile.exists()) {
					if(!trueVideoFile.mkdir()) {
						log.error("创建/trueVideo 文件夹目录失败，" + trueVideoUrl);
						throw new Exception("创建/trueVideo 文件夹目录失败，" + trueVideoUrl);
					}
				}
				String exceptionVideoUrl = projectUrl + "/exceptionVideo";
				File exceptionVideoFile = new File(exceptionVideoUrl);
				if (!exceptionVideoFile.exists()) {
					if(!exceptionVideoFile.mkdir()) {
						log.error("创建/exceptionVideo 文件夹目 录失败，" + exceptionVideoUrl);
						throw new Exception("创建/exceptionVideo 文件夹目录失败，" + exceptionVideoUrl);
					}
				}
				String exceptionPicUrl = projectUrl + "/exceptionPic";
				File exceptionPicFile = new File(exceptionPicUrl);
				if (!exceptionPicFile.exists()) {
					if(!exceptionPicFile.mkdir()) {
						log.error("创建/exceptionPic 文件夹目录失败，" + exceptionPicUrl);
						throw new Exception("创建/exceptionPic 文件夹目录失败，" + exceptionPicUrl);
					}
				}
			}
		}*/
		projectManagerRepository.save(projectManager);
	}

	@Override
	public void delete(Integer id) {
		projectManagerRepository.delete(id);
	}

	@Override
	public ProjectManager findById(Integer id) {
		return projectManagerRepository.findOne(id);
	}

	@Override
	public List<ProjectManager> findProjectName() {
		return projectManagerRepository.findProjectName();
	}

	@Override
	@Transactional
	public void updateFlag(Integer id, Integer projectFlag) {
		log.info("updateFlag id:" + id +",projectFlag:" + projectFlag);
		String jpql = "update project_manager set project_flag =:projectFlag where id =:id";
	    Query query = entityManager.createNativeQuery(jpql);
	    query.setParameter("projectFlag", projectFlag).setParameter("id",id);
	    query.executeUpdate();
	}
	
	@Override
	@Transactional
	public void updatePhotoStatus(String photoCode, String photoStatus) {
		log.info("updatePhotoStatus photoCode:" + photoCode +",photoStatus:" + photoStatus);
		String jpql = "update photo_manager set photo_status =:photoStatus where photo_code =:photoCode";
	    Query query = entityManager.createNativeQuery(jpql);
	    query.setParameter("photoStatus", photoStatus).setParameter("photoCode", photoCode);
	    query.executeUpdate();
	}
	
}
