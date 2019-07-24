package com.mf.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.mf.entity.GoodsUnit;

/**
 * 商品单位Repository接口
 * @author Administrator
 *
 */
public interface GoodsUnitRepository extends JpaRepository<GoodsUnit, Integer>{

	
}
