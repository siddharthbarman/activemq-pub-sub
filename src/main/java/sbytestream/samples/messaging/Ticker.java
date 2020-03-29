// Author       : Siddharth Barman
// Date         : 29 March 2020
// Description  : The Stock ticker which keeps publishing stock ticks every few
//                milli-seconds.

package sbytestream.samples.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import sbytestream.messaging.TopicWrapper;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TopicPublisher;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ticker implements Runnable {
    public Ticker(int frequencyMS, TopicWrapper topicWrapper) {
        m_intervalMS = frequencyMS;
        m_topicWrapper = topicWrapper;
    }

    public void stop() {
        m_stop = true;
    }

    @Override
    public void run() {
        m_symbols.add(new Tick("INFY", 230.34F, 0));
        m_symbols.add(new Tick("GLEN", 100F, 0));
        m_symbols.add(new Tick("MARU", 344.20F, 0));
        m_symbols.add(new Tick("RAYM", 54.66F, 0));

        try {
            m_publisher = m_topicWrapper.createPublisher();

            for (Tick tick : m_symbols) {
                sendTick(tick);
                Thread.sleep(m_intervalMS);
            }

            while (!m_stop) {
                Tick tick = getRandomTick();
                sendTick(tick);
                Thread.sleep(m_intervalMS);
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Publisher thread will not stop!");
        }
        finally {
            try {
                if (m_publisher != null) m_publisher.close();
            }
            catch(Exception e) {
                System.out.println(e.getMessage());
            }
         }
    }

    private void sendTick(Tick tick) throws JMSException, JsonProcessingException {
        System.out.println(tick);
        String tickXml = m_xmlMapper.writeValueAsString(tick);
        Message message =m_topicWrapper.createTextMessage(tickXml);
        m_publisher.send(message);
    }

    private Tick getRandomTick() {
        Random rnd = new Random();
        int ndx = rnd.nextInt(m_symbols.size() - 1);

        Tick tick = m_symbols.get(ndx);
        float oldPrice = tick.getPrice();

        int percent = rnd.nextInt(100);
        if (percent > 0) {
            boolean up = rnd.nextBoolean();
            float diff = oldPrice * (float) percent / 100F;

            if (up) {
                tick.setPrice(oldPrice + diff);
                tick.setDirection(1);
            } else {
                tick.setPrice(oldPrice - diff);
                tick.setDirection(-1);
            }
        }
        else {
            tick.setDirection(0);
        }

        return tick;
    }

    private List<Tick> m_symbols = new ArrayList<Tick>();
    private int m_intervalMS;
    private TopicWrapper m_topicWrapper;
    private volatile boolean m_stop = false;
    private XmlMapper m_xmlMapper = new XmlMapper();
    private TopicPublisher m_publisher = null;

}
