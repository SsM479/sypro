package sy.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sy.dao.BaseDaoI;
import sy.model.Tresource;
import sy.model.TroleTresource;
import sy.pageModel.Resource;
import sy.service.ResourceServiceI;

@Service("resourceService")
public class ResourceServiceImpl implements ResourceServiceI {

	private BaseDaoI<Tresource> resourceDao;
	private BaseDaoI<TroleTresource> roleResourceDao;

	public BaseDaoI<TroleTresource> getRoleResourceDao() {
		return roleResourceDao;
	}

	@Autowired
	public void setRoleResourceDao(BaseDaoI<TroleTresource> roleResourceDao) {
		this.roleResourceDao = roleResourceDao;
	}

	public BaseDaoI<Tresource> getResourceDao() {
		return resourceDao;
	}

	@Autowired
	public void setResourceDao(BaseDaoI<Tresource> resourceDao) {
		this.resourceDao = resourceDao;
	}

	@Override
	public List<Resource> treegrid() {
		List<Tresource> l = resourceDao.find("from Tresource t order by t.seq");
		List<Resource> nl = new ArrayList<Resource>();
		if (l != null && l.size() > 0) {
			for (Tresource t : l) {
				Resource r = new Resource();
				BeanUtils.copyProperties(t, r);
				if (t.getTresource() != null) {
					r.setPid(t.getTresource().getId());
					r.setPname(t.getTresource().getText());
				}
				nl.add(r);
			}
		}
		return nl;
	}

	@Override
	public List<Resource> allTreeNode() {
		List<Resource> nl = new ArrayList<Resource>();
		String hql = "from Tresource t order by t.seq";
		List<Tresource> l = resourceDao.find(hql);
		if (l != null && l.size() > 0) {
			for (Tresource t : l) {
				Resource m = new Resource();
				BeanUtils.copyProperties(t, m);
				Tresource tm = t.getTresource();// 获得当前节点的父节点对象
				if (tm != null) {
					m.setPid(tm.getId());
				}
				nl.add(m);
			}
		}
		return nl;
	}

	@Override
	public Resource add(Resource resource) {
		Tresource t = new Tresource();
		BeanUtils.copyProperties(resource, t);
		t.setId(UUID.randomUUID().toString());
		if (resource.getPid() != null) {
			Tresource p = resourceDao.get(Tresource.class, resource.getPid());
			if (p != null) {
				t.setTresource(p);
			}
		}
		resourceDao.save(t);
		BeanUtils.copyProperties(t, resource);
		return resource;
	}

	@Override
	public void remove(String id) {
		Tresource t = resourceDao.get(Tresource.class, id);
		this.del(t);
	}

	private void del(Tresource r) {
		Set<Tresource> s = r.getTresources();
		if (s != null && !s.isEmpty()) {
			for (Tresource t : s) {
				del(t);
			}
		}
		resourceDao.delete(r);
	}

	@Override
	public Resource edit(Resource resource) {
		Tresource t = resourceDao.get(Tresource.class, resource.getId());// 要修改的权限
		if (t != null) {
			BeanUtils.copyProperties(resource, t);
			t.setTresource(null);// 现将当前节点的父节点置空
			if (resource.getPid() != null && !resource.getPid().trim().equals("") && !resource.getPid().equals(resource.getId())) {
				// 如果pid不为空，并且pid不跟自己的id相同，说明要修改当前节点的父节点
				Tresource presource = resourceDao.get(Tresource.class, resource.getPid());// 要设置的上级权限
				if (presource != null) {
					if (isDown(t, presource)) {// 要将当前节点修改到当前节点的子节点中
						Set<Tresource> tresources = t.getTresources();// 当前要修改的权限的所有下级权限
						if (tresources != null && tresources.size() > 0) {
							for (Tresource tresource : tresources) {
								if (tresource != null) {
									tresource.setTresource(null);
								}
							}
						}
					}
					t.setTresource(presource);
				}
			}
		}
		return resource;
	}

	/**
	 * 判断是否是将当前节点修改到当前节点的子节点
	 * 
	 * @param t
	 * @param pt
	 * @return
	 */
	private boolean isDown(Tresource t, Tresource pt) {
		if (pt.getTresource() != null) {
			if (pt.getTresource().getId().equals(t.getId())) {
				return true;
			} else {
				return isDown(t, pt.getTresource());
			}
		}
		return false;
	}

}
