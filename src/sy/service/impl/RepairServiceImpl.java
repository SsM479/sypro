package sy.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sy.dao.BaseDaoI;
import sy.model.Tmenu;
import sy.model.Tresource;
import sy.model.Trole;
import sy.model.TroleTresource;
import sy.model.Tuser;
import sy.model.TuserTrole;
import sy.service.RepairServiceI;
import sy.util.Encrypt;

@Service("repairService")
public class RepairServiceImpl implements RepairServiceI {

	private BaseDaoI<Tmenu> menuDao;
	private BaseDaoI<Tuser> userDao;
	private BaseDaoI<Tresource> resourceDao;
	private BaseDaoI<Trole> roleDao;
	private BaseDaoI<TroleTresource> roleResourceDao;
	private BaseDaoI<TuserTrole> userRoleDao;

	public BaseDaoI<TuserTrole> getUserRoleDao() {
		return userRoleDao;
	}

	@Autowired
	public void setUserRoleDao(BaseDaoI<TuserTrole> userRoleDao) {
		this.userRoleDao = userRoleDao;
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

	public BaseDaoI<Tresource> getResourceDao() {
		return resourceDao;
	}

	@Autowired
	public void setResourceDao(BaseDaoI<Tresource> resourceDao) {
		this.resourceDao = resourceDao;
	}

	public BaseDaoI<Tuser> getUserDao() {
		return userDao;
	}

	@Autowired
	public void setUserDao(BaseDaoI<Tuser> userDao) {
		this.userDao = userDao;
	}

	public BaseDaoI<Tmenu> getMenuDao() {
		return menuDao;
	}

	@Autowired
	public void setMenuDao(BaseDaoI<Tmenu> menuDao) {
		this.menuDao = menuDao;
	}

	@Override
	synchronized public void delAndRepair() {
		menuDao.executeHql("update Tmenu t set t.tmenu = null");
		menuDao.executeHql("delete Tmenu");
		roleResourceDao.executeHql("delete TroleTresource");
		userRoleDao.executeHql("delete TuserTrole");
		resourceDao.executeHql("update Tresource t set t.tresource = null");
		resourceDao.executeHql("delete Tresource");
		roleDao.executeHql("delete Trole");
		userDao.executeHql("delete Tuser");

		repair();
	}

	@Override
	synchronized public void repair() {

		repairMenu();

		repairResource();

		repairRole();

		repairUser();

		repairRoleResource();

		repairUserRole();

	}

	private void repairUserRole() {
		userRoleDao.executeHql("delete TuserTrole t where t.tuser.id in ( '0' )");

		TuserTrole userrole = new TuserTrole();
		userrole.setId(UUID.randomUUID().toString());
		userrole.setTrole(roleDao.get(Trole.class, "0"));
		userrole.setTuser(userDao.get(Tuser.class, "0"));
		userRoleDao.save(userrole);
	}

	private void repairRoleResource() {
		roleResourceDao.executeHql("delete TroleTresource t where t.trole.id = '0'");
		List<Tresource> l = resourceDao.find("from Tresource");
		Trole r = roleDao.get(Trole.class, "0");
		for (Tresource t : l) {
			TroleTresource rr = new TroleTresource();
			rr.setId(UUID.randomUUID().toString());
			rr.setTrole(r);
			rr.setTresource(t);
			roleResourceDao.save(rr);
		}
	}

	private void repairRole() {
		Trole admin = new Trole();
		admin.setId("0");
		admin.setText("超管角色");
		roleDao.saveOrUpdate(admin);
	}

	private void repairResource() {
		resourceDao.executeHql("update Tresource t set t.tresource = null");

		Tresource sy = new Tresource();
		sy.setId("0");
		sy.setText("首页");
		sy.setSeq(BigDecimal.valueOf(1));
		resourceDao.saveOrUpdate(sy);

		Tresource xtgl = new Tresource();
		xtgl.setTresource(sy);
		xtgl.setId("xtgl");
		xtgl.setText("系统管理");
		xtgl.setSeq(BigDecimal.valueOf(1));
		resourceDao.saveOrUpdate(xtgl);

		Tresource yhgl = new Tresource();
		yhgl.setTresource(xtgl);
		yhgl.setId("yhgl");
		yhgl.setText("用户管理");
		yhgl.setSeq(BigDecimal.valueOf(1));
		resourceDao.saveOrUpdate(yhgl);

		Tresource yhglAdd = new Tresource();
		yhglAdd.setTresource(yhgl);
		yhglAdd.setId("yhglAdd");
		yhglAdd.setText("用户添加");
		yhglAdd.setSeq(BigDecimal.valueOf(1));
		yhglAdd.setUrl("/userController/add.action");
		resourceDao.saveOrUpdate(yhglAdd);

		Tresource yhglDel = new Tresource();
		yhglDel.setTresource(yhgl);
		yhglDel.setId("yhglDel");
		yhglDel.setText("用户删除");
		yhglDel.setSeq(BigDecimal.valueOf(2));
		yhglDel.setUrl("/userController/remove.action");
		resourceDao.saveOrUpdate(yhglDel);

		Tresource yhglEdit = new Tresource();
		yhglEdit.setTresource(yhgl);
		yhglEdit.setId("yhglEdit");
		yhglEdit.setText("用户修改");
		yhglEdit.setSeq(BigDecimal.valueOf(3));
		yhglEdit.setUrl("/userController/edit.action");
		resourceDao.saveOrUpdate(yhglEdit);

		Tresource yhglDatagrid = new Tresource();
		yhglDatagrid.setTresource(yhgl);
		yhglDatagrid.setId("yhglDatagrid");
		yhglDatagrid.setText("用户列表");
		yhglDatagrid.setSeq(BigDecimal.valueOf(4));
		yhglDatagrid.setUrl("/userController/datagrid.action");
		resourceDao.saveOrUpdate(yhglDatagrid);

		Tresource yhglModifyPwd = new Tresource();
		yhglModifyPwd.setTresource(yhgl);
		yhglModifyPwd.setId("yhglModifyPwd");
		yhglModifyPwd.setText("用户修改密码");
		yhglModifyPwd.setSeq(BigDecimal.valueOf(5));
		yhglModifyPwd.setUrl("/userController/modifyPwd.action");
		resourceDao.saveOrUpdate(yhglModifyPwd);

		Tresource yhglModifyRole = new Tresource();
		yhglModifyRole.setTresource(yhgl);
		yhglModifyRole.setId("yhglModifyRole");
		yhglModifyRole.setText("用户批量修改角色");
		yhglModifyRole.setSeq(BigDecimal.valueOf(6));
		yhglModifyRole.setUrl("/userController/modifyRole.action");
		resourceDao.saveOrUpdate(yhglModifyRole);

		Tresource jsgl = new Tresource();
		jsgl.setTresource(xtgl);
		jsgl.setId("jsgl");
		jsgl.setText("角色管理");
		jsgl.setSeq(BigDecimal.valueOf(2));
		resourceDao.saveOrUpdate(jsgl);

		Tresource jsglAdd = new Tresource();
		jsglAdd.setTresource(jsgl);
		jsglAdd.setId("jsglAdd");
		jsglAdd.setText("角色添加");
		jsglAdd.setSeq(BigDecimal.valueOf(1));
		jsglAdd.setUrl("/roleController/add.action");
		resourceDao.saveOrUpdate(jsglAdd);

		Tresource jsglDel = new Tresource();
		jsglDel.setTresource(jsgl);
		jsglDel.setId("jsglDel");
		jsglDel.setText("角色删除");
		jsglDel.setSeq(BigDecimal.valueOf(2));
		jsglDel.setUrl("/roleController/remove.action");
		resourceDao.saveOrUpdate(jsglDel);

		Tresource jsglEdit = new Tresource();
		jsglEdit.setTresource(jsgl);
		jsglEdit.setId("jsglEdit");
		jsglEdit.setText("角色修改");
		jsglEdit.setSeq(BigDecimal.valueOf(3));
		jsglEdit.setUrl("/roleController/edit.action");
		resourceDao.saveOrUpdate(jsglEdit);

		Tresource jsglDatagrid = new Tresource();
		jsglDatagrid.setTresource(jsgl);
		jsglDatagrid.setId("jsglDatagrid");
		jsglDatagrid.setText("角色列表");
		jsglDatagrid.setSeq(BigDecimal.valueOf(4));
		jsglDatagrid.setUrl("/roleController/datagrid.action");
		resourceDao.saveOrUpdate(jsglDatagrid);

		Tresource zygl = new Tresource();
		zygl.setTresource(xtgl);
		zygl.setId("zygl");
		zygl.setText("资源管理");
		zygl.setSeq(BigDecimal.valueOf(3));
		resourceDao.saveOrUpdate(zygl);

		Tresource zyglAdd = new Tresource();
		zyglAdd.setTresource(zygl);
		zyglAdd.setId("zyglAdd");
		zyglAdd.setText("资源添加");
		zyglAdd.setSeq(BigDecimal.valueOf(1));
		zyglAdd.setUrl("/resourceController/add.action");
		resourceDao.saveOrUpdate(zyglAdd);

		Tresource zyglDel = new Tresource();
		zyglDel.setTresource(zygl);
		zyglDel.setId("zyglDel");
		zyglDel.setText("资源删除");
		zyglDel.setSeq(BigDecimal.valueOf(2));
		zyglDel.setUrl("/resourceController/remove.action");
		resourceDao.saveOrUpdate(zyglDel);

		Tresource zyglEdit = new Tresource();
		zyglEdit.setTresource(zygl);
		zyglEdit.setId("zyglEdit");
		zyglEdit.setText("资源修改");
		zyglEdit.setSeq(BigDecimal.valueOf(3));
		zyglEdit.setUrl("/resourceController/edit.action");
		resourceDao.saveOrUpdate(zyglEdit);

		Tresource zyglTreegrid = new Tresource();
		zyglTreegrid.setTresource(zygl);
		zyglTreegrid.setId("zyglTreegrid");
		zyglTreegrid.setText("资源列表");
		zyglTreegrid.setSeq(BigDecimal.valueOf(4));
		zyglTreegrid.setUrl("/resourceController/treegrid.action");
		resourceDao.saveOrUpdate(zyglTreegrid);

		Tresource cdgl = new Tresource();
		cdgl.setTresource(xtgl);
		cdgl.setId("cdgl");
		cdgl.setText("菜单管理");
		cdgl.setSeq(BigDecimal.valueOf(4));
		resourceDao.saveOrUpdate(cdgl);

		Tresource cdglAdd = new Tresource();
		cdglAdd.setTresource(cdgl);
		cdglAdd.setId("cdglAdd");
		cdglAdd.setText("菜单添加");
		cdglAdd.setSeq(BigDecimal.valueOf(1));
		cdglAdd.setUrl("/menuController/add.action");
		resourceDao.saveOrUpdate(cdglAdd);

		Tresource cdglDel = new Tresource();
		cdglDel.setTresource(cdgl);
		cdglDel.setId("cdglDel");
		cdglDel.setText("菜单删除");
		cdglDel.setSeq(BigDecimal.valueOf(2));
		cdglDel.setUrl("/menuController/remove.action");
		resourceDao.saveOrUpdate(cdglDel);

		Tresource cdglEdit = new Tresource();
		cdglEdit.setTresource(cdgl);
		cdglEdit.setId("cdglEdit");
		cdglEdit.setText("菜单修改");
		cdglEdit.setSeq(BigDecimal.valueOf(3));
		cdglEdit.setUrl("/menuController/edit.action");
		resourceDao.saveOrUpdate(cdglEdit);

		Tresource cdglTreegrid = new Tresource();
		cdglTreegrid.setTresource(cdgl);
		cdglTreegrid.setId("cdglTreegrid");
		cdglTreegrid.setText("菜单列表");
		cdglTreegrid.setSeq(BigDecimal.valueOf(4));
		cdglTreegrid.setUrl("/menuController/treegrid.action");
		resourceDao.saveOrUpdate(cdglTreegrid);

		Tresource buggl = new Tresource();
		buggl.setTresource(xtgl);
		buggl.setId("buggl");
		buggl.setText("BUG管理");
		buggl.setSeq(BigDecimal.valueOf(5));
		resourceDao.saveOrUpdate(buggl);

		Tresource bugglAdd = new Tresource();
		bugglAdd.setTresource(buggl);
		bugglAdd.setId("bugglAdd");
		bugglAdd.setText("BUG添加");
		bugglAdd.setSeq(BigDecimal.valueOf(1));
		bugglAdd.setUrl("/bugController/add.action");
		resourceDao.saveOrUpdate(bugglAdd);

		Tresource bugglDel = new Tresource();
		bugglDel.setTresource(buggl);
		bugglDel.setId("bugglDel");
		bugglDel.setText("BUG删除");
		bugglDel.setSeq(BigDecimal.valueOf(2));
		bugglDel.setUrl("/bugController/remove.action");
		resourceDao.saveOrUpdate(bugglDel);

		Tresource bugglEdit = new Tresource();
		bugglEdit.setTresource(buggl);
		bugglEdit.setId("bugglEdit");
		bugglEdit.setText("BUG修改");
		bugglEdit.setSeq(BigDecimal.valueOf(3));
		bugglEdit.setUrl("/bugController/edit.action");
		resourceDao.saveOrUpdate(bugglEdit);

		Tresource bugglDatagrid = new Tresource();
		bugglDatagrid.setTresource(buggl);
		bugglDatagrid.setId("bugglDatagrid");
		bugglDatagrid.setText("BUG列表");
		bugglDatagrid.setSeq(BigDecimal.valueOf(4));
		bugglDatagrid.setUrl("/bugController/datagrid.action");
		resourceDao.saveOrUpdate(bugglDatagrid);

		Tresource bugglUpdate = new Tresource();
		bugglUpdate.setTresource(buggl);
		bugglUpdate.setId("bugglUpdate");
		bugglUpdate.setText("BUG上传");
		bugglUpdate.setSeq(BigDecimal.valueOf(5));
		bugglUpdate.setUrl("/bugController/upload.action");
		resourceDao.saveOrUpdate(bugglUpdate);

	}

	private void repairMenu() {
		menuDao.executeHql("update Tmenu m set m.tmenu = null");

		Tmenu root = new Tmenu();
		root.setId("0");
		root.setText("首页");
		root.setUrl("");
		root.setSeq(BigDecimal.valueOf(1));
		root.setIconcls("icon-tip");
		menuDao.saveOrUpdate(root);

		Tmenu xtgl = new Tmenu();
		xtgl.setId("xtgl");
		xtgl.setTmenu(root);
		xtgl.setText("系统管理");
		xtgl.setUrl("");
		xtgl.setSeq(BigDecimal.valueOf(1));
		xtgl.setIconcls("icon-sum");
		menuDao.saveOrUpdate(xtgl);

		Tmenu yhgl = new Tmenu();
		yhgl.setId("yhgl");
		yhgl.setTmenu(xtgl);
		yhgl.setText("用户管理");
		yhgl.setUrl("/admin/yhgl.jsp");
		yhgl.setSeq(BigDecimal.valueOf(1));
		yhgl.setIconcls("icon-back");
		menuDao.saveOrUpdate(yhgl);

		Tmenu jsgl = new Tmenu();
		jsgl.setId("jsgl");
		jsgl.setTmenu(xtgl);
		jsgl.setText("角色管理");
		jsgl.setUrl("/admin/jsgl.jsp");
		jsgl.setSeq(BigDecimal.valueOf(2));
		menuDao.saveOrUpdate(jsgl);

		Tmenu zygl = new Tmenu();
		zygl.setId("zygl");
		zygl.setTmenu(xtgl);
		zygl.setText("资源管理");
		zygl.setUrl("/admin/zygl.jsp");
		zygl.setSeq(BigDecimal.valueOf(3));
		menuDao.saveOrUpdate(zygl);

		Tmenu cdgl = new Tmenu();
		cdgl.setId("cdgl");
		cdgl.setTmenu(xtgl);
		cdgl.setText("菜单管理");
		cdgl.setSeq(BigDecimal.valueOf(4));
		cdgl.setUrl("/admin/cdgl.jsp");
		menuDao.saveOrUpdate(cdgl);

		Tmenu buggl = new Tmenu();
		buggl.setId("buggl");
		buggl.setTmenu(xtgl);
		buggl.setText("BUG管理");
		buggl.setSeq(BigDecimal.valueOf(5));
		buggl.setUrl("/admin/buggl.jsp");
		menuDao.saveOrUpdate(buggl);

		Tmenu druidgl = new Tmenu();
		druidgl.setId("druidgl");
		druidgl.setTmenu(root);
		druidgl.setText("连接池管理");
		druidgl.setUrl("");
		druidgl.setSeq(BigDecimal.valueOf(2));
		menuDao.saveOrUpdate(druidgl);

		Tmenu druidjk = new Tmenu();
		druidjk.setId("druidjk");
		druidjk.setTmenu(druidgl);
		druidjk.setText("连接池监控");
		druidjk.setUrl("/druidController/druid.action");
		druidjk.setSeq(BigDecimal.valueOf(1));
		druidjk.setIconcls("icon-tip");
		menuDao.saveOrUpdate(druidjk);
	}

	private void repairUser() {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("name", "孙宇");
		List<Tuser> tl = userDao.find("from Tuser t where t.name = :name and t.id != '0'", m);
		if (tl != null && tl.size() > 0) {
			for (Tuser u : tl) {
				u.setName(u.getName() + UUID.randomUUID().toString());
			}
		}

		Tuser admin = new Tuser();
		admin.setId("0");
		admin.setName("孙宇");
		admin.setPwd(Encrypt.e("admin"));
		admin.setModifydatetime(new Date());
		userDao.saveOrUpdate(admin);

		Tuser guest = userDao.get("from Tuser t where t.name = 'guest'");
		if (guest == null) {
			guest = new Tuser();
			guest.setId(UUID.randomUUID().toString());
			guest.setName("guest");
			guest.setPwd(Encrypt.e("123456"));
			guest.setCreatedatetime(new Date());
			userDao.save(guest);
		}
	}

}
