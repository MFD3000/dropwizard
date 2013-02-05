package com.helloworld.jdbi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.helloworld.core.EquationElement;
import com.helloworld.core.Kli;

import redis.client.RedisClient;
import redis.reply.IntegerReply;

public class KliRedisDAO {
	
	private RedisClient redis;
	
	public KliRedisDAO() throws IOException {
		
		this.redis = new RedisClient("127.0.0.1", 6379);
		
	}
	
	public Long AndCount(String resultKey, Object[] keys ){
	Long count = 0L;
	System.out.println(resultKey);
	
	this.redis.bitop("and", resultKey, keys);
	count = this.redis.bitcount(resultKey, null, null).data();
	//this.redis.del(new Object[]{resultKey});

	return count;
	}

	public Long OrCount(String resultKey, Object[] keys ){
	Long count = 0L;
	System.out.println(resultKey);
	
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
}
