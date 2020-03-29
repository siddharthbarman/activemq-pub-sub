// Author       : Siddharth Barman
// Date         : 29 March 2020
// Description  : Thin wrapper over ActiveMQ topic access.

package sbytestream.messaging;

import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;
import java.io.Serializable;

public class TopicWrapper {
    public TopicWrapper(String host, int port, String name) throws JMSException {
        String activeMQUri = String.format("tcp://%s:%d", host, port);
        TopicConnectionFactory connFactory = new ActiveMQConnectionFactory(activeMQUri);
        m_connection = connFactory.createTopicConnection();
        m_session = m_connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        m_topic = m_session.createTopic(name);
    }

    public TopicPublisher createPublisher() throws JMSException {
        return m_session.createPublisher(m_topic);
    }

    public TopicSubscriber createSubscriber(MessageListener listener) throws JMSException {
        TopicSubscriber subscriber = m_session.createSubscriber(m_topic);
        subscriber.setMessageListener(listener);
        m_connection.start();
        return subscriber;
    }

    public Message createTextMessage(String text) throws JMSException {
        return m_session.createTextMessage(text);
    }

    public Message createObjectMessage(Serializable obj) throws JMSException {
        return m_session.createObjectMessage(obj);
    }

    public void close() throws JMSException {
        if (m_session != null) m_session.close();
        if (m_connection != null) m_connection.close();
    }

    private TopicSession m_session;
    private Topic m_topic;
    private TopicConnection m_connection;
}