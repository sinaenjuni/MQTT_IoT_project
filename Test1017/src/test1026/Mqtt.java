package test1026;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

class Mqtt implements MqttCallback{
	private static String Broker;
	private static String Client_ID;
	private static String UserName;
	private static String Passwd;
	private static MqttClient Client;
	private static MqttMessage message;
	private static MemoryPersistence persistence;
	private static MqttConnectOptions connOpts;
	private static String inTopic;
		
	private static Connection connection = null;
	
	public Mqtt(String broker, String client_id){
		this.Broker = broker;
		this.Client_ID = client_id;
	}
	
	public Mqtt(String broker, String client_id, String username, String passwd){
		this.Broker = broker;
		this.Client_ID = client_id;
		this.UserName = username;
		this.Passwd = passwd;
		
	}
	
	public void setInTopic(String inTopic) {
		this.inTopic = inTopic;
	}
	
	
	public void init(){
		
		this.persistence = new MemoryPersistence();
		try {
			Client = new MqttClient(this.Broker, this.Client_ID, this.persistence);
			Client.setCallback(this);
			
			connOpts = new MqttConnectOptions();
			//if(Client_ID!=null && Passwd != null){
//				connOpts.setUserName(this.UserName);
//				connOpts.setPassword(this.Passwd.toCharArray());
			//}
			connOpts.setCleanSession(true);
			System.out.println("Connecting to broker: "+this.Broker);
			
			Client.connect(connOpts);

			System.out.println("Connected");
			
			message = new MqttMessage();
			
		} catch(MqttException me) {
			System.out.println("reason "+me.getReasonCode());
			System.out.println("msg "+me.getMessage());
			System.out.println("loc "+me.getLocalizedMessage());
			System.out.println("cause "+me.getCause());
			System.out.println("excep "+me);
			me.printStackTrace();
		}
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void disconnect(){
		 try {
			Client.disconnect();
			Client.close();
		 } catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void publish(String outTopic, String msg, int qos){
		message.setQos(qos);
		message.setPayload(msg.getBytes());
		
		try {
			Client.publish(outTopic, message);
		} catch (MqttPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void subscribe(String inTopic, int qos){
		try {
			Client.subscribe(inTopic, qos);
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getTopic(){
		return inTopic;
	}
	
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	public void parshing(String targetSting) {
		String decs = targetSting;
		String nodeID = "";
		String led1 = "";
		String led2 = "";
		//System.out.println(decs);
//		System.out.println(inTopic);
//		
//		System.out.println(decs.charAt(0));
		
		if(decs.charAt(0) == '{') {
			nodeID = decs.split("messageID: ")[1].split(",")[0];
			led1 = decs.split("led1_State: ")[1].split(",")[0];
			led2 = decs.split("led2_State: ")[1].split(",")[0];
//			System.out.println(decs.split("messageID: ")[1].split(",")[0]);
//			System.out.println(decs.split("led1_State: ")[1].split(",")[0]);
//			System.out.println(decs.split("led2_State: ")[1].split(",")[0]);
		}
		
		if(connection != null) {
			PreparedStatement pstmt = null;
	    	
	    	String sql = "INSERT NODEMCU (NODEID, LED1, LED2) VALUES (?, ?, ?);";
	
			try {
				pstmt = connection.prepareStatement(sql);
				pstmt.setString(1, nodeID);
				pstmt.setString(2, led1);
				pstmt.setString(3, led2);
	
				pstmt.executeUpdate();
				pstmt.close();
			} catch (SQLException e) {
				System.out.println("SQL¹® ¿¡·¯ : " + e.toString());
			}
		}
		
	}
	
	
	@Override
	public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
    	//System.out.println("Message arrived : " + new String(mqttMessage.getPayload(), "UTF-8"));
		System.out.println(new String(mqttMessage.getPayload(), "UTF-8"));
//		parshing(new String(mqttMessage.getPayload(), "UTF-8"));
    }

	@Override
	public void connectionLost(Throwable cause) {
		 System.out.println("Lost Connection." + cause.getCause());	
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
		System.out.println("Message with " + iMqttDeliveryToken + " delivered.");
	}
	

}