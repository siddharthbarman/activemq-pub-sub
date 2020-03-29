// Author       : Siddharth Barman
// Date         : 28 March 2020
// Description  : Sample program demonstrating sending and receiving
//                text messages using sbytestream.messaging.ActiveMQ queues.
//                You should have a running ActiveMQ server.

package sbytestream.samples.messaging.activemq;
import sbytestream.messaging.TopicWrapper;
import sbytestream.samples.messaging.Ticker;
import sbytestream.utilities.CmdLine;

import javax.jms.*;

enum Mode {
    PRODUCER,
    CONSUMER
}

public class PubSub {
    public static void help() {
        System.out.println("Create a sample pub/sub stock ticker application.");
        System.out.println("Syntax: -mode [producer|consumer] -host hostname -port portnumber -topic topic-name");
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            help();
            return;
        }

        CmdLine cmd = new CmdLine(args);

        if (!cmd.isFlagPresent("mode")) {
            System.out.println("mode has not been specified. Quitting.");
            return;
        }

        if (!cmd.isFlagPresent("host")) {
            System.out.println("ActiveMQ host has not been specified. Quitting.");
            return;
        }

        if (!cmd.isFlagPresent("port")) {
            System.out.println("ActiveMQ port has not been specified. Quitting.");
            return;
        }

        if (!cmd.isFlagPresent("topic")) {
            System.out.println("topic name has not been specified. Quitting.");
            return;
        }

        System.out.println(cmd.getFlagValue("port"));
        int port = cmd.getFlagValueInt("port", -1);
        if (port <= 0 || port > 65535) {
            System.out.println("Invalid port number has been specified. Quitting.");
            return;
        }

        String mode = cmd.getFlagValue("mode").toUpperCase();
        Mode programMode = Mode.valueOf(mode);

        String host = cmd.getFlagValue("host");
        String topic = cmd.getFlagValue("topic");

        try {
            if (programMode == Mode.PRODUCER) {
                startPublsiher(host, port, topic);
            }
            else if (programMode == Mode.CONSUMER) {
                startSubscriber(host, port, topic);
            }
        }
        catch(Exception e) {
            System.out.println("Something bad happened!");
            System.out.println(e.getMessage());
        }
    }

    private static void startPublsiher(String host, int port, String topicName) throws Exception {
        TopicWrapper topicWrapper = new TopicWrapper(host, port, topicName);
        Ticker ticker = new Ticker(2000, topicWrapper);
        Thread thread = new Thread(ticker);

        System.out.println("Press <Enter> to exit.");
        thread.start();

        System.console().readLine();
        System.out.println("Stopping stock ticker...");

        ticker.stop();
        thread.join();

        topicWrapper.close();
    }

    private static void startSubscriber(String host, int port, String topic) throws Exception {
        TopicWrapper topicWrapper = new TopicWrapper(host, port, topic);
        TopicSubscriber subscriber = topicWrapper.createSubscriber(new TickerListener());

        System.out.println("Press <Enter> to exit.");
        System.console().readLine();

        subscriber.close();
        topicWrapper.close();
    }
}
