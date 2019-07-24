package com.mf.detection.service.impl;

import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mf.detection.service.DetectionManagerService;
import com.mf.entity.detection.DetectionManager;
import com.mf.repository.DetectionManagerRepository;
import com.mf.util.StringUtil;

@Service
public class DetectionManagerServiceImpl implements DetectionManagerService {

	@Resource
	private DetectionManagerRepository detectionManagerRepository;
	
	@Override
	public List<DetectionManager> list(DetectionManager detectionManager, Integer page, Integer pageSize,Direction direction, String... properties) {
		Pageable pageable=new PageRequest(page-1,pageSize);
		Page<DetectionManager> pageCustomer=detectionManagerRepository.findAll(new Specification<DetectionManager>() {
			@Override
			public Predicate toPredicate(Root<DetectionManager> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(detectionManager!=null){
					if(StringUtil.isNotEmpty(detectionManager.getDetectionCode())) {
						predicate.getExpressions().add(cb.like(root.get("detectionCode"), "%"+detectionManager.getDetectionCode()+"%"));
					}
					if(StringUtil.isNotEmpty(detectionManager.getDetectionName())) {
						predicate.getExpressions().add(cb.like(root.get("detectionName"), "%"+detectionManager.getDetectionName()+"%"));
					}
					//起始日期,大于或等于
                    if (StringUtil.isNotEmpty(detectionManager.getCreateTimeStart())) {
                    	predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("createTime").as(String.class), detectionManager.getCreateTimeStart()));
                    }
					//结束日期,小于或等于
                    if (StringUtil.isNotEmpty(detectionManager.getCreateTimeEnd())) {
                    	predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("createTime").as(String.class), detectionManager.getCreateTimeEnd()));
					}
				}
				return predicate;
			}
		}, pageable);
		return pageCustomer.getContent();
	}

	@Override
	public Long getCount(DetectionManager detectionManager) {
		Long count = detectionManagerRepository.count(new Specification<DetectionManager>() {
			@Override
			public Predicate toPredicate(Root<DetectionManager> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(detectionManager!=null){
					if(StringUtil.isNotEmpty(detectionManager.getDetectionCode())) {
						predicate.getExpressions().add(cb.like(root.get("detectionCode"), "%"+detectionManager.getDetectionCode()+"%"));
					}
					if(StringUtil.isNotEmpty(detectionManager.getDetectionName())) {
						predicate.getExpressions().add(cb.like(root.get("detectionName"), "%"+detectionManager.getDetectionName()+"%"));
					}
					//起始日期,大于或等于
                    if (StringUtil.isNotEmpty(detectionManager.getCreateTimeStart())) {
                    	predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("createTime").as(String.class), detectionManager.getCreateTimeStart()));
                    }
					//结束日期,小于或等于
                    if (StringUtil.isNotEmpty(detectionManager.getCreateTimeEnd())) {
                    	predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("createTime").as(String.class), detectionManager.getCreateTimeEnd()));
					}
				}
				return predicate;
			}
		});
		return count;
	}

	@Override
	public void save(DetectionManager detectionManager) {
		detectionManager.setCreateTime(new Timestamp(System.currentTimeMillis()));
		detectionManagerRepository.save(detectionManager);
	}

	@Override
	public void delete(Integer id) {
		detectionManagerRepository.delete(id);
	}

	@Override
	public DetectionManager findById(Integer id) {
		return detectionManagerRepository.findOne(id);
	}
	
	@Override
	public List<DetectionManager> findDetectionByProjectId(Integer projectId) {
		if(projectId == null) {
			return null;
		}
		Sort sort = new Sort(Direction.ASC, "id");
		List<DetectionManager> resultList = detectionManagerRepository.findAll(new Specification<DetectionManager>() {
			@Override
			public Predicate toPredicate(Root<DetectionManager> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				predicate.getExpressions().add(cb.equal(root.get("projectId"), projectId));
				return predicate;
			}
		}, sort);
		return resultList;
	}
	
	@Override
	public void saveImg(DetectionManager detectionManager) {
		if(detectionManager != null) {
			this.save(detectionManager);
		}
	}

	@Override
	public List<DetectionManager> findDetectionManager() {
		return detectionManagerRepository.findDetectionManager();
	}

}