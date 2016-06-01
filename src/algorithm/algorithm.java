package algorithm;

import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.math3.distribution.NormalDistribution;
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
		//OfferPrice.add(String.valueOf(price));
		//OfferPrice.add(String.valueOf(price));
		//NormalDistribution cumulative = new NormalDistribution();	
		//cumulative.cumulativeProbability(x);
		double cenas=1;
		for(int i=0;i<numberAuctions;i++){
			cenas*=(G(setpricelocal()));
		}
		double newprice=cenas*price;
		System.out.println("preço global " + newprice);
		if(newprice/price>0.5){
			double coiso = setpricelocal();
			System.out.println("preço global2 " + coiso);
			OfferPrice.add(String.valueOf(coiso));
		}
		OfferPrice.add(String.valueOf(newprice));
		
		//calcular b pela formula (é so mesmo perceber a formula e copiar) pag10  (5)   
		//cumulative distribution G(x) has bounded support [0, vmax],
		
		//ver os 3 casos do budget pag17
		//easy
		
		return OfferPrice;
	}
	private double G(double x){
		return Math.pow(x, 1.0/3.0)/(Math.pow(Math.E, Math.log(price)/3));
	}

}
