package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class AgenteLeiloeiro extends Agent {

	private static final long serialVersionUID = 1L;

	AID[] leilao;
	String name;

	// Put agent initializations here
	protected void setup() {
		
		System.out.println("Bem Vindo Sr/Sra: " + getAID().getLocalName() + "!");
		
		// argumentos com informaçao do leilao
		// name.getText()+";"+quantity.getText()+";"+price.getText();
		if (getAID().getLocalName().length() > 0) {
			
			String[] parts = getAID().getLocalName().split(";");
			name = parts[0]; // name leilao			
			
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			ServiceDescription sd = new ServiceDescription();
			sd.setType("alocar-leilao");
			sd.setName(name);
			dfd.addServices(sd);
			
			try {
				DFService.register(this, dfd);
			} catch (FIPAException fe) {
				fe.printStackTrace();
			}
			
			// Schedules a request to Hospital agents every 10s
			addBehaviour(new TickerBehaviour(this, 5000) {
				private static final long serialVersionUID = 1L;

				protected void onTick() {
					System.out.println("A tentar vender: "+ name);
					// Update the list of seller agents
					DFAgentDescription template = new DFAgentDescription();
					ServiceDescription sd = new ServiceDescription();
					sd.setType("alocar-leilao");
					template.addServices(sd);
					
					try {
						DFAgentDescription[] result = DFService.search(myAgent,
								template);
						//System.out.println("Encontrei estes recursos:");
						leilao = new AID[result.length];
						for (int i = 0; i < result.length; ++i) {
							leilao[i] = result[i].getName();
							//System.out.println(leilao[i].getName());
						}
					} catch (FIPAException fe) {
						fe.printStackTrace();
					}
					
					// Perform the request
					// Add the behaviour serving queries from buyer agents
				    addBehaviour(new OfferRequestsServer());
				}
			});
			
		} else {
			// Make the agent terminate
			System.out.println("Não especificou o tipo de leilão!");
			doDelete();
		}

	}
	
	private class OfferRequestsServer extends Behaviour {
		
		private static final long serialVersionUID = 1L;
		int step =0;
		float estado =0;
		
		public void action() {
			switch (step) {

			case 0: 
				System.out.println("ENVIA PROPOSTAS");
				//Solicitar propostas à aos compradores
				ACLMessage cfp = new ACLMessage(ACLMessage.INFORM);
				cfp.setContent(name);
				cfp.setConversationId("propostas");
				cfp.setReplyWith("cfp" + System.currentTimeMillis()); // Unique
																		// value
				for (int i = 0; i < leilao.length; ++i) {
					cfp.addReceiver(leilao[i]);		
					myAgent.send(cfp);
				}
			
				step = 1;
				break;

			case 1:
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//Receber resposta da triagem que diz respeito ao meu estado de saúde
				MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
				ACLMessage reply = myAgent.receive(mt);
				
				if (reply != null && reply.getConversationId() == "propRecebidas") {
					estado=1;
					System.out.println("Leilao vendido " + name);
				}

				else {
					block();
				}
			}
		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return (estado!=0);
		}
	} // End of inner class OfferRequestsServer


	// Put agent clean-up operations here
	protected void takeDown() {
		// Deregister from the yellow pages
		try {
			DFService.deregister(this);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		// Printout a dismissal message

		System.out.println("Leilao " + getAID().getLocalName() + " fechou!");
	}
}
