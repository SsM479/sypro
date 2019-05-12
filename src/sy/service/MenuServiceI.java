package sy.service;

import java.util.List;

import sy.pageModel.Menu;

public interface MenuServiceI {

	public List<Menu> allTreeNode();

	public List<Menu> treegrid();

	public void remove(String id);

	public Menu add(Menu menu);

	public Menu edit(Menu menu);

}
