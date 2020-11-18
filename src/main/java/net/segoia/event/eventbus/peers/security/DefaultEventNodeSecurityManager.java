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
package net.segoia.event.eventbus.peers.security;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.segoia.event.eventbus.peers.EventBusNodeConfig;
import net.segoia.event.eventbus.peers.core.IdentitiesManager;
import net.segoia.event.eventbus.peers.core.PublicIdentityManager;
import net.segoia.event.eventbus.peers.core.PublicIdentityManagerFactory;
import net.segoia.event.eventbus.peers.manager.states.server.PeerAcceptedState;
import net.segoia.event.eventbus.peers.security.rules.PeerEventBlackList;
import net.segoia.event.eventbus.peers.security.rules.PeerEventRule;
import net.segoia.event.eventbus.peers.security.rules.PeerRuleEngine;
import net.segoia.event.eventbus.peers.vo.auth.id.SharedIdentityType;
import net.segoia.event.eventbus.peers.vo.auth.id.SharedNodeIdentity;
import net.segoia.event.eventbus.peers.vo.auth.id.SpkiFullIdentityType;
import net.segoia.event.eventbus.peers.vo.auth.id.SpkiFullNodeIdentity;
import net.segoia.event.eventbus.peers.vo.auth.id.SpkiNodeIdentity;
import net.segoia.event.eventbus.peers.vo.security.IssueIdentityRequest;
import net.segoia.event.eventbus.peers.vo.session.KeyDef;
import net.segoia.event.eventbus.util.JsonUtils;
import net.segoia.util.crypto.CryptoUtil;

public class DefaultEventNodeSecurityManager extends EventNodeSecurityManager {

    private static Map<Class<?>, IdentityGenerator<PrivateIdentityDataLoader<?>>> identityGenerators = new HashMap<>();

    static {
	identityGenerators.put(SpkiPrivateIdentityDataLoader.class,
		new IdentityGenerator<PrivateIdentityDataLoader<?>>() {

		    @Override
		    public void generateIdentity(PrivateIdentityDataLoader<?> loader,
			    EventNodeSecurityManager securityManager) throws Exception {
			SpkiPrivateIdentityDataLoader idl = (SpkiPrivateIdentityDataLoader) loader;
			SpkiIdentityDef identityDef = idl.getIndentityDef();

			IdentitiesManager identitiesManager = securityManager.getSecurityConfig()
				.getIdentitiesManager();
			securityManager.getLogger().info("Generating identity for name "+identityDef.getName());
			SpkiFullNodeIdentity newIdentity = (SpkiFullNodeIdentity) identitiesManager
				.issueIdentity(new IssueIdentityRequest(new SpkiFullIdentityType(
					new KeyDef(identityDef.getAlgorithm(), identityDef.getKeySize()))));

			/* check if target path exists */
			File targetPath = new File(identityDef.getPath());
			if (!targetPath.exists()) {
			    securityManager.getLogger().info("Generating path for identity "+identityDef.getName()+" -> "+identityDef.getPath());
			    boolean created = targetPath.mkdirs();
			}

			securityManager.getLogger().info("Saving identity "+identityDef.getName() +" path "+targetPath.getAbsolutePath());
			CryptoUtil.saveKey(newIdentity.getPrivateKey().getBytes("UTF-8"),
				new File(targetPath, identityDef.getName()).getAbsolutePath(),false);
			CryptoUtil.saveKey(newIdentity.getPubKey().getBytes("UTF-8"),
				new File(targetPath, identityDef.getName() + ".pub").getAbsolutePath(),false);
			
			securityManager.getLogger().info("Identity "+identityDef.getName()+" generated successfully.");

//		    try {
//			CryptoUtil.generateAndSaveKeyPair(identityDef.getAlgorithm(), identityDef.getKeySize(), identityDef.getPath(), identityDef.getName());
//		    } catch (NoSuchAlgorithmException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		    } catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		    }
		    }

		});

    }

    public DefaultEventNodeSecurityManager() {
	super();
	// TODO Auto-generated constructor stub
    }

    public DefaultEventNodeSecurityManager(EventNodeSecurityConfig securityConfig) {
	super(securityConfig);
	// TODO Auto-generated constructor stub
    }

