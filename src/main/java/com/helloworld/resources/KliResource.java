package com.helloworld.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helloworld.core.EquationElement;
import com.helloworld.core.Kli;
import com.helloworld.core.RedisReturn;
import com.helloworld.jdbi.KliDAO;
import com.helloworld.jdbi.KliRedisDAO;
import com.yammer.dropwizard.jersey.params.IntParam;

import java.io.IOException;
import java.util.List;

@Path("/klis")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class KliResource {

	private final KliDAO kliDAO;
	private KliRedisDAO redis;

	public KliResource(KliDAO dao) throws IOException {
		this.kliDAO = dao;
		this.redis = new KliRedisDAO();
	}

	@GET
	public List<Kli> fetchAll(
			@QueryParam("count") @DefaultValue("20") IntParam count) {
		return kliDAO.findTerms(0);
	}

	@GET
	@Path("/{termcode}")
	public List<Kli> fetch(@PathParam("termcode") IntParam termCode,
			@QueryParam("count") @DefaultValue("20") IntParam count)
			throws Exception {

		List<Kli> klis = kliDAO.findTerms(termCode.get());
		
		for (Kli temp : klis) {
			Long value = this.redis.AndCount("userid", new Object[]{"kli:" + temp.getTermcode() + ":4"});
			temp.setAdicTotal(value);
			
			
			value = this.redis.AndCount("userid", new Object[] { "kli:" + temp.getTermcode() + ":4",
			"kli:8616208:4" });
			
			temp.setAdicOnline(value);
		}

		return klis;
	}

	@GET
	@Path("/list/{adic}")
	public List<String> listByAdic(@PathParam("adic") String adic) {

		return kliDAO.listByAdic(adic);

	}

	@GET
	@Path("/{termcode}/count")
	public String fetchCount(@PathParam("termcode") IntParam termCode,
			@QueryParam("count") @DefaultValue("20") IntParam count)
			throws IOException {

	//	IntegerReply value = this.redis.bitcount("kli:"
		//		+ termCode.get().toString() + ":1", null, null);

	//	return value.data().toString();
		return null;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/equation")
	public RedisReturn processEquation(EquationElement jsonEquation) throws JsonProcessingException {
		//ObjectMapper mapper = new ObjectMapper();
		
		
		//mapper.writeValueAsString(jsonEquation);
		
		EquationElement key = this.redis.Solve(jsonEquation, "");
		System.out.println("final key bitches:  " + key.getTermcode());
		Long value = this.redis.AndCount("userid", new Object[] { key.getTermcode() });
		
		RedisReturn returnObject = new RedisReturn(value, key.getTermcode());
		
		return returnObject;

	}
}
