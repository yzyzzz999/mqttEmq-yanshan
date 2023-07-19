package com.example.emqdemo.util.mqttUtil;

import com.example.emqdemo.domain.MqttConfiguration;
import com.example.emqdemo.util.PushCallback;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.UnsupportedEncodingException;

@Slf4j
public class MqttPushClient  {
    private static MqttClient client;

    public static MqttClient getClient() {
        return client;
    }

    public static void setClient(MqttClient client) {
        MqttPushClient.client = client;
    }




    /**
     * 编辑连接信息
     * @param userName
     * @param password
     * @param outTime
     * @param KeepAlive
     * @return
     */
    private MqttConnectOptions getOption(String userName, String password, int outTime, int KeepAlive) {
        //MQTT连接设置
        MqttConnectOptions option = new MqttConnectOptions();
        //设置是否清空session,false表示服务器会保留客户端的连接记录，true表示每次连接到服务器都以新的身份连接
        option.setCleanSession(false);
        //设置连接的用户名
        option.setUserName(userName);
        //设置连接的密码
        option.setPassword(password.toCharArray());
        //设置超时时间 单位为秒
        option.setConnectionTimeout(outTime);
        //设置会话心跳时间 单位为秒 服务器会每隔(1.5*keepTime)秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
        option.setKeepAliveInterval(KeepAlive);
        //setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
        //option.setWill(topic, "close".getBytes(StandardCharsets.UTF_8), 2, true);
        option.setMaxInflight(1000);
        return option;
    }

    /**
     * 发起连接
     */
    public void connect(MqttConfiguration mqttConfiguration) {
        MqttClient client;
        try {
            client = new MqttClient(mqttConfiguration.getHost(), mqttConfiguration.getClientId(), new MemoryPersistence());
            MqttConnectOptions options = getOption(mqttConfiguration.getUsername(), mqttConfiguration.getPassword(),
                    mqttConfiguration.getTimeout(), mqttConfiguration.getKeepAlive());
            MqttPushClient.setClient(client);
            try {
                client.setCallback(new PushCallback(this, mqttConfiguration));
                if (!client.isConnected()) {
                    client.connect(options);
                    log.info("================>>>MQTT连接成功<<======================");

                } else {//这里的逻辑是如果连接不成功就重新连接
                    client.disconnect();
                    client.connect(options);
                    log.info("===================>>>MQTT断连成功<<<======================");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 断线重连
     *
     * @throws Exception
     */
    public Boolean reConnect() throws Exception {
        Boolean isConnected = false;
        if (null != client) {
            client.connect();
            if (client.isConnected()) {
                isConnected = true;
            }
        }
        return isConnected;
    }

    /**
     * 发布，默认qos为0，非持久化
     *
     * @param topic
     * @param pushMessage
     */
    public void publish(String topic, String pushMessage) {

        publish(0, false, topic, pushMessage);

    }
    /**
     * 发布
     *
     * @param qos
     * @param retained
     * @param topic
     * @param pushMessage
     */
    public void publish(int qos, boolean retained, String topic, String pushMessage) {
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(retained);
        try {
            message.setPayload(pushMessage.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        MqttTopic mTopic = MqttPushClient.getClient().getTopic(topic);
        if (null == mTopic) {
            log.error("===============>>>MQTT topic 不存在<<=======================");
        }
        MqttDeliveryToken token;
        try {
            token = mTopic.publish(message);
            token.waitForCompletion();
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    /**
     * 发布消息的服务质量(推荐为：2-确保消息到达一次。0-至多一次到达；1-至少一次到达，可能重复)，
     * retained 默认：false-非持久化（是指一条消息消费完，就会被删除；持久化，消费完，还会保存在服务器中，当新的订阅者出现，继续给新订阅者消费）
     *
     * @param topic
     * @param pushMessage
     */
    public void publish(int qos, String topic, String pushMessage) {

        publish(qos, false, topic, pushMessage);

    }

    /**
     *
     * 订阅多个主题
     *
     * @param topic
     * @param qos
     */
    public void subscribe(String[] topic, int[] qos) {
        try {
            MqttPushClient.getClient().unsubscribe(topic);
            MqttPushClient.getClient().subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清空主题
     * @param topic
     */
    public void cleanTopic(String topic) {
        try {
            MqttPushClient.getClient().unsubscribe(topic);
        } catch (MqttException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }




}
