package agents;

import gui.Caixa;
import gui.Comprador;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
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
	
	public void updateLeilao(final String dadosLeilao) {
		addBehaviour(new OneShotBehaviour() {
			private static final long serialVersionUID = 1L;

			public void action() {
				
				ContainerController cc = getContainerController();
				AgentController ac = null;
				
				try {
					ac = cc.createNewAgent(dadosLeilao, "agents.AgenteLeiloeiro" , null);
					ac.start();
				} catch (StaleProxyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
				
				System.out.println(dadosLeilao + " adicionada!");
			}
	});
	}
	
	public void updateComprador(final String dadosLeilao) {
		addBehaviour(new OneShotBehaviour() {
			private static final long serialVersionUID = 1L;

			public void action() {
				
				ContainerController cc = getContainerController();
				AgentController ac = null;
				
				String[] parts = dadosLeilao.split(";");
				int numCompradores = Integer.parseInt(parts[1]);
				//System.out.println("CRLL " + numCompradores);
				//while(numCompradores == 0) {
					try {
						ac = cc.createNewAgent(dadosLeilao, "agents.AgenteLicitante" , null);
						ac.start();
					} catch (StaleProxyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				//	numCompradores--;
				//}
				
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
