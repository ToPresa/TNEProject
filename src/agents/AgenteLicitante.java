package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class AgenteLicitante extends Agent{

	private static final long serialVersionUID = 1L;

	  AID[] leilao;
	  String nameProduct;
	  int budget, price;
	  // Put agent initializations here
	  protected void setup() {
	    
		  System.out.println("Bem Vindo Sr/Sra: " + getAID().getLocalName() + "!");
		  
		// argumentos com informaçao do comprador
		// name.getText()+";"+numC.getText()+";"+price.getText()+";"+budget.getText()+";"+comboChoice+";"+comboChoice2;
			if (getAID().getLocalName().length() > 0) {
				
				String[] parts = getAID().getLocalName().split(";");
				nameProduct = parts[0]; // name leilao			
				budget = Integer.parseInt(parts[3]);
				price = Integer.parseInt(parts[2]);
				
				DFAgentDescription dfd = new DFAgentDescription();
				dfd.setName(getAID());
				ServiceDescription sd = new ServiceDescription();
				sd.setType("alocar-leilao");
				sd.setName(nameProduct);
				dfd.addServices(sd);
				
				try {
					DFService.register(this, dfd);
				} catch (FIPAException fe) {
					fe.printStackTrace();
				}
				System.out.println("AUI CRL 1 " + nameProduct);
				// Add the behaviour serving queries from buyer agents
			    addBehaviour(new RequestsServer());
			} else {
				// Make the agent terminate
				System.out.println("Não especificou o tipo de leilão!");
				doDelete();
			}
	   
	  }
	  
	  private class RequestsServer extends CyclicBehaviour {
			
			private static final long serialVersionUID = 1L;
			
			public void action() {
				MessageTemplate mt = MessageTemplate
						.MatchPerformative(ACLMessage.INFORM);

				ACLMessage msg = myAgent.receive(mt);
				
				if (msg != null) {
					
					// CFP Message received. Process it
					String recebi = msg.getContent();
					ACLMessage info = msg.createReply();
					System.out.println("Recebi");
					
					if (msg.getConversationId() == "propostas") {

						info.setPerformative(ACLMessage.INFORM);
						info.setContent(nameProduct);
						info.setConversationId("propRecebidas");

						myAgent.send(info);
					}
				}

				else
					block();
			}
	  }

	  	
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
