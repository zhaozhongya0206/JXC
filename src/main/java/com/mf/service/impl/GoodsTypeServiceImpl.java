package com.mf.service.impl;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mf.entity.GoodsType;
import com.mf.repository.GoodsTypeRepository;
import com.mf.service.GoodsTypeService;

/**
 * 商品类别Service实现类
 * @author Administrator
 *
 */
@Service("goodsTypeService")
public class GoodsTypeServiceImpl implements GoodsTypeService{

	@Resource
	private GoodsTypeRepository goodsTypeRepository;

	@Override
	public List<GoodsType> findByParentId(int parentId) {
		return goodsTypeRepository.findByParentId(parentId);
	}

	@Override
	public void save(GoodsType goodsType) {
		goodsTypeRepository.save(goodsType);
	}

	@Override
	public void delete(Integer id) {
		goodsTypeRepository.delete(id);
	}

	@Override
	public GoodsType findById(Integer id) {
		return goodsTypeRepository.findOne(id);
	}




}
