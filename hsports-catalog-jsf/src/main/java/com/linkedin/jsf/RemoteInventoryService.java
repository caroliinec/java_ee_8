package com.linkedin.jsf;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

@ApplicationScoped
//@Named("remoteInventoryService") - Option 1
@RemoteService //Option 2
//@Alternative
public class RemoteInventoryService implements InventoryService {

	//private Map<Long, InventoryItem> items = new HashMap<Long, InventoryItem>();

	private String apiUrl = "http://localhost:8081/hsports-catalog-jax/hsports/api/";
	
	@Override
	public void createItem(Long catalogItemId, String name) {
		/*long inventoryItemId = items.size() + 1;
		this.items.put(inventoryItemId, new InventoryItem(inventoryItemId, catalogItemId, name, 0L));
		this.printInventory();*/
		Client client = ClientBuilder.newClient();
		Response response = client.target(apiUrl)
				.path("inventoryitems")
				.request().post(Entity.json(new InventoryItem(null, catalogItemId, name, (long) new Random().nextInt(10))));
		System.out.println(response.getStatus());
		System.out.println(response.getLocation());
	}

	/*public void printInventory() {
		System.out.println("Remote inventory contains:");
		for (Entry<Long, InventoryItem> entry : this.items.entrySet()) {
			System.out.println(entry.getValue().getName());
		}
	}*/

	@Override
	public Long getQuantity(Long catalogItemId) {
		Client client = ClientBuilder.newClient();
		InventoryItem inventoryItem = client.target(apiUrl)
				 .path("inventoryitems").path("catalog")
				 //.queryParam("catalogItemId", catalogItemId.toString()) option 1
				 .path("{catalogItemId}").resolveTemplate("catalogItemId", catalogItemId.toString())
				 .request().get(InventoryItem.class);		
		return inventoryItem.getQuantity();
	}

	@Override
	public Future<InventoryItem> asyncGetQuantity(Long catalogItemId) {
		Client client = ClientBuilder.newClient();
		return client.target(apiUrl)
				 .path("inventoryitems").path("catalog")
				 //.queryParam("catalogItemId", catalogItemId.toString()) option 1
				 .path("{catalogItemId}").resolveTemplate("catalogItemId", catalogItemId.toString())
				 .request().async().get(InventoryItem.class);	
	}

	@Override
	public CompletionStage<InventoryItem> reactiveGetQuantity(Long catalogItemId) {
		Client client = ClientBuilder.newClient();
		return client.target(apiUrl)
				 .path("inventoryitems").path("catalog")
				 //.queryParam("catalogItemId", catalogItemId.toString()) option 1
				 .path("{catalogItemId}").resolveTemplate("catalogItemId", catalogItemId.toString())
				 .request().rx().get(InventoryItem.class);	
	}

}