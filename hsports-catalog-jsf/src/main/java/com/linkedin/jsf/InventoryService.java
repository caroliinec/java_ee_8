package com.linkedin.jsf;

import java.io.Serializable;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;

public interface InventoryService extends Serializable {

	public void createItem(Long catalogItemId, String name);
	
	public Long getQuantity(Long catalogItemId);	
	
	public Future<InventoryItem> asyncGetQuantity(Long catalogItemId);
	
	public CompletionStage<InventoryItem> reactiveGetQuantity(Long catalogItemId);
	
}
