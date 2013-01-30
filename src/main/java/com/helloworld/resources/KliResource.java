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

import redis.client.RedisClient;
import redis.reply.IntegerReply;




import com.helloworld.core.Kli;
import com.helloworld.jdbi.KliDAO;
import com.yammer.dropwizard.jersey.params.IntParam;
import com.yammer.dropwizard.jersey.params.LongParam;

import java.io.IOException;
import java.util.List;

@Path("/klis")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class KliResource {

    private final KliDAO kliDAO;

    public KliResource(KliDAO dao) {
        this.kliDAO = dao;
    }

    
    @GET
    public List<Kli>  fetchAll(@QueryParam("count") @DefaultValue("20") IntParam count){
        return kliDAO.findTerms(0);
    }
    
    @GET
    @Path("/{termcode}")
    public List<Kli>  fetch(@PathParam("termcode") IntParam termCode, @QueryParam("count") @DefaultValue("20") IntParam count){
        return kliDAO.findTerms(termCode.get());
    }

    @GET
    @Path("/{termcode}/count")
    public String fetchCount(@PathParam("termcode") IntParam termCode, @QueryParam("count") @DefaultValue("20") IntParam count) throws IOException{
    	//Jedis jedis = new Jedis("localhost");
		//jedis.connect();
		
    	 RedisClient redis = new RedisClient("127.0.0.1", 6379);
    	 
    	 IntegerReply value = redis.bitcount("kli:" + termCode.get().toString() + ":1", null, null);
    	
		
    	return value.data().toString();
    }
}
