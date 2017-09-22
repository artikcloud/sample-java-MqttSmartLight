# An MQTT sample application subscribes and publish to ARTIK Cloud

Let's build a Java console application that communicates to [ARTIK Cloud MQTT endpoint](https://developer.artik.cloud/documentation/data-management/mqtt.html) using [ARTIK Cloud's Java/Android SDK](https://github.com/artikcloud/artikcloud-java). This console app emulates a smart light.

After completing this sample, you will learn:

- How to [establish an MQTT session with ARTIK Cloud](https://developer.artik.cloud/documentation/data-management/mqtt.html#establish-an-mqtt-session).
- How to [subscribe to receive Actions](https://developer.artik.cloud/documentation/data-management/mqtt.html#subscribe-to-receive-actions).
- How to [publish messages](https://developer.artik.cloud/documentation/data-management/mqtt.html#publish-data-only-messages) to ARTIK Cloud.

## Requirements
- Java version >= 1.8
- Apache Maven >= 3.3.9
- [ARTIK Cloud's Java/Android SDK](https://github.com/artikcloud/artikcloud-java)

## Setup / Installation

### Setup at ARTIK Cloud

 1. At [My ARTIK Cloud](https://my.artik.cloud/), [connect a device](/documentation/tools/web-tools.html#connecting-a-device) (or use one that you already own) of the device type "Example Simple Smart Light" (unique name `cloud.artik.example.simple_smartlight`).
 2. Get the [device ID, device token](https://developer.artik.cloud/documentation/tools/web-tools.html#managing-a-device-token), which you will need later. 
 
### Setup two Java projects

 1. Clone this repository if you haven't already done so.
 2. At the root directory and run the following command:
  ~~~shell
  mvn clean package
  ~~~
  The executable `mqtt-smartlight-x.x.jar` is generated under the target directory. Run the following command at target directory to learn the usage:
  ~~~shell
  java -jar mqtt-smartlight-x.x.jar
  ~~~

## Demo

Run the following command at target directory to start smart light emulator: 
~~~shell
  java -jar mqtt-smartlight-x.x.jar -d YOUR_DEVICE_ID -t YOUR_DEVICE_TOKEN
~~~

Remember that you got the device ID and token of your smart light in the ARTIK Cloud setup phase.

Then send Actions to the emulator using the [web tool](https://developer.artik.cloud/documentation/tutorials/an-iot-remote-control.html#test-the-light) at My ARTIK Cloud.

From the emulator terminal, you should see that the Action is received and updated state is published back to ARTIK Cloud.

## More about ARTIK Cloud

If you are not familiar with ARTIK Cloud, we have extensive documentation at https://developer.artik.cloud/documentation

The full ARTIK Cloud API specification can be found at https://developer.artik.cloud/documentation/api-spec

Check out sample applications at https://developer.artik.cloud/documentation/tutorials/

To create and manage your services and devices on ARTIK Cloud, create an account at https://developer.artik.cloud

Also see the ARTIK Cloud blog for tutorials, updates, and more: http://artik.io/blog/cloud

## License and Copyright

Licensed under the Apache License. See [LICENSE](LICENSE).

Copyright (c) 2017 Samsung Electronics Co., Ltd.
