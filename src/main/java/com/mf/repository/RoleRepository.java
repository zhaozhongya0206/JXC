package com.mf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.mf.entity.Role;

/**
 * 角色Repository接口
 * @author Administrator
 *
 */
public interface RoleRepository extends JpaRepository<Role, Integer>,JpaSpecificationExecutor<Role>{

	/**
	 * 根据用户id查角色集合
	 * @param id
	 * @return
	 */
	@Query(value="SELECT r.* FROM t_user u,t_role r,t_user_role ur WHERE ur.`user_id`=u.`id` AND ur.`role_id`=r.`id` AND u.`id`=?1",nativeQuery=true)
	public List<Role> findByUserId(Integer id);
	
	/**
	 * 根据角色名查找角色实体
	 * @param roleName
	 * @return
	 */
	@Query(value="select * from t_role where name=?1",nativeQuery=true)
	public Role findByRoleName(String roleName);
}
