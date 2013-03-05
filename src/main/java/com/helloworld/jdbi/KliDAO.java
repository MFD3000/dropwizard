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

	 
	 @SqlQuery("SELECT distinct termcode as term_code, parent_termcode as parent_term_code, term_desc FROM sgmt_kli_adic ska JOIN kli_collection kc ON ska.kli = kc.termcode where ska.adic = :adic ")
	 @Mapper(KliMapper.class)
	 List<Kli> listByAdic(@Bind("adic") String adic);
	 
	 @SqlQuery("SELECT id from sgmt_adic_int_map where adic = :adic ")
	 Long adicIndex(@Bind("adic") String adic);
	 
	 @SqlQuery("SELECT adic " +
	 		"FROM sgmt_customer " +
	 		"WHERE ucic = :ucic " +
	 		"and p_id = :pid")
	 String getAdicByUcicAndPartner(@Bind("ucic") String ucic, @Bind("pid") String pid);
	 
	 @SqlQuery("SELECT distinct term_desc FROM sgmt_kli_adic ska JOIN kli_collection kc ON ska.kli = kc.termcode where ska.adic = :adic ")
	 List<String> listDescriptionsByAdic(@Bind("adic") String adic);
	
}

