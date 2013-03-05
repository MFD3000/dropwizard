package com.helloworld.jdbi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.helloworld.core.Banner;
import com.helloworld.core.EquationElement;
import com.helloworld.core.Kli;

import redis.client.RedisClient;
import redis.reply.IntegerReply;
import redis.reply.MultiBulkReply;

import java.nio.charset.Charset;

public class KliRedisDAO {

	private RedisClient redis;

	public KliRedisDAO() throws IOException {

		this.redis = new RedisClient("127.0.0.1", 6379);

	}
	
	public String GetValue(String key){
		return this.redis.get(key).asString(Charset.defaultCharset());
		
	}
	
	public String SetValue(String key, String value){
		
		return this.redis.set(key, value).toString();
		
	}
	
	public Long Count(String key){
		return this.redis.bitcount(key, null, null).data();
		
	}
	public String SetHashValue(String key){
		return this.redis.hset(key, "name", "banner 1").toString();
		
	}
	
	public void AddBannerToZone(String zoneKey, String bannerKey){
		Object[] args = new Object[]{zoneKey,0, bannerKey}; 
		this.redis.zadd(args);
		
	}
	

	public Long AndCount(String resultKey, Object[] keys ){
		Long count = 0L;
	

		this.redis.bitop("and", resultKey, keys);
		count = this.redis.bitcount(resultKey, null, null).data();
		//this.redis.del(new Object[]{resultKey});
		
		return count;
	}

	public Long OrCount(String resultKey, Object[] keys ){
		Long count = 0L;
		

		this.redis.bitop("or", resultKey, keys);
		count = this.redis.bitcount(resultKey, null, null).data();
		//this.redis.del(new Object[]{resultKey});

		return count;
	}

	public IntegerReply XorCount(Object[] keys ){

		this.redis.bitop("xor", "user_id:xor", keys);
		return this.redis.bitcount("user_id:xor", null, null);

	}

	public IntegerReply NotCount(Object[] key ){
		//need check to make sure no more than one key
		this.redis.bitop("not", "user_id:not", key);
		return this.redis.bitcount("user_id:not", null, null);

	}

	public EquationElement Solve(EquationElement eq, String resultKey){

		//List<EquationElement> keys = new ArrayList<EquationElement>();
		List<String> keys = new ArrayList<String>();

		for (EquationElement temp : eq.getMembers()) {

			if (temp.getOperation().toLowerCase().equals("element")){

				//if(eq.getMembers().indexOf(temp)==0){
				//	resultKey += temp.getTermcode();
				//}else{
				resultKey +=  temp.getTermcode() +":";
				//}

				//System.out.println("kli + " + temp.getTermcode());
				//System.out.println("key " + resultKey);	


				keys.add(temp.getTermcode());
				System.out.println("element added key   " + temp.getTermcode());
			}
			else if (temp.getOperation().toLowerCase().equals("and")||temp.getOperation().toLowerCase().equals("or")){
				EquationElement solution = this.Solve(temp, temp.getOperation() + ":");
				resultKey +=  solution.getTermcode();
				keys.add(solution.getTermcode());
				System.out.println("operator added key   " + solution.getTermcode());

			}
		}

		Object[] keysObject = keys.toArray(); 

		if(keysObject.length > 0){
			System.out.println("key object count  " + keysObject.length);
			System.out.println("key " + resultKey);
			this.redis.bitop(eq.getOperation(), resultKey, keysObject);
		}
		EquationElement returnEquationElement = new EquationElement("element", resultKey, null);
		return returnEquationElement;

	}

	public List<String> getBannersByZone(String zoneKey){

		MultiBulkReply bannerReply = this.redis.zrangebyscore(zoneKey, 0, 1, null, new Object[]{} );

		List<String> banners = bannerReply.asStringList(Charset.defaultCharset() );
		for (String bannerId : banners) {
			System.out.println(banners.size()); 

		}
	
		return banners;

	}

	public Map<String, String> GetBannerContent(Long bannerId){
		 //targetedBanner = this.redis.hgetall(targetedBanners.get(0)+":content").asStringMap(Charset.defaultCharset());
		return null;
		
		
	}
	public List<String> getTargetedBanners(Long adicOffset,String zoneKey){

		//MultiBulkReply bannerReply = this.redis.zrange(key0, start1, stop2, withscores3)(zoneKey, 0, 1, null, new Object[]{} );
		MultiBulkReply bannerReply = this.redis.zrange(zoneKey, 0, -1, null);

		List<String> banners = bannerReply.asStringList(Charset.defaultCharset() );
		List<String> targetedBanners =  new ArrayList<String>();

		for (String bannerId : banners) {
			String audience = bannerId + ":audience";
			
			if(this.redis.getbit(audience, adicOffset).data().intValue()==1){
				targetedBanners.add(bannerId);
				;
				//System.out.println(bannerId + " Audience " + this.redis.getbit(audience, adicOffset).data().intValue());     
			}
		}
		if(targetedBanners.size()>0){
			///System.out.println("Adic: " + adicOffset + " Targeted Banner Count:" + targetedBanners.size());
			//targetedBanners = this.redis.hgetall(targetedBanners.get(0)+":content").asStringList(Charset.defaultCharset() );
			
		}
		
		return targetedBanners; 

	}








}
