package sy.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sy.dao.BaseDaoI;
import sy.model.Tmenu;
import sy.pageModel.Menu;
import sy.service.MenuServiceI;

@Service("menuService")
public class MenuServiceImpl implements MenuServiceI {

	private static final Logger logger = Logger.getLogger(MenuServiceImpl.class);

	private BaseDaoI<Tmenu> menuDao;

	public BaseDaoI<Tmenu> getMenuDao() {
		return menuDao;
	}

	@Autowired
	public void setMenuDao(BaseDaoI<Tmenu> menuDao) {
		this.menuDao = menuDao;
	}

	@Override
	public List<Menu> allTreeNode() {
		List<Menu> nl = new ArrayList<Menu>();
		String hql = "from Tmenu t order by t.seq";
		List<Tmenu> l = menuDao.find(hql);
		if (l != null && l.size() > 0) {
			for (Tmenu t : l) {
				Menu m = new Menu();
				BeanUtils.copyProperties(t, m);
				Map<String, Object> attributes = new HashMap<String, Object>();
				attributes.put("url", t.getUrl());
				m.setAttributes(attributes);
				Tmenu tm = t.getTmenu();// 获得当前节点的父节点对象
				if (tm != null) {
					m.setPid(tm.getId());
				}
				m.setIconCls(t.getIconcls());
				nl.add(m);
			}
		}
		return nl;
	}

	@Override
	public List<Menu> treegrid() {
		List<Tmenu> l = menuDao.find("from Tmenu t order by t.seq");
		List<Menu> nl = new ArrayList<Menu>();
		if (l != null && l.size() > 0) {
			for (Tmenu t : l) {
				Menu r = new Menu();
				BeanUtils.copyProperties(t, r);
				if (t.getTmenu() != null) {
					r.setPid(t.getTmenu().getId());
					r.setPname(t.getTmenu().getText());
				}
				r.setIconCls(t.getIconcls());
				nl.add(r);
			}
		}
		return nl;
	}

	@Override
	public void remove(String id) {
		Tmenu t = menuDao.get(Tmenu.class, id);
		this.del(t);
	}

	private void del(Tmenu r) {
		Set<Tmenu> s = r.getTmenus();
		if (s != null && !s.isEmpty()) {
			for (Tmenu t : s) {
				del(t);
			}
		}
		menuDao.delete(r);
	}

	@Override
	public Menu add(Menu menu) {
		Tmenu t = new Tmenu();
		BeanUtils.copyProperties(menu, t);
		t.setId(UUID.randomUUID().toString());
		if (menu.getPid() != null) {
			Tmenu p = menuDao.get(Tmenu.class, menu.getPid());
			if (p != null) {
				t.setTmenu(p);
			}
		}
		t.setIconcls(menu.getIconCls());
		menuDao.save(t);
		BeanUtils.copyProperties(t, menu);
		return menu;
	}

	@Override
	public Menu edit(Menu menu) {
		Tmenu t = menuDao.get(Tmenu.class, menu.getId());// 要修改的权限
		if (t != null) {
			BeanUtils.copyProperties(menu, t);
			t.setIconcls(menu.getIconCls());
			t.setTmenu(null);// 现将当前节点的父节点置空
			if (menu.getPid() != null && !menu.getPid().trim().equals("") && !menu.getPid().equals(menu.getId())) {
				// 如果pid不为空，并且pid不跟自己的id相同，说明要修改当前节点的父节点
				Tmenu pmenu = menuDao.get(Tmenu.class, menu.getPid());// 要设置的上级权限
				if (pmenu != null) {
					if (isDown(t, pmenu)) {// 要将当前节点修改到当前节点的子节点中
						Set<Tmenu> tmenus = t.getTmenus();// 当前要修改的权限的所有下级权限
						if (tmenus != null && tmenus.size() > 0) {
							for (Tmenu tmenu : tmenus) {
								if (tmenu != null) {
									tmenu.setTmenu(null);
								}
							}
						}
					}
					t.setTmenu(pmenu);
				}
			}
		}
		return menu;
	}

	/**
	 * 判断是否是将当前节点修改到当前节点的子节点
	 * 
	 * @param t
	 * @param pt
	 * @return
	 */
	private boolean isDown(Tmenu t, Tmenu pt) {
		if (pt.getTmenu() != null) {
			if (pt.getTmenu().getId().equals(t.getId())) {
				return true;
			} else {
				return isDown(t, pt.getTmenu());
			}
		}
		return false;
	}

}
