package net.segoia.event.eventbus.peers.comm;

/**
 * A protocol of event node communication that sends the events in plain text
 * 
 * @author adi
 *
 */
public class PlainCommunicationProtocol extends CommunicationProtocol {

    public PlainCommunicationProtocol() {
	CommunicationProtocolDefinition def = new CommunicationProtocolDefinition();
	def.setClientCommStrategy(new NodeCommunicationStrategy());
	def.setServerCommStrategy(new NodeCommunicationStrategy());

	CommunicationProtocolConfig config = new CommunicationProtocolConfig();

	setProtocolDefinition(def);
	setConfig(config);
    }

}
