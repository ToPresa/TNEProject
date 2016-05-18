package agents;

import gui.Caixa;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class AgenteLeiloeiro extends Agent{
	private static final long serialVersionUID = 1L;
	private Caixa myGui;
	
	protected void setup() {	
		
		// Create and show the GUI
		myGui = new Caixa(this);
		myGui.showGui();
						
	}

	public void updateLeiloeiro(final String nome) {
		addBehaviour(new OneShotBehaviour() {
			private static final long serialVersionUID = 1L;

			public void action() {
				
				ContainerController cc = getContainerController();
				AgentController ac = null;
				
				try {
					ac = cc.createNewAgent(nome, "agents.Agente" , null);
					ac.start();
				} catch (StaleProxyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
				
				//list.adiciona(sala);
				System.out.println(nome + " adicionada!");
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
			myGui.dispose();					
	
		// Printout a dismissal message
		System.out.println("Leiloeiro " + getAID().getLocalName() + " fechou!");
	}
	
}
