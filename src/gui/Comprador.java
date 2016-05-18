package gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import agents.Leilao;

public class Comprador extends JFrame {
	
	private static final long serialVersionUID = 1L;

	private agents.Leilao myAgent;
	private JComboBox combo, combo2;
	
	public Comprador(Leilao a) {
		super(a.getLocalName());

		setTitle("Vender Produtos");

		TitledBorder nameBorder = BorderFactory.createTitledBorder("Criar Leiloes");

		myAgent = a;
		JLabel label1= new JLabel();
		JLabel label2= new JLabel();
		JLabel label3= new JLabel();
		JLabel label4= new JLabel();
		label1.setText("N�mero de compradores");
		label2.setText("Nome do Produto a Comprar");
		label3.setText("Pre�o estimado do produto");
		label4.setText("Dinheiro Disponivel");
		
		JTextField numC = new JTextField();
		JTextField name = new JTextField();
		JTextField budget = new JTextField();
		JTextField price = new JTextField();
		
		//combo = new JComboBox((new Object[] {"Oncologia", "Pediatria", "Urgencia", "Ortopedia", "Genecologia", "Medicina Dentaria"}));
		JPanel p = new JPanel(new GridLayout(0,2));
		p.setPreferredSize(new Dimension(400, 250));
		
		combo = new JComboBox((new Object[] {"global", "local"}));
		//combo2 = new JComboBox((new Object[] {"dinamico", "estatico"}));
		
		p.add(combo);
		//textbox nome do produto
		p.add(label1);
		p.add(numC);
		//textbox quantidade
		p.add(label2);
		p.add(name);
		//textbox preco
		p.add(label3);
		p.add(price);
		//textbox preco
		p.add(label4);
		p.add(budget);
		
		p.setBorder(nameBorder);
		getContentPane().add(p);
		
		JButton addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					String comboChoice = combo.getSelectedItem().toString();
					String nomeProduto = name.getText()+";"+numC.getText()+";"+price.getText()+";"+budget.getText()+";"+comboChoice;
					myAgent.updateComprador(nomeProduto);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(Comprador.this, "Invalid values. " + e.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		p.add(addButton);
		// Make the agent terminate when the user closes
		// the GUI using the button on the upper right corner
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				myAgent.doDelete();
			}
		});

		setResizable(false);
	}

	public void showGui() {
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX = (int) screenSize.getWidth() / 2;
		int centerY = (int) screenSize.getHeight() / 2;
		setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
		super.setVisible(true);
	}

}
