package com.helloworld.jdbi;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;


public interface CampaignDAO {

	  @SqlQuery("SELECT term_desc FROM get_kli_children(88, 0)")
	  List<String> findTerms();
	  //String findNameById(@Bind("id") int id);
	}