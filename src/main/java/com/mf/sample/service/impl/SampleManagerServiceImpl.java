package com.mf.sample.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mf.entity.sample.SampleManager;
import com.mf.repository.SampleManagerRepository;
import com.mf.sample.service.SampleManagerService;
import com.mf.util.StringUtil;

@Service
public class SampleManagerServiceImpl implements SampleManagerService {

	@Autowired
	@PersistenceContext
	private EntityManager entityManager;

	@Resource
	private SampleManagerRepository sampleManagerRepository;
	
	@Override
	public List<SampleManager> list(SampleManager sampleManager, Integer page, Integer pageSize,Direction direction, String... properties) {
		Pageable pageable=new PageRequest(page-1,pageSize);
		Page<SampleManager> pageCustomer=sampleManagerRepository.findAll(new Specification<SampleManager>() {
			@Override
			public Predicate toPredicate(Root<SampleManager> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(sampleManager!=null){
					if(StringUtil.isNotEmpty(sampleManager.getSampleCode())) {
						predicate.getExpressions().add(cb.like(root.get("sampleCode"), "%"+sampleManager.getSampleCode()+"%"));
					}
					if(StringUtil.isNotEmpty(sampleManager.getSampleName())) {
						predicate.getExpressions().add(cb.like(root.get("sampleName"), "%"+sampleManager.getSampleName()+"%"));
					}
				}
				return predicate;
			}
		}, pageable);
		return pageCustomer.getContent();
	}

	@Override
	public Long getCount(SampleManager sampleManager) {
		Long count = sampleManagerRepository.count(new Specification<SampleManager>() {
			@Override
			public Predicate toPredicate(Root<SampleManager> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(sampleManager!=null){
					if(StringUtil.isNotEmpty(sampleManager.getSampleCode())) {
						predicate.getExpressions().add(cb.like(root.get("sampleCode"), "%"+sampleManager.getSampleCode()+"%"));
					}
					if(StringUtil.isNotEmpty(sampleManager.getSampleName())) {
						predicate.getExpressions().add(cb.like(root.get("sampleName"), "%"+sampleManager.getSampleName()+"%"));
					}
				}
				return predicate;
			}
		});
		return count;
	}

	@Override
	public void save(SampleManager sampleManager) throws Exception {
		sampleManagerRepository.save(sampleManager);
	}

	@Override
	public void delete(Integer id) {
		sampleManagerRepository.delete(id);
	}

	@Override
	public SampleManager findById(Integer id) {
		return sampleManagerRepository.findOne(id);
	}
	
	@Override
	public List<SampleManager> findProjectName() {
		return sampleManagerRepository.findSampleName();
	}
	
	@Override
	public SampleManager findSample(String projectId) {
		return sampleManagerRepository.findSample(projectId);
	}
	
	@Override
	@Transactional
	public void updateProjectId(Integer id, String projectId) {
		String jpql = "update sample_manager set project_id =:projectId where id =:id";
	    Query query = entityManager.createNativeQuery(jpql);
	    query.setParameter("projectId", projectId).setParameter("id", id);
	    query.executeUpdate();
	}
	
}