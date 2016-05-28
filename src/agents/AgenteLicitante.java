package agents;

import algorithm.algorithm;
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

	  private AID[] leilao;
	  private String nameProduct;
	  private double budget, price, dinamic;
	  private boolean global;
	  
	  // Put agent initializations here
	  protected void setup() {
	    
		  System.out.println("Bem Vindo Sr/Sra: " + getAID().getLocalName() + "!");
		  
		// argumentos com informaçao do comprador
		// name.getText()+";"+numC.getText()+";"+price.getText()+";"+budget.getText()+";"+comboChoice+";"+comboChoice2;
		  Object[] args = getArguments();
		  if (args != null && args.length > 0) {
			for(int i=0;i<args.length;i++){
				System.out.println(args[i]);
			}
			nameProduct = (String) args[0]; // name leilao			
			budget = Float.parseFloat((String) args[3]);
			if(((String) args[4]).equals("global"))
				global=true;
			else
				global=false;
			price = Integer.parseInt((String) args[2]);
			
			
			
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			ServiceDescription sd = new ServiceDescription();
			sd.setType("alocar-leilao");
			//System.out.println("qwqwqwqw: "+getAID().getLocalName());
			sd.setName(getAID().getLocalName());
			dfd.addServices(sd);
			
			try {
				DFService.register(this, dfd);
			} catch (FIPAException fe) {
				fe.printStackTrace();
			}
			
			//System.out.println("AUI CRL 1 " + nameProduct);
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
					
					//System.out.println("Recebi ISTO : " + msg.getConversationId() + " EEE " + nameProduct);
					//System.out.println("olaaaaaaa: "+recebi);
					if (msg.getConversationId() == "propostas" && recebi.equals(nameProduct)) {
						info.setPerformative(ACLMessage.INFORM);
						//mensagem do static ou dinamic
						algorithm pr = new algorithm(price,global,0,budget);
						price=pr.setprice();
						info.setContent(nameProduct+";"+price);
						info.setConversationId("propRecebidas");
						//System.out.println("Quero entrar neste leilao: " + nameProduct + " PREÇO " + price);
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
