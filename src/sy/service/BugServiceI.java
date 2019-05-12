package sy.service;

import sy.pageModel.Bug;
import sy.pageModel.DataGrid;

public interface BugServiceI {

	public DataGrid datagrid(Bug bug);

	public Bug save(Bug bug);

	public void remove(String ids);

	public Bug getBug(String id);

	public Bug edit(Bug bug);

}
