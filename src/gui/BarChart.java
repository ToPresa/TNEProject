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
	 JListMedias JList = new JListMedias();
	 
	 private ArrayList<String> LocalPrices = new ArrayList<String>();
	 private ArrayList<String> GlobalPrices = new ArrayList<String>();
	 private ArrayList<String> GlobalMedia = new ArrayList<String>();
	 private ArrayList<String> NumberauctionsBuyers = new ArrayList<String>();
	
	 public BarChart(final String title) {

	        super(title);
	         
	        //read informations
	        readFile();
	        
	        // create the first dataset...
	        DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
	        for (int i = 0; i < NamesAuctions.size(); i++) {
	        	String type = typeBuyer(TypeBuyer.get(i));
	        	dataset1.addValue(Double.parseDouble(SellPrice.get(i)), "Preço Vendido", (NamesAuctions.get(i)+"-"+type) );
			}
	      
	        // create the first renderer...
	        // final CategoryLabelGenerator generator = new StandardCategoryLabelGenerator();
	        final CategoryItemRenderer renderer = new BarRenderer();
	        // renderer.setLabelGenerator(generator);
	        renderer.setItemLabelsVisible(true);
	        
	        final CategoryPlot plot = new CategoryPlot();
	        plot.setDataset(dataset1);
	        plot.setRenderer(renderer);
	        
	        plot.setDomainAxis(new CategoryAxis("Leilões"));
	        plot.setRangeAxis(new NumberAxis("Preço"));

	        plot.setOrientation(PlotOrientation.VERTICAL);
	        plot.setRangeGridlinesVisible(true);
	        plot.setDomainGridlinesVisible(true);

	        // now create the second dataset and renderer...
	        DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
	        dataset2.addValue(realPrice, "Preço Real", NamesAuctions.get(0));
	        dataset2.addValue(realPrice, "Preço Real", NamesAuctions.get(0));
	        int size = NamesAuctions.size();
	        dataset2.addValue(realPrice, "Preço Real", NamesAuctions.get(size-1));
	        dataset2.addValue(realPrice, "Preço Real", NamesAuctions.get(size-1));

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
	        // chart.setLegend(new StandardLegend());

	        // add the chart to a panel...
	        final ChartPanel chartPanel = new ChartPanel(chart);
	        chartPanel.setPreferredSize(new java.awt.Dimension(700, 470));
	        setContentPane(chartPanel);
	        
	        int tamanhoL = LocalPrices.size()/2;
	        double mediaLocal=0.0;
	   
	        for(int i=0; i < LocalPrices.size(); i+=2) {
	        	mediaLocal += ( Double.parseDouble (LocalPrices.get(i+1)) / Double.parseDouble (LocalPrices.get(i)) );
	        	//System.out.println("JA CRL : " + mediaLocal );
	        }
	      
	        while(GlobalPrices.size() != 0) {
	        	
		        double mediaGlobal = 0.0;
		        int count=0;
		        String globalAtual=GlobalPrices.get(0);
		        int i= 0;
	        	while(i <= GlobalPrices.size()-2) {
	        		
		        	if(GlobalPrices.get(i).equals(globalAtual)){
		        		mediaGlobal += ( Double.parseDouble (GlobalPrices.get(i+1)) / realPrice );
		        		//System.out.println("iii : " + globalAtual);
		        		GlobalPrices.remove(i);
		      
		        		GlobalPrices.remove(i); 
		        		i=0;
		        		count++;
		        	}
		        	else
		        			i+=2;
		        }
	        	
	        	NumberauctionsBuyers.add(globalAtual);
	        	NumberauctionsBuyers.add(String.valueOf(count));
	        	GlobalMedia.add(String.valueOf(mediaGlobal));
	        	
	        }
	        
	        
	       // processar media global
	        int sizeGlobal = GlobalMedia.size();
	        double mediaGlobalTotal =0.0;
	        for(int j=0; j < GlobalMedia.size(); j++) {
	        	mediaGlobalTotal += Double.parseDouble(GlobalMedia.get(j));
	        }
	        
	        double MediaGlobal = mediaGlobalTotal/sizeGlobal;
	        double MediaLocal = (mediaLocal/tamanhoL);
	        
	        
	        System.out.println("MEDIA LOCAL : " + MediaLocal + "  MEDIA GLOBAL : " + MediaGlobal);
	        JList.run(String.valueOf(MediaLocal), String.valueOf(MediaGlobal), NumberauctionsBuyers);

	    }

	    public static void main(final String[] args) {

	        final BarChart demo = new BarChart("Leilões Estatisticas");
	        demo.pack();
	        RefineryUtilities.centerFrameOnScreen(demo);
	        demo.setVisible(true);

	    }
	    	 
	 /*	public void run() {

	        final BarChart demo = new BarChart("Leilões Estatisticas");
	        demo.pack();
	        RefineryUtilities.centerFrameOnScreen(demo);
	        demo.setVisible(true);

	    }*/
	    
	    public String typeBuyer(String type) {
	    	if (type.equals("local")) {
	    		return "L";
	    	}
	    	else
	    		return "G";
	    }
	    
		public void readFile() {
			//ler ficheiro e processar dados para passar para o gráfico!
			//nomeLeilao;nomeProduto;preçoGanhou;preçoPagou;PreçoReal;Tipo(local, global)
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
					String[] nameGlobal = parts[5].split("-");
					//buscar os valores
					 NamesAuctions.add(parts[0]);
					 SellPrice.add(parts[3]); 
					 TypeBuyer.add(nameGlobal[0]);
					 realPrice = Double.parseDouble(parts[4]);
					 nameProductSelled = parts[1];
					 stringBuffer.append("\n");
					 
					 
							 
					 //System.out.println("AI : " + TypeBuyer);
					 
					 if(TypeBuyer.get(TypeBuyer.size() - 1).equals("local")){
						 //System.out.println("AQUI LOCAL");
						 LocalPrices.add(String.valueOf(realPrice));
						 LocalPrices.add(String.valueOf(SellPrice.get(SellPrice.size() - 1)));
					 }
					 else {
						 //System.out.println("AQUI GLOBAL");
						 GlobalPrices.add(String.valueOf(nameGlobal[1]));
						 GlobalPrices.add(String.valueOf(SellPrice.get(SellPrice.size() - 1)));
					 }

				}
				//System.out.println(NamesAuctions.size() + "  " + SellPrice.size() + "  " + TypeBuyer.size());
				
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
