import jade.core.Agent;
import jade.core.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;


public class mainAgent extends Agent {
	
	public static void main(String[] args){
		
		 mainAgent main = new mainAgent();
         mainAgent.setup();

	}
	 protected static void setup() 
     { 
		 ContainerController cc = getContainerController();
		AgentController ac = null;
		 try {
				ac = cc.createNewAgent("Agente Principal", "agents.Caixa" , null);
				ac.start();
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
     }
}
