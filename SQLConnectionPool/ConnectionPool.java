package Utils;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Properties;

/**
 * 2018年2月25日11:39:50
 * 连接池
 * @author 14746
 *
 */
public class ConnectionPool
{	
	/**连接池*/
	private LinkedList<Connection> connPool = new LinkedList<Connection>();
	/**最大连接数*/
	private static final int maxCount = 300;
	/**当前连接数*/
	private int currCount = 0;
	/**
	 * 创建大小为size的连接池
	 * @param size
	 */
	public ConnectionPool(int size)
	{
		if(size <= 0)
			size = 1;
		if(size > maxCount)
			size = maxCount;
		for (int i = 0; i < size; i++) 
		{
			try {
				connPool.add(JdbcUtils.getConnection());
				++currCount;
			} catch (SQLException e) {
				System.out.println("创建连接池大小:" + i);
				e.printStackTrace();
			}	
		}
	}
	
	/**
	 * 从连接池中获取链接，如果连接池为空但连接数不超过最大连接数（maxCount）则创建新的连接
	 * 并返回
	 * @return
	 */
	public synchronized Connection getConnection()
	{
		if(connPool.size() > 0)
		{
			return connPool.pollFirst();
		}
		if(currCount < maxCount)
		{
			Connection conn = null;
			try 
			{
				 conn = JdbcUtils.getConnection();
			} catch (SQLException e) 
			{
				e.printStackTrace();
			}
			++currCount;
			return conn;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * 释放连接重新加入连接池
	 * @param res
	 * @param st
	 * @param conn
	 */
	public synchronized void free(ResultSet res, Statement st, Connection conn)
	{
		
		try 
		{
			if(res != null)
				res.close();
		} catch (SQLException e) 
		{
				e.printStackTrace();
		}
		finally 
		{
			
			try 
			{
				if(st != null)
					st.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			finally 
			{		
				if(conn != null)
				{
					connPool.offerLast(conn);
				}
			}
		}
	}
	
	/**
	 * 关闭连接池中全部的连接
	 */
	public synchronized void closeAll()
	{
		Connection conn = null;
		while((conn = connPool.pollFirst()) != null)
		{
			try 
			{
				conn.close();
				--currCount;
			} catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
	}
}

 