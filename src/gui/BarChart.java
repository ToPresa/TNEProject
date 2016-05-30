package gui;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.TextAnchor;


public class BarChart extends ApplicationFrame
{

	private static final long serialVersionUID = 1L;
	
	private ArrayList<String> NamesAuctions = new ArrayList<String>();
	 private ArrayList<String> SellPrice = new ArrayList<String>();
	 private ArrayList<String> TypeBuyer = new ArrayList<String>();
	 private double realPrice = 0.0;
	 private String nameProductSelled;
	
	 public BarChart(final String title) {

	        super(title);
	         
	        //read informations
	        readFile();
	        
	        // create the first dataset...
	        DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
	        for (int i = 0; i < NamesAuctions.size(); i++) {
	        	String type = typeBuyer(TypeBuyer.get(i));
	        	dataset1.addValue(Double.parseDouble(SellPrice.get(i)), "Pre�o Vendido", (NamesAuctions.get(i)+"-"+type) );
			}
	      
	        // create the first renderer...
	  //      final CategoryLabelGenerator generator = new StandardCategoryLabelGenerator();
	        final CategoryItemRenderer renderer = new BarRenderer();
	    //    renderer.setLabelGenerator(generator);
	        renderer.setItemLabelsVisible(true);
	        
	        final CategoryPlot plot = new CategoryPlot();
	        plot.setDataset(dataset1);
	        plot.setRenderer(renderer);
	        
	        plot.setDomainAxis(new CategoryAxis("Leil�es"));
	        plot.setRangeAxis(new NumberAxis("Pre�o"));

	        plot.setOrientation(PlotOrientation.VERTICAL);
	        plot.setRangeGridlinesVisible(true);
	        plot.setDomainGridlinesVisible(true);

	        // now create the second dataset and renderer...
	        DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
	        dataset2.addValue(realPrice, "Pre�o Real", NamesAuctions.get(0));
	        dataset2.addValue(realPrice, "Pre�o Real", NamesAuctions.get(0));
	        int size = NamesAuctions.size();
	        dataset2.addValue(realPrice, "Pre�o Real", NamesAuctions.get(size-1));
	        dataset2.addValue(realPrice, "Pre�o Real", NamesAuctions.get(size-1));

	        final CategoryItemRenderer renderer2 = new LineAndShapeRenderer();
	        plot.setDataset(1, dataset2);
	        plot.setRenderer(1, renderer2);

	     
	        renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.TOP_CENTER,TextAnchor.HALF_ASCENT_CENTER,0D));
	       
	        // change the rendering order so the primary dataset appears "behind" the 
	        // other datasets...
	        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
	        
	        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
	        final JFreeChart chart = new JFreeChart(plot);
	        chart.setTitle("Resultados Produto -> " + nameProductSelled);
	      //  chart.setLegend(new StandardLegend());

	        // add the chart to a panel...
	        final ChartPanel chartPanel = new ChartPanel(chart);
	        chartPanel.setPreferredSize(new java.awt.Dimension(700, 470));
	        setContentPane(chartPanel);

	    }

	    public static void main(final String[] args) {

	        final BarChart demo = new BarChart("Leil�es Estatisticas");
	        demo.pack();
	        RefineryUtilities.centerFrameOnScreen(demo);
	        demo.setVisible(true);

	    }
	    
	    public String typeBuyer(String type) {
	    	if (type.equals("Global")) {
	    		return "G";
	    	}
	    	else
	    		return "L";
	    }
	    
		public void readFile() {
			//ler ficheiro e processar dados para passar para o gr�fico!
			//nomeLeilao;nomeProduto;pre�oGanhou;pre�oPagou;Pre�oReal;Tipo(local, global)
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
					 NamesAuctions.add(parts[0]);
					 SellPrice.add(parts[2]); 
					 TypeBuyer.add(parts[5]);
					 realPrice = Double.parseDouble(parts[4]);
					 nameProductSelled = parts[1];
					 stringBuffer.append("\n");

				}
				System.out.println(NamesAuctions.size() + "  " + SellPrice.size() + "  " + TypeBuyer.size());
				
				// Dar delete dos dados do file
				PrintWriter writer = new PrintWriter(file);
				writer.print("");
				writer.close();
				
				fileReader.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	   
}