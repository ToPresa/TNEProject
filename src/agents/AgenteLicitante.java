package agents;

import java.util.ArrayList;

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

public class AgenteLicitante extends Agent {

	private static final long serialVersionUID = 1L;

	private AID[] leilao, sdd;
	private String nameProduct, name;
	private double budget, price, dinamic, globalcurrentacc = 0;
	private boolean global, proposta, first = true;
	private int numberAuctions = 0;
	private ArrayList<String> OfferPrice = new ArrayList<String>();

	// Put agent initializations here
	protected void setup() {

		System.out.println("Bem Vindo Sr/Sra: " + getAID().getLocalName() + "!");
		proposta = false;
		String[] nameAux = (getAID().getLocalName()).split("-");
		name = nameAux[1]+"-"+nameAux[2];

		// argumentos com informaçao do comprador
		// name.getText()+";"+numC.getText()+";"+price.getText()+";"+budget.getText()+";"+comboChoice+";"+comboChoice2;
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			nameProduct = (String) args[0]; // name leilao
			budget = Float.parseFloat((String) args[3]);
			if (((String) args[4]).equals("global"))
				global = true;
			else
				global = false;

			try {
				price = Integer.parseInt(((String) args[2]));

				// System.out.println("Value parsed :"+i);

			} catch (NumberFormatException ex) {

				System.err.println("Ilegal input");

			}
			// price = Integer.parseInt(((String) args[2]).trim());

			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			ServiceDescription sd = new ServiceDescription();
			sd.setType("licitante");
			sd.setName(getAID().getLocalName());
			dfd.addServices(sd);

			try {
				DFService.register(this, dfd);
			} catch (FIPAException fe) {
				fe.printStackTrace();
			}
			//////////////////////////////////////////////////////////
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd1 = new ServiceDescription();
			sd1.setType("leilao-" + nameProduct);
			template.addServices(sd1);

			try {
				DFAgentDescription[] result = DFService.search(this, template);
				// System.out.println("Encontrei estes recursos:");

				numberAuctions = result.length;

			} catch (FIPAException fe) {
				fe.printStackTrace();
			}

			algorithm pr = new algorithm(price, global, numberAuctions, budget);
			if (global) {
				OfferPrice = pr.setpriceglobal();
			} else
				price = pr.setpricelocal();

			// System.out.println("AUI CRL 1 " + nameProduct);
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
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);

			ACLMessage msg = myAgent.receive(mt);

			if (msg != null) {

				// CFP Message received. Process it
				String recebi = msg.getContent();
				ACLMessage info = msg.createReply();

				double numberrandom = 0;
				if (!global) {
					numberrandom = 0 + (Math.random() * (100 - 0));
				}
				//System.out.println(msg.getConversationId());
				if (msg.getConversationId().equals("resultados")) {
					
					String[] parts = msg.getContent().toString().split(";");
					globalcurrentacc -= price;
					if (Double.parseDouble(parts[0]) == price) {
						System.out.println("COmPREIII " + parts[0]);
						globalcurrentacc += Double.parseDouble(parts[1]);
					}
				}

				else if (msg.getConversationId() == "propostas" && recebi.equals(nameProduct)) {
					if ((numberrandom < 5 && !proposta && !global) || (global && (budget > globalcurrentacc))
							|| (global && first) || (global && budget == 0)) {
						first = false;
						info.setPerformative(ACLMessage.INFORM);
						// mensagem do static ou dinamic

						if (global && OfferPrice.size() > 1) {
							price = Double.parseDouble(OfferPrice.get(0));
							OfferPrice.remove(0);
						} else if (global && OfferPrice.size() == 1)
							price = Double.parseDouble(OfferPrice.get(0));

						if (global && budget < globalcurrentacc + price && budget != 0)
							price = budget - globalcurrentacc;

						globalcurrentacc += price;
						info.setContent(nameProduct + ";" + price + ";" + name);
						info.setConversationId("propRecebidas");
						// System.out.println("Quero entrar neste leilao: " +
						// nameProduct + " PREÇO " + price);
						myAgent.send(info);

						if (!global) {
							proposta = true;
						}
					} else {
						if (msg.getConversationId() == "propostas" && recebi.equals(nameProduct)) {
							info.setPerformative(ACLMessage.INFORM);
							// mensagem do static ou dinamic

							info.setContent(nameProduct + ";" + "0.0" + ";" + name);
							info.setConversationId("propRecebidas");
							// System.out.println("Quero entrar neste leilao: "
							// + nameProduct + " PREÇO " + price);
							myAgent.send(info);

						}
					}
				}

			} else
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
		System.out.println("Licitante " + getAID().getLocalName() + " fechou!");
	}
}
