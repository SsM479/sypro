package sy.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sy.dao.BaseDaoI;
import sy.model.Tresource;
import sy.model.Trole;
import sy.model.TroleTresource;
import sy.pageModel.DataGrid;
import sy.pageModel.Role;
import sy.service.RoleServiceI;

@Service("roleService")
public class RoleServiceImpl implements RoleServiceI {

	private BaseDaoI<Trole> roleDao;
	private BaseDaoI<TroleTresource> roleResourceDao;
	private BaseDaoI<Tresource> resourceDao;

	public BaseDaoI<Tresource> getResourceDao() {
		return resourceDao;
	}

	@Autowired
	public void setResourceDao(BaseDaoI<Tresource> resourceDao) {
		this.resourceDao = resourceDao;
	}

	public BaseDaoI<TroleTresource> getRoleResourceDao() {
		return roleResourceDao;
	}

	@Autowired
	public void setRoleResourceDao(BaseDaoI<TroleTresource> roleResourceDao) {
		this.roleResourceDao = roleResourceDao;
	}

	public BaseDaoI<Trole> getRoleDao() {
		return roleDao;
	}

	@Autowired
	public void setRoleDao(BaseDaoI<Trole> roleDao) {
		this.roleDao = roleDao;
	}

	@Override
	public DataGrid datagrid(Role role) {
		DataGrid dg = new DataGrid();
		String hql = "from Trole t ";
		Map<String, Object> params = new HashMap<String, Object>();
		hql = addWhere(role, hql, params);
		String totalHql = "select count(*) " + hql;
		hql = addOrder(role, hql);
		List<Trole> l = roleDao.find(hql, params, role.getPage(), role.getRows());
		List<Role> nl = new ArrayList<Role>();
		changeModel(l, nl);
		dg.setTotal(roleDao.count(totalHql, params));
		dg.setRows(nl);
		return dg;
	}

	private void changeModel(List<Trole> l, List<Role> nl) {
		if (l != null && l.size() > 0) {
			for (Trole t : l) {
				Role u = new Role();
				BeanUtils.copyProperties(t, u);

				Set<TroleTresource> s = t.getTroleTresources();
				if (s != null && !s.isEmpty()) {
					String resourceIds = "";
					String resourceNames = "";
					for (TroleTresource rr : s) {
						Tresource r = rr.getTresource();
						if (resourceIds.length() > 0) {
							resourceIds += ",";
							resourceNames += ",";
						}
						resourceIds += r.getId();
						resourceNames += r.getText();
					}
					u.setResourceIds(resourceIds);
					u.setResourceNames(resourceNames);
				}

				nl.add(u);
			}
		}
	}

	private String addOrder(Role role, String hql) {
		if (role.getSort() != null) {
			hql += " order by " + role.getSort() + " " + role.getOrder();
		}
		return hql;
	}

	private String addWhere(Role role, String hql, Map<String, Object> params) {
		return hql;
	}

	@Override
	public Role save(Role role) {
		Trole t = new Trole();
		BeanUtils.copyProperties(role, t);
		t.setId(UUID.randomUUID().toString());
		roleDao.save(t);
		if (role.getResourceIds() != null) {
			String resourceNames = "";
			for (String id : role.getResourceIds().split(",")) {
				Tresource r = resourceDao.get(Tresource.class, id);
				if (r != null) {
					TroleTresource rr = new TroleTresource();
					rr.setId(UUID.randomUUID().toString());
					rr.setTresource(r);
					rr.setTrole(t);
					roleResourceDao.save(rr);

					if (resourceNames.length() > 0) {
						resourceNames += ",";
					}
					resourceNames += r.getText();
				}
			}
			role.setResourceNames(resourceNames);
		}
		role.setId(t.getId());
		return role;
	}

	@Override
	public void remove(String ids) {
		for (String id : ids.split(",")) {
			Trole t = roleDao.get(Trole.class, id);
			if (t != null) {
				roleDao.delete(t);
			}
		}
	}

	@Override
	public Role edit(Role role) {
		Trole t = roleDao.get(Trole.class, role.getId());
		if (t != null) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("trole", t);
			roleResourceDao.executeHql("delete TroleTresource t where t.trole=:trole", params);

			BeanUtils.copyProperties(role, t);
			if (role.getResourceIds() != null) {
				String resourceNames = "";
				for (String id : role.getResourceIds().split(",")) {
					Tresource r = resourceDao.get(Tresource.class, id);
					if (r != null) {
						TroleTresource rr = new TroleTresource();
						rr.setId(UUID.randomUUID().toString());
						rr.setTresource(r);
						rr.setTrole(t);
						roleResourceDao.save(rr);

						if (resourceNames.length() > 0) {
							resourceNames += ",";
						}
						resourceNames += r.getText();
					}
				}
				role.setResourceNames(resourceNames);
			}
		}
		return role;
	}
}