    public DefaultEventNodeSecurityManager(EventBusNodeConfig nodeConfig) {
	super(nodeConfig);
	// TODO Auto-generated constructor stub
    }

    @Override
    protected void init() {
	setCryptoHelper(new DefaultCryptoHelper());
	super.init();
    }

    @Override
    protected void handleNoIdentity() {
	EventNodeSecurityConfig securityConfig = getSecurityConfig();
	List<PrivateIdentityDataLoader<?>> identityLoaders = securityConfig.getIdentityLoaders();

	boolean generated=false;
	/* if we have at least an identity loader, we try to generate an identity for it */
	if (!identityLoaders.isEmpty()) {
	    for(PrivateIdentityDataLoader<?> idLoader : identityLoaders) {
	    IdentityGenerator identityGenerator = identityGenerators.get(idLoader.getClass());
	    if (identityGenerator != null) {
		try {
		    identityGenerator.generateIdentity(idLoader, this);
		    generated=true;
		    break;
		} catch (Exception e) {
		    getLogger().error("Failed to ganerate identity for loader "+idLoader,e);
		}
	    }
	    }
	}
	
	if(generated) {
	    /* reload identities */
	    getLogger().info("Reloading identities");
	    loadIdentities();
	}
    }

    @Override
    protected void initSessionManagerBuilders() {
	sessionManagerBuilders = new HashMap<>();

	sessionManagerBuilders.put(new SharedIdentityType(new KeyDef("AES", 128)),
		new SessionManagerFactory<DefaultSessionManager>() {

		    @Override
		    public DefaultSessionManager build(SharedNodeIdentity identity) {
			return new DefaultSessionManager(identity);
		    }
		});
    }

    @Override
    protected void initPublicIdentityBuilders() {
	if (publicIdentityBuilders == null) {
	    publicIdentityBuilders = new HashMap<>();
	}

	publicIdentityBuilders.put(SpkiNodeIdentity.class, new PublicIdentityManagerFactory<SpkiNodeIdentity>() {

	    @Override
	    public PublicIdentityManager build(SpkiNodeIdentity nodeidentity) {
		return new SpkiPublicIdentityManagerImpl(nodeidentity);
	    }

	});
    }

    @Override
    protected void initSerializers() {
	defaultDataSerializer = new NestedOperationDataSerializer((o) -> {
	    return o.getData();
	});

	defaultDataSerializer.addSerializer(SignCommOperationOutput.class, (o) -> {

	    try {
		return JsonUtils.toJson(o).getBytes("UTF-8");
	    } catch (UnsupportedEncodingException e) {

		e.printStackTrace();
	    }
	    return null;
	});
    }

    @Override
    protected void initIdentityRoles() {
	PeerRuleEngine peerRuleEngine = getPeerRuleEngine();
	if (peerRuleEngine == null) {
	    peerRuleEngine = new PeerRuleEngine();
	    peerRuleEngine.setRuleMatcherFactory(new DefaultRuleMatcherFactory());
	    setPeerRuleEngine(peerRuleEngine);
	}

	List<PeerEventRule> serviceAccessRules = new ArrayList<>();
	serviceAccessRules.add(peerRuleEngine.buildRegexRule(PeerAcceptedState.ID + "/SERVICE:ACCESS_ID:REQUEST"));

	PeerEventBlackList deniedServiceAccessIdEvents = new PeerEventBlackList(serviceAccessRules);

	IdentityRole serviceAccessIdRole = new IdentityRole(IdentityRole.SERVICE_ACCESS, deniedServiceAccessIdEvents);

	identityRoles.put(serviceAccessIdRole.getType(), serviceAccessIdRole);

	identityRoles.put(IdentityRole.PEER_AUTH, new IdentityRole(IdentityRole.PEER_AUTH, null));
    }

    @Override
    protected void initDeserializers() {
	deserializers = new HashMap<>();
	deserializers.put(SignCommOperationOutput.class, (data) -> {

	    String json;
	    try {
		json = new String(data, "UTF-8");
	    } catch (UnsupportedEncodingException e) {
		throw new RuntimeException("Failed to convert bytes to UTF-8 string");
	    }
	    SignCommOperationOutput signOperationOutput = JsonUtils.fromJson(json, SignCommOperationOutput.class);
	    return signOperationOutput;
	});
    }

}
