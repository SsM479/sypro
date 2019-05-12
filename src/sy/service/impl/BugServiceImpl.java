package sy.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sy.dao.BaseDaoI;
import sy.model.Tbug;
import sy.pageModel.Bug;
import sy.pageModel.DataGrid;
import sy.service.BugServiceI;
import sy.util.ClobUtil;

@Service("bugService")
public class BugServiceImpl implements BugServiceI {

	private BaseDaoI<Tbug> bugDao;

	public BaseDaoI<Tbug> getBugDao() {
		return bugDao;
	}

	@Autowired
	public void setBugDao(BaseDaoI<Tbug> bugDao) {
		this.bugDao = bugDao;
	}

	@Override
	public DataGrid datagrid(Bug bug) {
		DataGrid dg = new DataGrid();
		String hql = "select new Tbug(t.id,t.name,t.createdatetime) from Tbug t ";
		Map<String, Object> params = new HashMap<String, Object>();
		hql = addWhere(bug, hql, params);
		String totalHql = "select count(*) from Tbug t ";
		Map<String, Object> params2 = new HashMap<String, Object>();
		totalHql = addWhere(bug, totalHql, params2);
		hql = addOrder(bug, hql);
		List<Tbug> l = bugDao.find(hql, params, bug.getPage(), bug.getRows());
		List<Bug> nl = new ArrayList<Bug>();
		changeModel(l, nl);
		dg.setTotal(bugDao.count(totalHql, params));
		dg.setRows(nl);
		return dg;
	}

	private void changeModel(List<Tbug> l, List<Bug> nl) {
		if (l != null && l.size() > 0) {
			for (Tbug t : l) {
				Bug u = new Bug();
				BeanUtils.copyProperties(t, u);
				nl.add(u);
			}
		}
	}

	private String addOrder(Bug bug, String hql) {
		if (bug.getSort() != null) {
			hql += " order by " + bug.getSort() + " " + bug.getOrder();
		}
		return hql;
	}

	private String addWhere(Bug bug, String hql, Map<String, Object> params) {
		hql += " where 1=1 ";
		if (bug.getName() != null && !bug.getName().trim().equals("")) {
			hql += " and t.name like '%%" + bug.getName().trim() + "%%'";
		}
		return hql;
	}

	@Override
	public Bug save(Bug bug) {
		Tbug t = new Tbug();
		t.setId(UUID.randomUUID().toString());
		t.setName(bug.getName());
		t.setNote(ClobUtil.getClob(bug.getNote()));
		t.setCreatedatetime(new Date());
		bugDao.save(t);
		bug.setId(t.getId());
		bug.setNote("");
		bug.setCreatedatetime(t.getCreatedatetime());
		return bug;
	}

	@Override
	public void remove(String ids) {
		for (String id : ids.split(",")) {
			Tbug t = bugDao.get(Tbug.class, id);
			if (t != null) {
				bugDao.delete(t);
			}
		}
	}

	@Override
	public Bug getBug(String id) {
		Tbug t = bugDao.get(Tbug.class, id);
		if (t != null) {
			Bug b = new Bug();
			b.setId(t.getId());
			b.setName(t.getName());
			b.setCreatedatetime(t.getCreatedatetime());
			b.setNote(ClobUtil.getString(t.getNote()));
			return b;
		}
		return null;
	}

	@Override
	public Bug edit(Bug bug) {
		Tbug t = bugDao.get(Tbug.class, bug.getId());
		if (t != null) {
			t.setName(bug.getName());
			t.setNote(ClobUtil.getClob(bug.getNote()));
		}
		bug.setNote("");// 大文本不需要返回到前台
		return bug;
	}

}
