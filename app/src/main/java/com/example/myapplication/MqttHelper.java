package com.example.myapplication;

import android.content.Context;
import android.util.Log;

import info.mqtt.android.service.Ack;
import info.mqtt.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttHelper {
    MqttAndroidClient client;
    private Context context;
    String serverURI;
    public MqttHelper(Context context){
        this.context = context;
    }
    public IMqttToken connect(String serverURI){
        this.serverURI = serverURI;
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(context.getApplicationContext(),"tcp://broker.hivemq.com:1883",clientId, Ack.AUTO_ACK);
        return client.connect();
    }


    public void send(String payloadStr, String topicStr){
        String topic = topicStr;
        String payload = payloadStr;
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            client.publish(topic, message, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // Message published successfully
                    Log.d("Message Sent","success");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Failed to publish the message
                    Log.d("Message Sent","failed");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void subscribe(){
        String topic = "test/1";
        try {
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });
            IMqttToken subToken = client.subscribe(topic,1);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // The message was published
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // The subscription could not be performed, maybe the user was not
                    // authorized to subscribe on the specified topic e.g. using wildcards

                }
            });
        }catch (Exception e){

        }
    }
}
