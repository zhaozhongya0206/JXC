package com.mf.index.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mf.index.service.IndexService;

@Service
public class IndexServiceImpl implements IndexService {
	
	private static final Logger log = LoggerFactory.getLogger(IndexServiceImpl.class);
	
	@Autowired
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public  Map<String,Object> listIndex() {
		 Map<String,Object> resultMap = new HashMap<String,Object>();
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		try {
			StringBuilder selectSql = new StringBuilder();
	        selectSql.append("select b.id,b.project_name,c.photo_name,c.photo_ip,c.photo_port,c.photo_logo_name,c.photo_logo_password,c.photo_code,b.project_flag, "
	        			+ "c.file, c.code_type,b.project_code,b.sample_code,b.sample_name from project_manager b, photo_manager c where b.photo_code=c.photo_code "
	        			+ " and b.project_flag in(0,1,2) "
	        			+ " order by c.create_time asc ");
	        
	        StringBuilder countSql = new StringBuilder
	        		("select count(1) from exceptions a where a.flag='0' and a.project_code=?1 and a.project_name=?2 and a.sample_code=?3 and a.sample_name=?4");
	        Map<Object, Object> mapCount = new HashMap<Object, Object>();
	        
	        log.info("listIndex selectSql: " + selectSql.toString());
			Query query = entityManager.createNativeQuery(selectSql.toString());
			
	        int startIndex = 0;
	        int pageSize = 4;
	        query.setFirstResult(startIndex);
	        query.setMaxResults(pageSize);
	        List<Object[]> result = (List<Object[]>)query.getResultList();
	        for(Object[] obj : result) {
	        	Map<String, Object> map = new HashMap<String, Object>();
	        	map.put("projectId", obj[0]);
	        	if(obj[0] != null) {
		            log.info(" querySql:"+ countSql.toString());
		        	Query countQuery = entityManager.createNativeQuery(countSql.toString());
		        	countQuery.setParameter(1, String.valueOf(obj[11]));
		        	countQuery.setParameter(2, String.valueOf(obj[1]));
		        	countQuery.setParameter(3, String.valueOf(obj[12]));
		        	countQuery.setParameter(4, String.valueOf(obj[13]));
		    		BigInteger totalCount = (BigInteger) countQuery.getSingleResult();
		    		mapCount.put(obj[0], totalCount.longValue());
	        	}
	        	map.put("projectName", obj[1]);
	        	map.put("photoName", obj[2]);
	        	map.put("photoIP", obj[3]);
	        	map.put("photoPost", obj[4]);
	        	map.put("photoUserName", obj[5]);
	        	map.put("photoPassword", obj[6]);
	        	map.put("photoCode", obj[7]);
	        	map.put("projectFlag", obj[8]);
	        	map.put("file", obj[9]);
	        	map.put("codeType", obj[10]);
	        	listMap.add(map);
	        }
	        if(listMap == null || listMap.size()<=0) {
				resultMap.put("success", false);
			} else {
				resultMap.put("list", listMap);
				resultMap.put("mapCount", mapCount);
				resultMap.put("success", true);
			}
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
		return resultMap;
	}

}
