/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.potteringabout.logging;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.log4j.jdbc.JDBCAppender;

/**
 *
 * @author tone
 */
public class JNDIDatabaseAppender extends JDBCAppender {

  private String jndiDataSource;

  /**
   * @return the jndiDataSource
   */
  public String getJndiDataSource() {
    return jndiDataSource;
  }

  /**
   * @param jndiDataSource the jndiDataSource to set
   */
  public void setJndiDataSource(String jndiDataSource) {
    this.jndiDataSource = jndiDataSource;
  }

  @Override
  protected Connection getConnection() throws SQLException {
    if (jndiDataSource == null) {
      return super.getConnection();
    } else {
      try { 
        InitialContext context = new InitialContext();
        DataSource ds = (DataSource) context.lookup(jndiDataSource);
        return ds.getConnection();
      } catch (NamingException e) {
        throw new RuntimeException("Cannot find JNDI DataSource: " + jndiDataSource, e);
      }
    }
  }

  @Override
  protected void closeConnection(Connection con) {
    if (jndiDataSource == null) {
      super.closeConnection(con);
    } else {
      try {
        if (con != null && !con.isClosed()) {
          con.close();
        }
      } catch (SQLException e) {
      }
    }
  }

}
