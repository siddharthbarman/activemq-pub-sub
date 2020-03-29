// Author       : Siddharth Barman
// Date         : 28 March 2020
// Description  : Thin wrapper over ActiveMQ classes.

package sbytestream.messaging;

import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;

public class QueueWrapper {
    public void QueueWrapper(String host, int port, String name) throws JMSException {
        String activeMQUri = String.format("tcp://%s:%d", host, port);
        ConnectionFactory connFactory = new ActiveMQConnectionFactory(activeMQUri);
        m_connection = connFactory.createConnection();
        m_session = m_connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        m_queue = m_session.createQueue(name);
    }

    public MessageProducer createProducer() throws JMSException {
        return m_session.createProducer(m_queue);
    }

    public MessageConsumer createConsumer(MessageListener listener) throws JMSException {
        MessageConsumer consumer = m_session.createConsumer(m_queue);
        consumer.setMessageListener(listener);
        m_connection.start();
        return consumer;
    }

    public void close() throws JMSException {
        if (m_session != null) m_session.close();
        if (m_connection != null) m_connection.close();
    }

    public Message createTextMessage(String text) throws JMSException {
        return m_session.createTextMessage(text);
    }

    private Session m_session;
    private Queue m_queue;
    private Connection m_connection;
}
