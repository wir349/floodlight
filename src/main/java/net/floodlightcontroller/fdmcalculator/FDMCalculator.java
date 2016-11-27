package net.floodlightcontroller.fdmcalculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.projectfloodlight.openflow.types.DatapathId;
import org.projectfloodlight.openflow.types.OFPort;
import org.projectfloodlight.openflow.types.U64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.linkdiscovery.ILinkDiscovery.LDUpdate;
import net.floodlightcontroller.linkdiscovery.Link;
import net.floodlightcontroller.topology.ITopologyListener;
import net.floodlightcontroller.topology.ITopologyService;

public class FDMCalculator implements IFDMCalculator, ITopologyListener, IFloodlightModule {

	protected static final Logger log = LoggerFactory.getLogger(FDMCalculator.class);

	// Protected variables we'll be using
	protected ITopologyService topologyService;		// Topology service that we'll be calling

	private Map<Link, Float> globalLinkFlows;
	
	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleServices() {
		// TODO Auto-generated method stub
		log.info("getModuleServices");
		return null;
	}

	@Override
	public Map<Class<? extends IFloodlightService>, IFloodlightService> getServiceImpls() {
		// TODO Auto-generated method stub
		log.info("getServiceImpls");
		return null;
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleDependencies() {
		// Set dependencies
		// We require TopologyService to be up and running first
		Collection<Class<? extends IFloodlightService>> l = 
				new ArrayList<Class<? extends IFloodlightService>>();
		l.add(ITopologyService.class);
		log.info("getModuleDependencies");

		return l;
	}

	@Override
	public void init(FloodlightModuleContext context) 
			throws FloodlightModuleException {
		// Initialize our dependencies
		topologyService = context.getServiceImpl(ITopologyService.class);
		topologyService.addListener(FDMCalculator.this);
		log.info("init");

	}

	@Override
	public void startUp(FloodlightModuleContext context) 
			throws FloodlightModuleException {
		// TODO Auto-generated method stub
		log.info("startup");
	}

	@Override
	public void topologyChanged(List<LDUpdate> linkUpdates) {
		// Update our topology
		log.info("topologyChanged");
		buildTopology();
	}

	@Override
	public void addFlow(DatapathId srcNodeID, DatapathId desNodeID,
			double requestBW) {
		// TODO Auto-generated method stub
		// THIS IS OUTSIDE FALL 2016 SCOPE - ADDED AS TEMPLATE FOR FUTURE WORK
		//calculateFDM();
	}

	@Override
	public void removeFlow(DatapathId srcNodeID, DatapathId desNodeID) {
		// TODO Auto-generated method stub
		// THIS IS OUTSIDE FALL 2016 SCOPE - ADDED AS TEMPLATE FOR FUTURE WORK
		//calculateFDM();
	}

	@Override
	public float getFlowBW(IOFSwitch currentSwitch, OFPort currentPort,IOFSwitch nextSwitch, OFPort nextPort) {
		U64 latency = U64.of(0L);
		
		// Build a link to send in
		Link link = new Link(currentSwitch.getId(), currentPort, nextSwitch.getId(), nextPort, latency);
		
		return globalLinkFlows.get(link);
	}
	
	@Override
	public float getFlowBW(Link link) {
		return globalLinkFlows.get(link);
	}
	
	/**
	 * Main function for doing FDM
	 * This only calculates and fill gflow
	 */
	private void calculateFDM() {
		// TODO FDM code here
	}
	
	/**
	 * Build the topology in our implementation
	 * This is needed as TopologyService keeps its topology in a very different way
	 */
	private void buildTopology() {
		// Variables we need
		Map<DatapathId, Set<Link>> linkMap;
		
		linkMap = topologyService.getAllLinks();
		log.info("No. of Nodes: " + linkMap.size());
		log.info("Complete Topology: " + linkMap.toString());

		FDMTopology top = new FDMTopology(1, linkMap);
		log.info("All Links: " + top.allLinks);
		log.info("All Nodes: " + top.nodes);
		Float[] a_cap = {10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f};
		top.initCapacity(a_cap);
		Float[][] a_req = { {0f, 0f, 0f, 10f} , {0f, 0f, 0f, 0f}, {0f, 0f, 0f, 0f}, {0f, 0f, 0f, 0f} };
		log.info("All Req: " + Arrays.deepToString(a_req));
		top.initRequirements(a_req);
		
		float delta = 0.002f;
		float epsilon = 0.00001f;
		FlowDeviationMethod fdm = new FlowDeviationMethod(delta, epsilon);
		globalLinkFlows = fdm.runFDM(top);
		
		log.info("Global Flows: " + globalLinkFlows);
		
	}

}