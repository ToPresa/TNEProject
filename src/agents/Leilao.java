package agents;

import gui.BarChart;
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
	private BarChart barchart;
	private int numberCompradores=0;
	private int numberVendedores=0;
	
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
				
				
				
				for(int i=0; i<number; i++) {
					try {
						ac = cc.createNewAgent("Leilao"+((i+1)+numberVendedores), "agents.AgenteLeiloeiro" , dadosLeilao);
						ac.start();
					} catch (StaleProxyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				}
				numberVendedores+=number;
				
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
			
				for(int i=0; i<number; i++) {
					try {
						
						ac = cc.createNewAgent("Licitante-"+(String) dadosLeilao[4]+"-"+((i+1)+numberCompradores), "agents.AgenteLicitante" , dadosLeilao);
						ac.start();
					} catch (StaleProxyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				}
				numberCompradores+=number;
								
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
		
		barchart.run();
		
		// Printout a dismissal message
		System.out.println("Leiloeiro " + getAID().getLocalName() + " fechou!");
	}
	
}
