package com.linkedin.jsf;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.linkedin.CatalogItem;
import com.linkedin.CatalogLocal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//@SessionScoped
@RequestScoped
@Named
public class CatalogItemFormBean implements Serializable {
	
	//@EJB
	@Inject //we can use @Inject instead of @EJB because of CDI
	private CatalogLocal catalogBean;
	
	@Inject
	//@Named("remoteInventoryService") - Option 1
	//@RemoteService //Option 2
	// Option 3 - Using InventoryServiceFactory
	private InventoryService inventoryService;
	
	private CatalogItem item = new CatalogItem();
	
	private List<CatalogItem> items = new ArrayList<>();
	
	private String searchText;
	
	public void searchByName() {
		this.items = this.catalogBean.searchByName(this.searchText);
	}

	public String addItem() {
		//long itemId = this.catalogBean.getItems().size() + 1;
		
		/*this.catalogBean.addItem(new CatalogItem(this.item.getName(), this.item.getManufacturer(), 
					 				   this.item.getDescription(), this.item.getAvailableDate()));*/
		
		this.catalogBean.addItem(this.item);
		
		/*this.catalogBean.getItems().stream().forEach(item ->{
			System.out.println(item.toString());
		});*/
		
		this.inventoryService.createItem(this.item.getItemId(), this.item.getName());
		
		return "list?faces-redirect=true";
	}
	
	public void init() {
		this.items = this.catalogBean.getItems();
	}
	
	public CatalogItem getItem() {
		return item;
	}

	public void setItem(CatalogItem item) {
		this.item = item;
	}

	public List<CatalogItem> getItems() {
		return items;
	}

	public void setItems(List<CatalogItem> items) {
		this.items = items;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
	
	

}
