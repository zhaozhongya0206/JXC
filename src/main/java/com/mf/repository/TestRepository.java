package com.mf.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import com.mf.entity.camera.Test;

/**
 * @ClassName:TestRepository  
 * @Description:测试Repository接口
 * @author zzy
 * @date 2018年12月10日
 * @version V1.0
 */
public interface TestRepository extends JpaRepository<Test, Integer>,JpaSpecificationExecutor<Test>{

	/**
	 * 根据名称模糊查询客户信息
	 * @param name
	 * @return
	 */
	@Query(value="select * from t_test where name like ?1",nativeQuery=true)
	public List<Test> findByName(String name);
}
