package com.linkedin;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="CATALOG_ITEM")
public class CatalogItem {

	@Id //primary key
	@Column(name="CATALOG_ITEM_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long itemId;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(joinColumns = @JoinColumn(name="CATALOG_ITEM_ID"),
			   inverseJoinColumns = @JoinColumn(name="ITEM_MANAGER_ID"))
	private List<ItemManager> itemManagers = new ArrayList<>();
	
	@Column(name="NAME")
	private String name;

	@Column(name="MANUFACTURER")
	private String manufacturer;

	@Column(name="DESCRIPTION")
	private String description;

	@Column(name="AVAILABLE_DATE")
	private LocalDate availableDate;

	public CatalogItem() {
		
	}
	
	public CatalogItem(String name, String manufacturer, String description, LocalDate availableDate) {
		super();
		this.name = name;
		this.manufacturer = manufacturer;
		this.description = description;
		this.availableDate = availableDate;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getAvailableDate() {
		return availableDate;
	}

	public void setAvailableDate(LocalDate availableDate) {
		this.availableDate = availableDate;
	}

	@Override
	public String toString() {
		return "CatalogItem [itemId=" + itemId + ", name=" + name + ", manufacturer=" + manufacturer + ", description="
				+ description + ", availableDate=" + availableDate + "]";
	}

	public List<ItemManager> getItemManagers() {
		return itemManagers;
	}

	public void setItemManagers(List<ItemManager> itemManagers) {
		this.itemManagers = itemManagers;
	}

}
