/**
 * event-bus - An event bus framework for event driven programming
 * Copyright (C) 2016  Adrian Cristian Ionescu - https://github.com/acionescu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.segoia.event.eventbus.peers.test.vo;

import net.segoia.event.eventbus.peers.core.AbstractEventTransceiver;
import net.segoia.event.eventbus.peers.vo.PeerLeavingReason;

public abstract class TestEventTransceiver extends AbstractEventTransceiver {
    /**
     * keep the pair transceiver instance
     */
    private transient TestEventTransceiver pairTransceiver;

    private boolean sendAsync;

    @Override
    public void sendData(byte[] data) {
	System.out.println("Sending "+new String(data));
	if (!sendAsync) {
	    pairTransceiver.receiveData(data);
	} else {
	    new Thread() {

		@Override
		public void run() {
		    pairTransceiver.receiveData(data);
		}

	    }.start();
	}
    }
    
    

    @Override
    public void start() {
	// TODO Auto-generated method stub
	
    }



    @Override
    public void init() {
	// TODO Auto-generated method stub
	
    }



    @Override
    public void receiveData(byte[] data) {
	System.out.println("Receiving: "+new String(data));
	super.receiveData(data);
    }



    @Override
    public void terminate() {
	System.out.println("Terminating transceiver");
	pairTransceiver.onPeerLeaving(new PeerLeavingReason(0, "Terminated"));
    }

    @Override
    public String getChannel() {
	return "TEST_LOCAL";
    }

    public TestEventTransceiver getPairTransceiver() {
	return pairTransceiver;
    }

    public void setPairTransceiver(TestEventTransceiver pairTransceiver) {
	this.pairTransceiver = pairTransceiver;
    }

    public boolean isSendAsync() {
        return sendAsync;
    }

    public void setSendAsync(boolean sendAsync) {
        this.sendAsync = sendAsync;
    }

}
