import java.sql.*;
import java.lang.*;

public class Main {
	static DbToMqtt dbToMqtt;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		DbToMqtt dbToMqtt;
		dbToMqtt = new DbToMqtt();
		
		dbToMqtt.databaseConnect("root", "root", "jdbc:mysql://192.168.137.227:3306/mqtt");
		
		String sql = "select * FROM nodemcu;";
		dbToMqtt.selectData(sql);
		
		
		
		
//	    Thread thread = new Thread(new myThread());
//	    thread.start();

//	    dbToMqtt.disconnect();
	}
	
	
	static class myThread implements Runnable {


		@Override
		public void run() {
			int timestamp = 0;

			while (true) {
				
				int a = 0;
				dbToMqtt.insertData("node" + ++a, "12321", "123123");
				
				// Delay
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}

		}
	}

}
