import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Main {

	String outTopic[] = {"mqtt/nodeMCU1/control", "mqtt/nodeMCU2/control"};
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MQTT mqtt = new MQTT("tcp://211.46.213.232:1883", "test", "id", "password");
		mqtt.init();
		mqtt.setInTopic("mqtt/+/data/#");
		
		sleep(1000); 
		
		mqtt.subscribe(1);//qos¼³Á¤
			
		sleep(2000);
			
			
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


class MQTT implements MqttCallback{
	private static String Broker;
	private static String Client_ID;
	private static String UserName;
	private static String Passwd;
	private static MqttClient Client;
	private static MqttMessage message;
	private static MemoryPersistence persistence;
	private static MqttConnectOptions connOpts;
	private static String inTopic;
		
	public MQTT(String broker, String client_id,String username, String passwd){
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
	
	public void subscribe(int qos){
		try {
			Client.subscribe(inTopic,qos);
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getTopic(){
		return inTopic;
	}
	
	public void parshing(String targetSting) {
		String decs = targetSting;
		//System.out.println(decs);
//		System.out.println(inTopic);
//		
//		System.out.println(decs.charAt(0));
		
		if(decs.charAt(0) == 'm') {
			String nodeID = decs.split("messageID: ")[1].split(",")[0];
			String led1 = decs.split("led1_State: ")[1].split(",")[0];
			String led2 = decs.split("led2_State: ")[1].split(",")[0];
			System.out.println(decs.split("messageID: ")[1].split(",")[0]);
			System.out.println(decs.split("led1_State: ")[1].split(",")[0]);
			System.out.println(decs.split("led2_State: ")[1].split(",")[0]);
		}
		
	}
	
	
	@Override
	public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
    	//System.out.println("Message arrived : " + new String(mqttMessage.getPayload(), "UTF-8"));
		
		parshing(new String(mqttMessage.getPayload(), "UTF-8"));
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

