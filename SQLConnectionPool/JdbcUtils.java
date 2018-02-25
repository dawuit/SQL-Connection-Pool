package Utils;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Properties;
import com.mysql.jdbc.Driver;

/**
 * 2018年2月20日14:01:17
 * @author 14746
 * @version 1.0
 */

public class JdbcUtils 
{
	/**数据库地址*/
	private final static String address;
	/**数据库用户名*/
	private final static String userName;
	/**数据库密码*/
	private final static String password;
	/**jdbc连接初始化*/
	static 
	{
		Reader fr = null;
		
		try 
		{
			//注册驱动
			Class.forName("com.mysql.jdbc.Driver");	
			//读取配置文件
			fr = new FileReader("./src/Utils/jdbc.properties");
			Properties pro = new Properties();
			pro.load(fr);
			address = pro.getProperty("address");
			userName = pro.getProperty("userName");
			password = pro.getProperty("password");
		} catch (ClassNotFoundException | IOException e) 
		{
			throw new ExceptionInInitializerError(e);
		}finally 
		{
			if(fr != null)
				try 
				{
					fr.close();
				} catch (IOException e) 
				{
					throw new ExceptionInInitializerError(e);
				}
		}
			
	}
	
	private JdbcUtils() {}
	
	
	
	/**
	 * 返回连接
	 * @return 数据库连接
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException
	{
		return DriverManager.getConnection(address, userName, password);
	}
	
	/**
	 * 关闭资源
	 * @param res
	 * @param st
	 * @param conn
	 */
	public static void free(ResultSet res, Statement st, Connection conn)
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
				try 
				{
					if(conn != null)
						conn.close();
				} catch (SQLException e) 
				{
						e.printStackTrace();
				}
			}
		}
	}
	
}

