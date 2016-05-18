package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
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

import agents.AgenteLeiloeiro;

public class Caixa extends JFrame{

	private static final long serialVersionUID = 1L;

	private agents.AgenteLeiloeiro myAgent;

	private JComboBox combo;
	private JLabel label1,label2,label3;
	private JTextField name, quantity, price;

	public Caixa(AgenteLeiloeiro a) {
		super(a.getLocalName());

		setTitle("Vender Produtos");

		TitledBorder nameBorder = BorderFactory.createTitledBorder("Criar Leiloes");

		myAgent = a;
		label1.setText("Nome do produto");
		label2.setText("Quantidade de leilões");
		label3.setText("Preço real do produto");
		//combo = new JComboBox((new Object[] {"Oncologia", "Pediatria", "Urgencia", "Ortopedia", "Genecologia", "Medicina Dentaria"}));
		JPanel p = new JPanel();
		p.setPreferredSize(new Dimension(400, 250));
		//textbox nome do produto
		p.add(label1);
		p.add(name);
		//textbox quantidade
		p.add(label2);
		p.add(quantity);
		//textbox preco
		p.add(label3);
		p.add(price);
		
		p.setBorder(nameBorder);
		getContentPane().add(p);

		JButton addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					String sala = combo.getSelectedItem().toString();
					myAgent.updateLeiloeiro(sala);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(Caixa.this, "Invalid values. " + e.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		myAgent.updateLeiloeiro("Triagem");
		p = new JPanel();
		p.add(addButton);
		getContentPane().add(p, BorderLayout.SOUTH);

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
