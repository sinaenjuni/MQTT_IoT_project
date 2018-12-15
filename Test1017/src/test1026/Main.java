package test1026;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Main {
	static Database database;
	static Mqtt mqtt;
	
	String outTopic[] = {"mqtt/nodeMCU1/control", "mqtt/nodeMCU2/control"};
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		mqtt = new Mqtt("tcp://118.34.118.75:1883", "databaseconnection");
		mqtt.init();
//		mqtt.setInTopic("mqtt/+/data/#");
		
//		database = new Database();
//		database.databaseConnect("root", "root", "jdbc:mysql://192.168.137.227:3306/mqtt");
		
//		String sql = "select * FROM nodemcu;";
//		database.selectData(sql);
		
//		mqtt.setConnection(database.getConnetion());
//		sleep(1000); 
		
		mqtt.subscribe("mqtt/+/data/#", 1);//qos¼³Á¤
			
//		sleep(2000);
			
		
	}

	static void sleep(int time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}




