package com.linkedin;

import java.util.List;

import javax.ejb.Local;

@Local
public interface CatalogLocal {
	
	public List<CatalogItem> getItems();
	
	public void addItem(CatalogItem item);
	
	public CatalogItem findItem(Long itemId);
	
	public void deleteItem(CatalogItem item);
	
	public List<CatalogItem> searchByName(String name);
	
	public void saveItem(CatalogItem item);

}
