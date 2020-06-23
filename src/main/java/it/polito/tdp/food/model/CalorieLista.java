package it.polito.tdp.food.model;

public class CalorieLista implements Comparable<CalorieLista> {
	Food alimento;
	double peso;
	
	public CalorieLista(Food alimento, double peso) {
		super();
		this.alimento = alimento;
		this.peso = peso;
	}

	public Food getAlimento() {
		return alimento;
	}

	public double getPeso() {
		return peso;
	}

	@Override
	public int compareTo(CalorieLista o) {
		// TODO Auto-generated method stub
		double temp= this.peso-o.peso;
		if(temp>0) return 1;
		if(temp<0) return -1;
		return  0;
	}

	@Override
	public String toString() {
		return alimento.getDisplay_name();
	}

}
