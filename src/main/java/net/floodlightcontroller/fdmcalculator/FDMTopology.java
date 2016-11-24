package net.floodlightcontroller.fdmcalculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.projectfloodlight.openflow.types.DatapathId;

import net.floodlightcontroller.linkdiscovery.Link;


class FDMTopology {

	
	LinkedList<Link> allLinks = new LinkedList<Link>();
//	ArrayList<Link> links = new ArrayList<Link>();

	Integer LINK;	
	
	LinkedList<Integer> nodeAdjLinks[];
	LinkedList<DatapathId> nodes = new LinkedList<DatapathId>();

	Float[][] req;
//	Float[][] mm_req;
	
	Float total_requirement = 0.0f;
	
	
	Float capacity[];
	
	Integer msgLen = 1;
	
//	Float CurrentDelay = 0.0f;
//	Float[] globalFlow;
	
	public FDMTopology(Integer msgLen) {
		this.msgLen = msgLen;
	}
	
	public FDMTopology(Integer msgLen, Map<DatapathId, Set<Link>> topLinks) {
		super();
		nodeAdjLinks = new LinkedList[topLinks.size()];
		for (Map.Entry<DatapathId, Set<Link>> entry : topLinks.entrySet())
		{
			LinkedList<Integer> ll = new LinkedList<Integer>();
			for (Link link : entry.getValue()) {
				int currentLinkIndex = allLinks.indexOf(link);
				if (currentLinkIndex == -1) {
					currentLinkIndex = allLinks.size();
					allLinks.addLast(link);
				}
				ll.add(currentLinkIndex);
			}
			nodeAdjLinks[nodes.size()] = ll;
			nodes.addLast(entry.getKey());
		}
		
//		end1 = new Integer[noLinks];
//		end2 = new Integer[noLinks];
		
		req = new Float[getNoNodes()][getNoNodes()];
//		mm_req = new Float[noNodes][noNodes];
		
//		FDLen = new Float[noLinks];
		capacity = new Float[getNoLinks()];
//		globalFlow = new Float[noLinks];
	}
	
	public Integer getNoLinks() {
		return allLinks.size();
	}

	public Integer getNoNodes() {
		return nodes.size();
	}
	
//	public void initCapacity(Float[] linkCapacities) {
////		capacity = new Float[getNoLinks()];
//		capacity = linkCapacities;
//	}
//	
	public void initRequirements(Float [][] a_req) {
		for(Integer i = 0; i < getNoNodes(); i++) {
			for(Integer j = 0; j < getNoNodes(); j++) {
				if (a_req[i][j] > 0) {
					req[i][j] = a_req[i][j];
					total_requirement += a_req[i][j];
				}
				else {
					req[i][j] = 0.0f;
				}
//				mm_req[i][j] = 0.0f;
			}
		}
	}
	
	public void initCapacity(Float[] a_cap) {
		System.arraycopy(a_cap, 0, capacity, 0, capacity.length);
	}

	public Float[][] getReq() {
		return req;
	}

//	public Float getCurrentDelay() {
//		return CurrentDelay;
//	}
//
//	public void setCurrentDelay(Float currentDelay) {
//		CurrentDelay = currentDelay;
//	}

//	public Float[] getGflow() {
//		return globalFlow;
//	}
//
//	public void setGflow(Float[] gflow) {
//		globalFlow = gflow;
//	}

	public Integer getEnd1(Integer index) {
		Link currentLink = allLinks.get(index);
		int node = nodes.indexOf(currentLink.getSrc());
//		System.out.println("End 1 Value: " + node);
		return node;
	}

	public Integer getEnd2(Integer index) {
		Link currentLink = allLinks.get(index);
		int node = nodes.indexOf(currentLink.getDst());
//		System.out.println("End 1 Value: " + node);
		return node;
	}

	public Float getTotal_requirement() {
		return total_requirement;
	}

	public LinkedList<Integer>[] getAdj() {
		return nodeAdjLinks;
	}

	public Float[] getCapacity() {
		return capacity;
	}

	public Integer getMsgLen() {
		return msgLen;
	}
	
	
	
}
