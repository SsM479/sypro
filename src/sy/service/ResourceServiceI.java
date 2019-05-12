package sy.service;

import java.util.List;

import sy.pageModel.Resource;

public interface ResourceServiceI {

	public List<Resource> treegrid();

	public List<Resource> allTreeNode();

	public Resource add(Resource resource);

	public void remove(String id);

	public Resource edit(Resource resource);

}
