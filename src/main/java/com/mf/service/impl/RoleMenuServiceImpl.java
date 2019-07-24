package com.mf.service.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mf.entity.RoleMenu;
import com.mf.repository.RoleMenuRepository;
import com.mf.service.RoleMenuService;


/**
 * 角色菜单关联Service实现类
 * @author Administrator
 *
 */
@Service("roleMenuService")
@Transactional
public class RoleMenuServiceImpl implements RoleMenuService{

	@Resource
	private RoleMenuRepository roleMenuRepository;
	
	@Override
	public void deleteByRoleId(Integer roleId) {
		roleMenuRepository.deleteByRoleId(roleId);
	}

	@Override
	public void save(RoleMenu roleMenu) {
		roleMenuRepository.save(roleMenu);
	}

}
