package com.helloworld.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helloworld.core.Banner;
import com.helloworld.core.EquationElement;
import com.helloworld.core.EquationRequest;
import com.helloworld.core.Kli;
import com.helloworld.core.RedisReturn;
import com.helloworld.jdbi.KliDAO;
import com.helloworld.jdbi.KliRedisDAO;
import com.helloworld.views.BannerView;
import com.yammer.dropwizard.jersey.params.IntParam;
import com.yammer.dropwizard.views.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			Long value = this.redis.AndCount("userid", new Object[]{"kli:" + temp.getTermcode() });
			temp.setAdicTotal(value);


			value = this.redis.AndCount("userid", new Object[] { "kli:" + temp.getTermcode() ,
			"kli:8616208" });

			temp.setAdicOnline(value);
		}

		return klis;
	}

	@GET
	@Path("/list/{adic}")
	public List<Kli> listByAdic(@PathParam("adic") String adic) {

		List<Kli> klis = kliDAO.listByAdic(adic);

		for (Kli temp : klis) {
			Long value = this.redis.AndCount("userid", new Object[]{"kli:" + temp.getTermcode() });
			temp.setAdicTotal(value);


			value = this.redis.AndCount("userid", new Object[] { "kli:" + temp.getTermcode() ,
			"kli:8616208" });

			temp.setAdicOnline(value);
		}

		klis = this.gatherChildren(klis);

		return klis;
	}
	public List<Kli> gatherChildren(List<Kli> klis){

		for(int i = 0; i < klis.size(); i++) {
			klis = this.addKlis(i, klis)	;
			
		}

		List<Kli> remove = new ArrayList<Kli>();
		for(Kli parent : klis) {
			for(Kli temp : klis) {
				if (temp.getParentTermCode()==parent.getTermcode()&&klis.contains(temp)){
					remove.add(temp);
				}
			}

		}
		
		klis.removeAll(remove);
		
		return klis;
	}

	public List<Kli> addKlis(int index, List<Kli> klis){

		Kli parent = klis.get(index);
		for(Kli temp : klis) {


			if (temp.getParentTermCode()==parent.getTermcode()&&klis.contains(temp)){

				klis = this.addKlis(klis.indexOf(temp), klis);
				if(!parent.getChildren().contains(temp)){
					parent.getChildren().add(temp);
				}

			}

		}

		return klis;
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
	@Path("/audience")
	public RedisReturn createTargetAudience(EquationRequest request) throws IOException {
		
		String bannerRoot = "banner:" + request.getBannerid();
		ObjectMapper mapper = new ObjectMapper();

		//mapper.writeValueAsString(jsonEquation);

		EquationElement key = this.redis.Solve(request.getEquation(), "");
		
		
		String newBannerAdicsKey = bannerRoot + ":audience";
		System.out.println("termcode : " + key.getTermcode());
		
		if( this.redis.GetValue(key.getTermcode()) != null ){
		this.redis.SetValue(newBannerAdicsKey, this.redis.GetValue(key.getTermcode()));
		
		this.redis.SetHashValue(bannerRoot + ":content");
		
		//update zone with new banner key root
		String zoneKey = "zone:" + request.getZoneid();
		this.redis.AddBannerToZone(zoneKey, bannerRoot);
		}
		Long value = this.redis.Count(newBannerAdicsKey);

		RedisReturn returnObject = new RedisReturn(value, newBannerAdicsKey);
		//RedisReturn returnObject = new RedisReturn(value, bannerRoot);

		return returnObject;

	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/decision/{zoneid}/{adic}")
	public List<String> deliver(@PathParam("adic") String adic, @PathParam("zoneid")int zoneId){
		String zoneKey = "zone:" + zoneId;
		
		//List<String> banners = this.redis.getBannersByZone(zoneKey);
		
		Long adicOffset = this.kliDAO.adicIndex(adic);
		
		List<String> targetedBanners = this.redis.getTargetedBanners(adicOffset, zoneKey);
		return targetedBanners;

	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_HTML)
	@Path("/banner/{zoneid}/{adic}")
	public BannerView deliverBanner(@PathParam("adic") String adic, @PathParam("zoneid")int zoneId){
		String zoneKey = "zone:" + zoneId;
		
		//List<String> banners = this.redis.getBannersByZone(zoneKey);
		
		Long adicOffset = this.kliDAO.adicIndex(adic);
		
		List<String> targetedBanners = this.redis.getTargetedBanners(adicOffset, zoneKey);
		Banner banner = new Banner();
		banner.setFilename("test");
		banner.setId(3);
		return new BannerView(banner);

	}
}
