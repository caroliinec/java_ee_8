package com.rest;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Response.Status;

import com.linkedin.InventoryItem;

@RequestScoped
@Path("/inventoryitems")
@Produces("application/json")
@Consumes("application/json")
public class InventoryItemEndpoint {
	
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	@POST
	public Response create(final InventoryItem inventoryitem) {
		//TODO: process the given inventoryitem 
		//you may want to use the following return statement, assuming that InventoryItem#getId() or a similar method 
		//would provide the identifier to retrieve the created InventoryItem resource:
		//return Response.created(UriBuilder.fromResource(InventoryItemEndpoint.class).path(String.valueOf(inventoryitem.getId())).build()).build();
		this.entityManager.persist(inventoryitem);
		return Response.created(UriBuilder.fromResource(InventoryItemEndpoint.class).path(String.valueOf(inventoryitem.getCatalogItemId())).build()).build();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	public Response findById(@PathParam("id") final Long id) {
		InventoryItem inventoryitem = this.entityManager.find(InventoryItem.class, id);
		
		if (inventoryitem == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		
		inventoryitem.setQuantity(ThreadLocalRandom.current().nextLong(1,100));
		return Response.ok(inventoryitem).build();
	}
	
	@GET
	@Path("/catalog/{catalogItemId}")
	public void asyncFindByCatalogId(@NotNull @PathParam("catalogItemId") Long catalogItemId,
			@Suspended AsyncResponse ar) { //option 2
		 new Thread() {
			 public void run() {
				 try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 ar.resume(findByCatalogId(catalogItemId));
			 }
		 }.start();
	}
	
	//@GET option 1/2
	//@Path("/catalog") option 1
	//public InventoryItem findByCatalogId(@NotNull @QueryParam("catalogItemId") Long catalogItemId) { option 1
	//@Path("/catalog/{catalogItemId}") //option 2
	public InventoryItem findByCatalogId(@NotNull @PathParam("catalogItemId") Long catalogItemId) { //option 2
		TypedQuery<InventoryItem> query = this.entityManager
				.createQuery("select i from InventoryItem i where i.catalogItemId = :catalogItemId", InventoryItem.class)
				.setParameter("catalogItemId", catalogItemId);
		
		InventoryItem item = query.getSingleResult();
		item.setQuantity(ThreadLocalRandom.current().nextLong(1,100));
		
		return item;
	}	
	
	@GET
	public List<InventoryItem> listAll(@QueryParam("start") final Integer startPosition, @QueryParam("max") final Integer maxResult) {
		TypedQuery<InventoryItem> query = this.entityManager.createQuery("select i from InventoryItem i", InventoryItem.class);
		final List<InventoryItem> inventoryitems = query.getResultList();
		return inventoryitems;
	}

	@Transactional
	@PUT
	@Path("/{id:[0-9][0-9]*}")
	public Response update(@PathParam("id") Long id, final InventoryItem inventoryitem) {
		this.entityManager.merge(inventoryitem);
		return Response.noContent().build();
	}

	@Transactional
	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") final Long id) {
		this.entityManager.remove(this.entityManager.find(InventoryItem.class, id));
		return Response.noContent().build();
	}

}
