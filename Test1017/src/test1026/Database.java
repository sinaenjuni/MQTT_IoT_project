package test1026;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class Database {
	private static Connection connection = null;
	private static Statement st = null;
	private static String NodeID = null, LED1 = null, LED2 = null, ID = null;
    
    public void databaseConnect(String id, String passwd, String url) {
    	try {
    		
    		
	    	Class.forName("com.mysql.jdbc.Driver"); // SQL 드라이버 로드
			connection = DriverManager.getConnection(url, id, passwd);
			System.out.println("Connecting to database: " + url);
			st = connection.createStatement();
			
			System.out.println("Connected");
		} catch (ClassNotFoundException cnfe) {
		  System.out.println("DB 드라이버 로딩에 실패 :" + cnfe.toString());
		} catch (SQLException e) {
		   System.out.println("DB 접속 실패 :" + e.toString());
		}catch (Exception ex) {
		    System.out.println("원인을 알 수 없는 에러가 발생 했습니다.");
		    ex.printStackTrace();
		}
    	
    }
    
    public void disconnect() {
    	try {
			
			st.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }

    public void selectData(String sql) {
    	String tmpsql = sql;
    	 try {

    		ResultSet rs = st.executeQuery(tmpsql);

		    while (rs.next()) {
		    	ID = rs.getString("ID");
		        NodeID = rs.getString("NodeID");
		        LED1 = rs.getString("LED1");
		        LED2 = rs.getString("LED2");
		        System.out.println(ID + ", " + NodeID +", "+ LED1 +", "+ LED2);
		    }
		    
//		    rs.close();
			   
		} catch (SQLException se) {
		    System.out.println("DB 접속 실패 :" + se.toString());
		} catch (Exception ex) {
		    System.out.println("원인을 알 수 없는 에러가 발생 했습니다.");
		    ex.printStackTrace();
		}
    	
    }
    
    public Connection getConnetion() {
    	
    	return connection;
    }
    
    public void insertData(String nodeid, String data1, String data2) {

    	PreparedStatement pstmt = null;
    	
    	String sql = "INSERT NODEMCU (NODEID, LED1, LED2) VALUES (?, ?, ?);";

		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, nodeid);
			pstmt.setString(2, data1);
			pstmt.setString(3, data2);

			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			System.out.println("SQL문 에러 : " + e.toString());
		}

    }
    
}


/*
 * //Auto_Icrement 초기화 루틴 try { String reset_sql =
 * "ALTER TABLE NODEMCU AUTO_INCREMENT=1"; st.executeUpdate(reset_sql);
 * reset_sql = "SET @COUNT = 0"; st.executeUpdate(reset_sql); reset_sql =
 * "UPDATE NODEMCU SET ID = @COUNT := @COUNT+1"; st.executeUpdate(reset_sql);
 * reset_sql = "DELETE NODEMCU where ID > 1"; st.executeUpdate(reset_sql); }
 * catch (SQLException e) { System.out.println("SQL문 에러 : "+ e.toString()); }
 */

// 데이터 계속 집어 넣는다