package algorithm;

import java.util.ArrayList;
import java.util.Random;

public class algorithm {
	private double price,budget;
	private boolean global;
	private int numberAuctions;
	private ArrayList<String> OfferPrice = new ArrayList<String>();
	
	public algorithm(double price, boolean global, int numberAuctions, double budget) {
		// TODO Auto-generated constructor stub
		this.price=price;
		this.global=global;
		this.numberAuctions=numberAuctions;
		this.budget=budget;
		
	}

	public double setpricelocal() {
		double priceset=price;
			//distribuicao normal abaixo do preco tipo metade da distribuição normal
			Random rdn = new Random();
			priceset=(price-(price/7*Math.abs(rdn.nextGaussian())));
		return priceset;
	}
	
	public ArrayList<String> setpriceglobal(){
		
		//primeiro preço amis alto
		OfferPrice.add(String.valueOf(price));
		OfferPrice.add(String.valueOf(price));
		
		//calcular b pela formula (é so mesmo perceber a formula e copiar) pag10  (5)   
		//cumulative distribution G(x) has bounded support [0, vmax],
		
		//ver os 3 casos do budget pag17
		//easy
		
		return OfferPrice;
	}

}
