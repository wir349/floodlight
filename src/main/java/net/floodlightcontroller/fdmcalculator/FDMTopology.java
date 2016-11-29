package net.floodlightcontroller.fdmcalculator;


import java.util.LinkedList;
import java.util.Map;
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
	Float[][] mm_req;
	
	Float total_requirement = 0.0f;
	Float mm_total_requirement = 0.0f;
	
	Float capacity[];
	
	Integer msgLen = 1;
		
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
		
		req = new Float[getNoNodes()][getNoNodes()];
		mm_req = new Float[getNoNodes()][getNoNodes()]
		
		capacity = new Float[getNoLinks()];
	}
	
	public Integer getNoLinks() {
		return allLinks.size();
	}

	public Integer getNoNodes() {
		return nodes.size();
	}
	
//	public void initCapacity(Float[] linkCapacities) {
//		capacity = new Float[getNoLinks()];
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
			}
		}
	}
	
	public void initMMRequirements(Float [][] a_req) {
		for(Integer i = 0; i < getNoNodes(); i++) {
			for(Integer j = 0; j < getNoNodes(); j++) {
				if (a_req[i][j] > 0) {
					mm_req[i][j] = a_req[i][j];
					mm_total_requirement += a_req[i][j];
				}
				else {
					mm_req[i][j] = 0.0f;
				}
			}
		}
	}
	
	public void initCapacity(Float[] a_cap) {
		System.arraycopy(a_cap, 0, capacity, 0, capacity.length);
	}

	public Float[][] getReq() {
		return req;
	}
	
	public Float[][] getMMReq() {
		return mm_req;
	}
	
	public void increaseMMReqByStep(Float step) {
		for(Integer i = 0; i < getNoNodes(); i++) {
			for(Integer j = 0; j < getNoNodes(); j++) {
				// If a req exists, increase it by step
				if (mm_req[i][j] > 0) {
					mm_req[i][j] += step;
				}
				// If we overshot the true request, set it to true request
				if (mm_req[i][j] > req[i][j]) {
					mm_req[i][j] = req[i][j];
				}
			}
		}
	}
	
	public void recalculateTotalReqs() {
		for(Integer i = 0; i < getNoNodes(); i++) {
			for(Integer j = 0; j < getNoNodes(); j++) {
				total_requirement += req[i][j];
				mm_total_requirement += mm_req[i][j];
			}
		}
	}

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
	
	public Float getMM_Total_requirement() {
		return mm_total_requirement;
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
