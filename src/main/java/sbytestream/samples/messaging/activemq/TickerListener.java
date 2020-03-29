// Author       : Siddharth Barman
// Date         : 29 March 2020
// Description  : Sample program demonstrating sending and receiving
//                text messages topics.
//                You should have a running ActiveMQ server.

package sbytestream.samples.messaging.activemq;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import sbytestream.samples.messaging.Tick;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class TickerListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            Tick tick = m_xmlMapper.readValue(textMessage.getText(), Tick.class);
            System.out.printf("[%s] ", tick.toString());
        }
        catch(Exception e) {
            System.out.println("onMessage error: ");
            System.out.println(e.getMessage());
        }
    }

    private XmlMapper m_xmlMapper = new XmlMapper();
}
