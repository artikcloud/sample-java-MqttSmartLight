/*
 * Copyright (C) 2017 Samsung Electronics Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cloud.artik.example;

import java.lang.reflect.Type;
import java.util.List;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import cloud.artik.mqtt.*;

public class MqttSmartLight 
{
    // Device of "Example Simple Smart Light" (unique name cloud.artik.example.simple_smartlight)
    static private String deviceId     = null;
    static private String deviceToken  = null;
   
    static private final int EXPECTED_ARGUMENT_NUMBER = 4;

    public static void main( String[] args ) {
        if (!succeedParseCommand(args)) {
            printUsage();
             return;
        }
        MqttSmartLight mqtt = new MqttSmartLight();
        mqtt.startSession();
    }
    
    // More Private instance variables
    private final int PUBLISH_QOS             = 2;
    private MqttSession mqttSession           = null;
    private boolean endSession                = false;
    
    private void startSession() {
        ArtikCloudMqttCallback mqttCallback = new ArtikCloudMqttCallback() {
    
            @Override
            public void onFailure(OperationMode opMode, IMqttToken mqttToken, Throwable throwable) {
                System.out.println("Failed " + opMode + "; reason: " + throwable.toString());
                switch (opMode) {
                case CONNECT:
                    System.exit(0);
                default:
                    break;
                }
                
            }

            @Override
            public void onSuccess(OperationMode opMode, IMqttToken mqttToken) {
                System.out.println("Succeeded: " + opMode);        
                switch (opMode) {
                case CONNECT:
                    System.out.println("\nSubscribing to topic: " + mqttSession.getSubscribeTopic() + "......" );
                    try {
                         mqttSession.subscribe();
                    } catch (ArtikCloudMqttException e) {
                        e.printStackTrace();
                    } 
                    break;
                case DISCONNECT:
                    endSession = true;
                    break;
                case PUBLISH:
                    System.out.println("Published to topic: " + mqttToken.getTopics()[0]);
                    break;
                case SUBSCRIB: 
                    System.out.println("Subscribed to topic: " + mqttToken.getTopics()[0]);
                    break;
                    
                default:
                    System.out.println("WARNING: received unexpected MQTT operation mode: " + opMode);
                    break;
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("\nConnection is lost due to " + cause);
                endSession = true;
            }
    
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                System.out.println("\nReceived message. Payload: " + new String(message.getPayload()) + "; Topic:" + topic + "; Qos:" + message.getQos());
                handleAction(new String(message.getPayload()));
            }
    
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                System.out.println("Complete message delivery.");
            }
        };
    
        System.out.println("Start mqtt session....");
    
        try {
            mqttSession = new MqttSession(deviceId, deviceToken, mqttCallback);
            System.out.println("Connecting to broker: "+ mqttSession.getBrokerUri() + "......");
            mqttSession.connect();
        } catch (ArtikCloudMqttException e) {
            e.printStackTrace();
        }
            
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if (mqttSession == null || !mqttSession.isConnected()) {
                    System.out.println("\nExit.");
                    return;
                }
                try {
                    System.out.println("\nPrepare shutting down. Disconnecting... Please wait...");
                    mqttSession.disconnect();
                    Thread.sleep(2000); //wait for disconnection to finish
                    
                } catch (ArtikCloudMqttException|InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        
        while (!endSession) {
            // session (sub&pub) is going on
        }

     }

    /* Examples of received actions:
     * 
     * {"actions":[{"name":"setOn"}]}
     * 
     * {"actions":[{"name":"setOff"}]}
     */
    private void handleAction(String receivedActions) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jo = (JsonObject)jsonParser.parse(receivedActions);

        JsonArray jsonArr = jo.getAsJsonArray("actions");

        Gson gson = new Gson();
        Type listType = new TypeToken<List<SimpleAction>>() {}.getType();
        List<SimpleAction> actionList = gson.fromJson(jsonArr, listType);
        
        if (actionList.size() > 0) {
            SimpleAction action = actionList.get(0);
            System.out.println("Handle Action: " + action.name);
           
            switch (action.name) {
            case "setOn":
                updateState(true);
                break;
            case "setOff":
                updateState(false);
                break;
            default:
                System.err.println("Unexpected Action:" + action.name);
                break;
            }
        }
    }
    
    void updateState(boolean state) {
        String payload    =  "{\"state\":" + state +"}";
        System.out.println("Set State to " + state);
        System.out.println("Publishing to topic: " + mqttSession.getPublishTopic() + "; message payload: " + payload + "; QoS:" + PUBLISH_QOS + ".....");
        try {
            mqttSession.publish(PUBLISH_QOS, payload);
        } catch (ArtikCloudMqttException e) {
            e.printStackTrace();
        }

    }
    
    class SimpleAction {
        String name;
        String operation;
    }
    
    ////////////////////////////////////////////
    // Helper functions
    //
    //
    // java -jar target/smartlight-mqtt-subpub.jar -d DEVICE_ID -t DEVICE_TOKEN
    //
    //       Must use ID and token of a device of 'Example Simple Smart Light' type (unique name cloud.artik.example.simple_smartlight)");
   private static boolean succeedParseCommand(String args[]) {
       if (args.length != EXPECTED_ARGUMENT_NUMBER) {
           return false; 
       }
       int index = 0;
       while (index < args.length) {
           String arg = args[index];
           if ("-d".equals(arg)) {
               ++index; // Move to the next argument the value of device id
               deviceId = args[index];
           } else if ("-t".equals(arg)) {
               ++index; // Move to the next argument the value of device token
               deviceToken = args[index];
           }
           ++index;
       }
       if (deviceToken == null || deviceId == null ) {
           return false;
       }
       return true;
   }
   
   private static void printUsage() {
       System.out.println("Usage: mqtt-smartlight" + " -d YOUR_DEVICE_ID -t YOUR_DEVICE_TOKEN");
       System.out.println("       You must use ID and token of a device of 'Example Simple Smart Light' type (unique name cloud.artik.example.simple_smartlight)");
   }

}
