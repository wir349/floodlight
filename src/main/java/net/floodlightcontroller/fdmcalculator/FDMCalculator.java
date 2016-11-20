package net.floodlightcontroller.fdmcalculator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.projectfloodlight.openflow.types.DatapathId;

import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.linkdiscovery.ILinkDiscovery.LDUpdate;
import net.floodlightcontroller.linkdiscovery.Link;
import net.floodlightcontroller.topology.ITopologyListener;
import net.floodlightcontroller.topology.ITopologyService;

public class FDMCalculator implements IFDMCalculator, ITopologyListener, IFloodlightModule {

	// Protected variables we'll be using
	protected ITopologyService topologyService;		// Topology service that we'll be calling
	// Declare FDL needed stuff here, such as End1[] and End2[]
	protected int nl;
	protected LinkedList<Long> End1; // Stub!
	protected LinkedList<Long> End2;
	// protected LinkedList<Long> req;
	
	
	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleServices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Class<? extends IFloodlightService>, IFloodlightService> getServiceImpls() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleDependencies() {
		// Set dependencies
		// We require TopologyService to be up and running first
		Collection<Class<? extends IFloodlightService>> l = 
				new ArrayList<Class<? extends IFloodlightService>>();
		l.add(ITopologyService.class);
		return l;
	}

	@Override
	public void init(FloodlightModuleContext context) 
			throws FloodlightModuleException {
		// Initialize our dependencies
		topologyService = context.getServiceImpl(ITopologyService.class);
	}

	@Override
	public void startUp(FloodlightModuleContext context) 
			throws FloodlightModuleException {
		// TODO Auto-generated method stub

	}

	@Override
	public void topologyChanged(List<LDUpdate> linkUpdates) {
		// Update our topology
		buildTopology();
	}

	@Override
	public void addFlow(DatapathId srcNodeID, DatapathId desNodeID,
			double requestBW) {
		// TODO Auto-generated method stub
		// req.add
		calculateFDM();
	}

	@Override
	public void removeFlow(DatapathId srcNodeID, DatapathId desNodeID) {
		// TODO Auto-generated method stub
		// req.remove
		calculateFDM();
	}

	@Override
	public double getFlowBW(DatapathId srcNodeID, DatapathId desNodeID) {
		// TODO Auto-generated method stub
		// Go through End1 and End2, find match, find match in Gflow
		return 0;
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
		Collection<DatapathId> idCollection;
		Set<Link> linkSet;
		DatapathId currentID;
		Link currentLink;
		int index = 0;
		
		linkMap = topologyService.getAllLinks();
		
		// Get keys into collection. Allows traverse in map.
		idCollection = linkMap.keySet();
		
		// Traverse collection, for each id, match links
		while(idCollection.iterator().hasNext()){
			currentID = idCollection.iterator().next();
			
			// Grab the set of links for this node
			linkSet = linkMap.get(currentID);
			while(linkSet.iterator().hasNext()){
				currentLink = linkSet.iterator().next();
				// TODO Build End1, End2, and Adj here
				// End1.add(index, currentLink.getSrc().getLong());
				// End2.add(index, currentLink.getDst().getLong());
				// Adj thingy does something here...
				
				index++;
			}
		}
		
	}

}