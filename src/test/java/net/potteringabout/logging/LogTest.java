/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.potteringabout.logging;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.log4j.MDC;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author tone
 */
public class LogTest {

  private Connection getConnection() throws SQLException {
    try {
      InitialContext context = new InitialContext();
      DataSource ds = (DataSource) context.lookup("testDS");
      return ds.getConnection();
    } catch (NamingException e) {
      throw new RuntimeException("Cannot find JNDI DataSource: testDS", e);
    }

  }

  @Test
  public void logTest() throws Exception {
    Logger LOGGER = Logger.getLogger("JNDI");
    MDC.put("pie", "p1");
    LOGGER.info("Testing");
    
    String f = null;
    try{
      f.getBytes();
    }
    catch(Exception e){
      LOGGER.error("NULLPOINTER", e);
    }
    Statement st = null;
    try {
      Connection connection = getConnection();
      st = connection.createStatement();
      ResultSet rs = st.executeQuery("select * from LOGS where pie = 'p1' and message = 'Testing'");
      while (rs.next()) {
        String pie = rs.getString("PIE");
        String message = rs.getString("MESSAGE");
        Assert.assertEquals("Testing", message);
        Assert.assertEquals("p1", pie);
        return;
      }
      Assert.fail();

    } finally {
      if (st != null) {
        st.close();
      }
    }

  }

}
