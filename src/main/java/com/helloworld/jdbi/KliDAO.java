package com.helloworld.jdbi;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import com.helloworld.core.Kli;
import com.helloworld.core.KliMapper;


public interface KliDAO {

	 @SqlQuery("SELECT term_code, term_desc, parent_term_code FROM get_kli_children(4, :termcode)")
	 @Mapper(KliMapper.class)
	 List<Kli> findTerms(@Bind("termcode") int termcode);
	
	 
	 //@SqlQuery("select id, name from something where id = :id")
	  
	  //Something findById(@Bind("id") int id);
	 
	 
	 
	
}

