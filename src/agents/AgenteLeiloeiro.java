package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class AgenteLeiloeiro extends Agent {

	private static final long serialVersionUID = 1L;

	private AID[] leilao;
	private AID bestbuyer = null;
	private String name;
	private int bestprice;
	private boolean productoffer = false;
	private int number;
	
	// Put agent initializations here
	protected void setup() {
		
		System.out.println("Bem Vindo Sr/Sra: " + getAID().getLocalName() + "!");
		
		// argumentos com informaçao do leilao
		// name.getText()+";"+quantity.getText()+";"+price.getText();
		if (getAID().getLocalName().length() > 0) {
			
			String[] parts = getAID().getLocalName().split(";");
			number = Integer.parseInt(parts[1]);
			name = parts[0]; // name leilao			
			System.out.println("este e o meu nome: "+number);
			//for(int i=0;i<number;i++){
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			ServiceDescription sd = new ServiceDescription();
			sd.setType("leilao");
			sd.setName(name);
			dfd.addServices(sd);
			
			try {
				DFService.register(this, dfd);
			} catch (FIPAException fe) {
				fe.printStackTrace();
			}
			
			// Schedules a request to Hospital agents every 10s
			addBehaviour(new TickerBehaviour(this, 10000) {
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
							System.out.println("cenas"+leilao[i].getName());
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
		private int step =0;
		private int totalBuyers = 0;
		

		public void action() {
			
			if(productoffer == true) {
				return;
			}
			
			switch (step) {

			case 0: 
				System.out.println("ENVIA PROPOSTAS");
				//Solicitar propostas aos compradores
				ACLMessage cfp = new ACLMessage(ACLMessage.INFORM);
				
				
				for (int i = 0; i < leilao.length; ++i) {
					System.out.println("este e o gajo que quero falar  "+leilao[0]);
					cfp.addReceiver(leilao[i]);
					
				}
				cfp.setContent(name);
				cfp.setConversationId("propostas");
				cfp.setReplyWith("cfp" + System.currentTimeMillis()); // Unique value
				
				myAgent.send(cfp);
			System.out.println("recebi isto   ");
				step = 1;
				break;

			case 1:
				System.out.println("oasdasdasdasd");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//Receber resposta da triagem que diz respeito ao meu estado de saúde
				MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
				ACLMessage reply = myAgent.receive(mt);
				
				String[] parts = reply.toString().split(";");
				int price = Integer.parseInt(parts[1]);
				AID nameComprador = reply.getSender();
				
				System.out.println("PREÇO OFERICOD: " + price + " E O ID CRL " + reply.getConversationId() +  " TAMANHO DO FDO: " +leilao.length);	
				
				if (reply != null && reply.getConversationId().equals("propRecebidas")) {
								
					if(bestbuyer == null || price > bestprice) {
						bestprice = price;
						bestbuyer = nameComprador;
					}
					System.out.println(totalBuyers + " " + leilao.length + "  CRLLL PREÇO ESCOLHIDO : " + price + " ao " + nameComprador);
					totalBuyers++;
					if(totalBuyers >= (leilao.length) ){
						step=2;
					}
				}
				else {
					block();
				}
				break;
			case 2: 
			  //Send ao buyer with best offer
		      System.out.println("VENDI ao " + bestbuyer + " pagou " + bestprice);
		      productoffer = true;
		      step = 3;
		      break;
			}
		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			if (step == 1 && bestbuyer == null) {
		  		System.out.println("Attempt failed to sell: "+name);
		  	}
		    return ( step == 3);
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
