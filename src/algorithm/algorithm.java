package algorithm;

import java.util.Random;

public class algorithm {
	private double price,budget;
	private boolean global;
	private int dinamico;
	
	public algorithm(double price, boolean global, int dinamico, double budget) {
		// TODO Auto-generated constructor stub
		this.price=price;
		this.global=global;
		this.dinamico=dinamico;
		this.budget=budget;
		
	}

	public double setprice() {
		double priceset=price;
		if(!global){
			
		}
		else{
			//distribuicao normal abaixo do preco tipo metade da distribuição normal
			Random rdn = new Random();
			priceset=price-(price* Math.abs(rdn.nextGaussian())/2);
		}
		return priceset;
	}

}
