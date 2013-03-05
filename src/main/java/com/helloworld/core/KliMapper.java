package com.helloworld.core;

import java.awt.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class KliMapper implements ResultSetMapper<Kli>
{
  public Kli map(int index, ResultSet r, StatementContext ctx) throws SQLException
  {
	ArrayList<Kli> emptyChildren = new ArrayList<Kli>();
    return new Kli(r.getInt("term_code"), r.getString("term_desc"), r.getInt("parent_term_code"), emptyChildren );
  }
}
