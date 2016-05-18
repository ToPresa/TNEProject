package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class AgenteLeiloeiro extends Agent{
  
  private static final long serialVersionUID = 1L;

  AID[] leilao;

  // Put agent initializations here
  protected void setup() {
    
    // Registar o serviço da sala nas "paginas-amarelas"
    DFAgentDescription dfd = new DFAgentDescription();
    dfd.setName(getAID());
    ServiceDescription sd = new ServiceDescription();
    sd.setType("alocar-recursos");
    sd.setName("Serviço Hospitalar");
    dfd.addServices(sd);

    try {
      DFService.register(this, dfd);
    } catch (FIPAException fe) {
      fe.printStackTrace();
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
