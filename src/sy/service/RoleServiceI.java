package sy.service;

import sy.pageModel.DataGrid;
import sy.pageModel.Role;

public interface RoleServiceI {

	public DataGrid datagrid(Role role);

	public Role save(Role role);

	public void remove(String ids);

	public Role edit(Role role);

}
