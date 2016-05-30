package gui;

import org.jfree.chart.ChartPanel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class Chart extends ApplicationFrame {

	public Chart( String applicationTitle , String chartTitle )
	   {
	      super(applicationTitle);
	      JFreeChart lineChart = ChartFactory.createLineChart(
	         chartTitle,
	         "Valuation","Bid",
	         createDataset(),
	         PlotOrientation.VERTICAL,
	         true,true,false);
	         
	      ChartPanel chartPanel = new ChartPanel( lineChart );
	      chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
	      setContentPane( chartPanel );
	   }

	private DefaultCategoryDataset createDataset() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(0, "bids", "0");
		dataset.addValue(0.2, "bids", "0.2");
		dataset.addValue(0.4, "bids", "0.4");
		dataset.addValue(0.6, "bids", "0.6");
		dataset.addValue(0.8, "bids", "0.8");
		dataset.addValue(1, "bids", "1");
		return dataset;
	}

	public static void main(String[] args) {
		Chart chart = new Chart("Otimal Strategies for simultaneous Vickrey Auctions", "Bids vs Valuation");

		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
	
	public StringBuffer readFile() {
		//ler ficheiro e processar dados para passar para o gráfico!
		
		StringBuffer stringBuffer = null;
		
		try {

			String currentDirFile = System.getProperty("user.dir");

			File file = new File(currentDirFile + "\\" + "resources" + "\\" +  "sells.txt");

			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			stringBuffer = new StringBuffer();
			String line;

			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line);

				String[] parts = line.split(";");
				//buscar os valores
				
				stringBuffer.append("\n");

			}

			fileReader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return stringBuffer;
	}

}
