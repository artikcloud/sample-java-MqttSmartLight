# An MQTT sample application subscribes and publishes to ARTIK Cloud

Let's build a Java console application that communicates to ARTIK Cloud [MQTT endpoint](https://developer.artik.cloud/documentation/data-management/mqtt.html) using ARTIK Cloud's [Java/Android SDK](https://github.com/artikcloud/artikcloud-java). This console app emulates a smart light.

After completing this sample, you will learn:

- How to [establish an MQTT session](https://developer.artik.cloud/documentation/data-management/mqtt.html#establish-an-mqtt-session) with ARTIK Cloud.
- How to [subscribe](https://developer.artik.cloud/documentation/data-management/mqtt.html#subscribe-to-receive-actions) to receive Actions.
- How to [publish messages](https://developer.artik.cloud/documentation/data-management/mqtt.html#publish-data-only-messages).

## Requirements
- Java version >= 1.8
- Apache Maven >= 3.3.9
- [ARTIK Cloud's Java/Android SDK](https://github.com/artikcloud/artikcloud-java)

## Setup / Installation

### Setup at ARTIK Cloud

 1. At My ARTIK Cloud, [connect a device](https://my.artik.cloud/new_device/cloud.artik.example.simple_smartlight) of the type "Example Simple Smart Light" (unique type name `cloud.artik.example.simple_smartlight`). You can use the one that you already own.
 2. Get the [device ID, device token](https://developer.artik.cloud/documentation/tools/web-tools.html#managing-a-device-token). You will need them when running the console app (smart light emulator). 
 
### Setup Java project

 1. Clone this repository if you haven't already done so.
 2. At the root directory and run the command:
  ~~~shell
  mvn clean package
  ~~~
  The executable `mqtt-smartlight-x.x.jar` is generated under the target directory. 
 3. Run the following command at target directory to learn the usage:
  ~~~shell
  java -jar mqtt-smartlight-x.x.jar
  ~~~

## Demo

Run the command at the target directory to start smart light emulator: 
~~~shell
  java -jar mqtt-smartlight-x.x.jar -d YOUR_DEVICE_ID -t YOUR_DEVICE_TOKEN
~~~

Remember that you got the device ID and token of your smart light in the ARTIK Cloud setup phase.

Then send Actions to the emulator using the [web tool](https://developer.artik.cloud/documentation/tutorials/an-iot-remote-control.html#test-the-light) at My ARTIK Cloud.

From the emulator terminal, you should see that Actions are received and updated states (on or off) are published back to ARTIK Cloud. The following is the example
~~~shell
  Start mqtt session....
  Connecting to broker: ssl://api.artik.cloud:8883......
  Succeeded: CONNECT

  Subscribing to topic: /v1.1/actions/7d469......
  Succeeded: SUBSCRIB
  Subscribed to topic: /v1.1/actions/7d469

  Received message. Payload: {"actions":[{"name":"setOn"}]}; Topic:/v1.1/actions/7d469; Qos:0
  Handle Action: setOn
  Set State to true
  Publishing to topic: /v1.1/messages/7d469; message payload: {"state":true}; QoS:2.....
  Succeeded: PUBLISH
  Published to topic: /v1.1/messages/7d469

  Received message. Payload: {"actions":[{"name":"setOff"}]}; Topic:/v1.1/actions/7d469; Qos:0
  Handle Action: setOff
  Set State to false
  Publishing to topic: /v1.1/messages/7d469; message payload: {"state":false}; QoS:2.....
  Succeeded: PUBLISH
  Published to topic: /v1.1/messages/7d469
~~~

Enter Ctrl+c to terminate the emulator.

## More about ARTIK Cloud

If you are not familiar with ARTIK Cloud, we have extensive documentation at https://developer.artik.cloud/documentation

The full ARTIK Cloud API specification can be found at https://developer.artik.cloud/documentation/api-spec

Check out sample applications at https://developer.artik.cloud/documentation/tutorials/

To create and manage your services and devices on ARTIK Cloud, create an account at https://developer.artik.cloud

Also see the ARTIK Cloud blog for tutorials, updates, and more: http://artik.io/blog/cloud

## License and Copyright

Licensed under the Apache License. See [LICENSE](LICENSE).

Copyright (c) 2017 Samsung Electronics Co., Ltd.
