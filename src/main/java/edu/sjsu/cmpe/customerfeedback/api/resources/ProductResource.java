/**
 * 
 */
package edu.sjsu.cmpe.customerfeedback.api.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.yammer.metrics.annotation.Timed;

import edu.sjsu.cmpe.customerfeedback.domain.Product;
import edu.sjsu.cmpe.customerfeedback.dto.LinkDto;
import edu.sjsu.cmpe.customerfeedback.dto.LinksDto;
import edu.sjsu.cmpe.customerfeedback.dto.ProductDto;
import edu.sjsu.cmpe.customerfeedback.repository.ProductRepositoryInterface;

/**
 * @author Rajiv
 *
 */

@Path("owners/{ownerId}/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class ProductResource {
	private final ProductRepositoryInterface productRepository;
	/**
	 * 
	 */
	public ProductResource(ProductRepositoryInterface productRepository) {
		this.productRepository = productRepository;
	}

	@POST
	@Path("products")
	@Timed(name = "create-product")
	public Response createProduct(@PathParam("ownerId") int ownerId,Product request){
		Product savedProduct = productRepository.saveProduct(request);
		savedProduct.setOwnerId(ownerId);
		int productId = savedProduct.getProductId();
		LinksDto links = new LinksDto();
		links.addLink(new LinkDto("view-product", "/owners/"+ownerId+"/products/"+productId, "GET"));
		return Response.status(201).entity(links).build();
	}
	
	@GET
	@Path("products")
	@Timed(name = "view-products-by-owner")
	public Response viewProductsOfOwner(@PathParam("ownerId") int ownerId) {
		return Response.ok().build();
	}
	
	@GET
	@Path("products/{productId}")
	@Timed(name = "view-product-by-owner")
	public Response viewProductByOwner(@PathParam("ownerId") int ownerId, @PathParam("productId") int productId) {
		Product product = productRepository.getProductbyProductId(productId);
		ProductDto links = new ProductDto(product);
		links.addLink(new LinkDto("view-reviews", "/owners/"+ownerId+"/products/"+productId+"/reviews", "GET"));
		
		return Response.ok().entity(links).build();
	}
	
	@PUT
	@Path("products/{productId}")
	@Timed(name = "set-reviewable")
	public Response setReviewable(@PathParam("productId") int productId, @PathParam("ownerId") int ownerId, @QueryParam("canReview") boolean value) {
		Product product = productRepository.getProductbyProductId(productId);
		product.setCanReview(value);
		LinksDto links = new LinksDto();
		links.addLink(new LinkDto("view-product-by-owner", "/owners/"+ownerId+"products"+productId , "GET"));
		return Response.ok().entity(links).build();
	}
	
	/*@GET
	@Path("/products/{productId}")
	@Timed(name = "view-product")
	public Response viewProduct(@PathParam("ownerId") int ownerId, @PathParam("productId") int productId) {
		Product product = productRepository.getProductbyProductId(productId);
		ProductDto links = new ProductDto(product);
		links.addLink(new LinkDto("view-reviews", "/owners/"+ownerId+"products"+productId+"reviews", "GET"));
		
		return Response.ok().entity(links).build();
	}
	*/
	

}