package gui;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class JListMedias {
	public void run(String mediaL, String mediaG, ArrayList<String> Compradores) {
		
		//String mediaL = "mediaL" , mediaG = "mediaG";
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("JList Test");
		frame.setLayout(new GridLayout(0,1));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		String[] selections = { "Média Local: " , mediaL," " ,"Média Global: " ,mediaG, " "  };
		JList list = new JList(selections);
		list.setSelectedIndex(1);
		//System.out.println(list.getSelectedValue());
		frame.add(new JScrollPane(list));
		for(int i=0; i< Compradores.size();i+=2){
			String[] select = { "Nome Global: " , Compradores.get(i) ,"Numero Produtos Comprados: " ,Compradores.get(i+1), " "  };
			JList list2 = new JList(select);
			list2.setSelectedIndex(i+2);
			frame.add(new JScrollPane(list2));
		}
	
		
		frame.pack();
		frame.setSize(500,400);
		frame.setVisible(true);
	}

}
