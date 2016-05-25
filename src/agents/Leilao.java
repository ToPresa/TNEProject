package agents;

import gui.Caixa;
import gui.Comprador;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class Leilao extends Agent{
	private static final long serialVersionUID = 1L;
	private Caixa vendedorGui;
	private Comprador compradorGui;
	
	protected void setup() {	
		
		// Create and show the GUI
		vendedorGui = new Caixa(this);
		vendedorGui.showGui();
		
		compradorGui = new Comprador(this);
		compradorGui.showGui();
						
	}
	
	public void updateLeilao(final Object[] dadosLeilao) {
		addBehaviour(new OneShotBehaviour() {
			private static final long serialVersionUID = 1L;

			public void action() {
				
				ContainerController cc = getContainerController();
				AgentController ac = null;
				
				int number= Integer.parseInt((String) dadosLeilao[1]);
				
				// Update the list of seller agents
				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType("leilao");
				template.addServices(sd);
				DFAgentDescription[] result = null;
				try {
					result = DFService.search(myAgent,
							template);				
				} catch (FIPAException fe) {
					fe.printStackTrace();
				}
				
				for(int i=0; i<number; i++) {
					try {
						ac = cc.createNewAgent("Leilao"+((i+1)+result.length), "agents.AgenteLeiloeiro" , dadosLeilao);
						ac.start();
					} catch (StaleProxyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				}
				
				System.out.println(dadosLeilao + " adicionada!");
			}
	});
	}
	
	public void updateComprador(final Object[] dadosLeilao) {
		addBehaviour(new OneShotBehaviour() {
			private static final long serialVersionUID = 1L;

			public void action() {
				
				ContainerController cc = getContainerController();
				AgentController ac = null;
				
				int number= Integer.parseInt((String) dadosLeilao[1]);
				
				// Update the list of seller agents
				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType("alocar-leilao");
				template.addServices(sd);
				DFAgentDescription[] result = null;
				try {
					result = DFService.search(myAgent,
							template);				
				} catch (FIPAException fe) {
					fe.printStackTrace();
				}
				
				for(int i=0; i<number; i++) {
					try {
						ac = cc.createNewAgent("Licitante"+((i+1)+result.length), "agents.AgenteLicitante" , dadosLeilao);
						ac.start();
					} catch (StaleProxyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				}
								
				System.out.println(dadosLeilao + " adicionada!");
			}
	});
	}
		
	// Put agent clean-up operations here
	protected void takeDown() {
		// Deregister from the yellow pages
		try {
			DFService.deregister(this);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		// Close the GUI
		vendedorGui.dispose();
		compradorGui.dispose();
	
		// Printout a dismissal message
		System.out.println("Leiloeiro " + getAID().getLocalName() + " fechou!");
	}
	
}
