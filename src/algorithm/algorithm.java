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
			Random rdn = new Random();
			priceset=(price-(price/7*Math.abs(rdn.nextGaussian())));
		return priceset;
	}
	
	public ArrayList<String> setpriceglobal(){
		
		double valor1=1;
		for(int i=0;i<numberAuctions;i++){
			valor1*=(G(setpricelocal()));
		}
		double newprice=valor1*price;
		
		if(newprice/price>0.5){
			double valor2 = setpricelocal();
			OfferPrice.add(String.valueOf(valor2));
		}
		OfferPrice.add(String.valueOf(newprice));
		
		return OfferPrice;
	}
	private double G(double x){
		return Math.pow(x, 1.0/3.0)/(Math.pow(Math.E, Math.log(price)/3));
	}

}
