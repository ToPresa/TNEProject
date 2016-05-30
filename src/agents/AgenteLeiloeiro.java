package agents;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

	private AID[] leilao;
	private AID bestbuyer = null;
	private String name;
	private double bestprice=0.0, secondbestprice=0.0 , realpriceproduct = 0.0;
	private boolean productoffer = false;
	private int number;
	
	// Put agent initializations here
	protected void setup() {
		
		System.out.println("Bem Vindo Sr/Sra: " + getAID().getLocalName() + "!");
		
		// argumentos com informaçao do leilao
		// name.getText()+";"+quantity.getText()+";"+price.getText();
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			
			number = Integer.parseInt((String) args[1]);
			name = (String) args[0]; // name leilao			
			realpriceproduct= Integer.parseInt((String) args[2]);
					
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
			int Min = 8000;
			int Max = 12000;
			int time = Min + (int)(Math.random() * ((Max - Min) + 1));
			addBehaviour(new TickerBehaviour(this, time) {
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
							//System.out.println("cenas"+leilao[i].getName());
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
				//System.out.println("ENVIA PROPOSTAS");
				//Solicitar propostas aos compradores
				ACLMessage cfp = new ACLMessage(ACLMessage.INFORM);
				
				
				for (int i = 0; i < leilao.length; ++i) {
					//System.out.println("este e o gajo que quero falar  "+leilao[i]);
					cfp.addReceiver(leilao[i]);
					
				}
				cfp.setContent(name);
				cfp.setConversationId("propostas");
				cfp.setReplyWith("cfp" + System.currentTimeMillis()); // Unique value
				
				myAgent.send(cfp);
				//System.out.println("recebi isto   ");
				step = 1;
				break;

			case 1:
				
				//Receber resposta da triagem que diz respeito ao meu estado de saúde
				MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
				ACLMessage reply = myAgent.receive(mt);
				
				String[] parts = reply.getContent().toString().split(";");

				double price = Double.parseDouble(parts[1]);
				System.out.println(price);
				AID nameComprador = reply.getSender();
				
				//System.out.println("PREÇO OFERICOD: " + price + " E O ID CRL " + reply.getConversationId() +  " TAMANHO DO FDO: " +leilao.length);	
				
				if (reply != null && reply.getConversationId().equals("propRecebidas")) {
								
					if(bestbuyer == null || price > bestprice) {
						secondbestprice = bestprice;
						bestprice = price;
						bestbuyer = nameComprador;
					}
					else if (price > secondbestprice){
						secondbestprice = price;
					}
					//System.out.println(totalBuyers + " " + leilao.length + "  CRLLL PREÇO ESCOLHIDO : " + price + " ao " + nameComprador);
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
		      System.out.println("VENDI ao " + bestbuyer + " ofereceu " +bestprice  +" pagou " + secondbestprice);
		      productoffer = true;
		      step = 3;
		      doDelete();
		      break;
			}
		}

		@Override
		public boolean done() {
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
	
	public void writeFile(String comprador, double secondbestprice) {
		
		String currentDirFile = System.getProperty("user.dir");

		File file = new File(currentDirFile + "\\" + "resources" + "\\"
				+ "sells.txt");

		// if file doesnt exists, then create it
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		String content = comprador + ";" + secondbestprice + ";" + realpriceproduct + "\n";
		
		FileWriter fw;
		try {
			fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		System.out.println("Writing Done");
		
	}
}
