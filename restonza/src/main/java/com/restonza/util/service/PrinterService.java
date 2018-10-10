package com.restonza.util.service;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class PrinterService {
	public static JTable itemsTable;
	public static int total_item_count=0;
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss a";
	public static String title[] = new String[] {"Item Name","Qty","Price","Total"};
	public static String hotelName = null;
	public static String hotelAddress = null;
	public static String orderNo = null;
	public static String amt = null;
	public static String taxes = null;
	public static double taxamount = 0;
	public static String ltaxes = null;
	public static double ltaxamount = 0;
	public static int barorderount = 0;
	public static String foodamount = null;
	public static String baramount = null;
	public static void setItems(Object[][] printitem, String ahotelName, String ahotelAddress, String aOrderNo, String aAmt, StringBuilder ataxes, double taxamt) {
	        Object data[][]=printitem;
	        DefaultTableModel model = new DefaultTableModel();
	       //assume jtable has 4 columns.
	        model.addColumn(title[0]);
	        model.addColumn(title[1]);
	        model.addColumn(title[2]);
	        model.addColumn(title[3]);
	        int rowcount=printitem.length;
	        addtomodel(model, data, rowcount);
	        itemsTable = new JTable(model);
	        hotelName = ahotelName;
	        hotelAddress = ahotelAddress;
	        orderNo = aOrderNo;
	        amt = aAmt;
	        taxes = ataxes.toString();
	        taxamount = taxamt;
	}
	
	public static void setItems(Object[][] printitem, 
			String ahotelName, 
			String ahotelAddress, 
			String aOrderNo, 
			String aAmt, 
			StringBuilder ataxes, 
			StringBuilder altaxes,
			double taxamt, 
			double liquortaxamount,
			Integer barOrderCount, 
			String foodbillamt, 
			String barbillamt) {
        Object data[][]=printitem;
        DefaultTableModel model = new DefaultTableModel();
       //assume jtable has 4 columns.
        model.addColumn(title[0]);
        model.addColumn(title[1]);
        model.addColumn(title[2]);
        model.addColumn(title[3]);
        int rowcount=printitem.length;
        addtomodel(model, data, rowcount);
        itemsTable = new JTable(model);
        hotelName = ahotelName;
        hotelAddress = ahotelAddress;
        orderNo = aOrderNo;
        amt = aAmt;
        taxes = ataxes.toString();
        taxamount = taxamt;
        ltaxes = altaxes.toString();
        ltaxamount = liquortaxamount;
        barorderount = barOrderCount;
        foodamount = foodbillamt;
        baramount = barbillamt;
}

	public static void addtomodel(DefaultTableModel model,Object [][]data,int rowcount){
	        int count=0;
	        while(count < rowcount) {
	        	model.addRow(data[count]);
	        	count++;
	        }
	        if(model.getRowCount()!=rowcount)
	          addtomodel(model, data, rowcount);
	        System.out.println("Check Passed.");
	}
	          
	public Object[][] getTableData (JTable table) {
	    int itemcount=table.getRowCount();
	    System.out.println("Item Count:"+itemcount);
	    
	    DefaultTableModel dtm = (DefaultTableModel) table.getModel();
	    int nRow = dtm.getRowCount(), nCol =dtm.getColumnCount();
	    Object[][] tableData = new Object[nRow][nCol];
	    if(itemcount==nRow)                                        //check is there any data loss.
	    {
		    for (int i = 0 ; i < nRow ; i++){
		        for (int j = 0 ; j < nCol ; j++){
		            tableData[i][j] = dtm.getValueAt(i,j);           //pass data into object array.
		        }
		    }
		    if(tableData.length!=itemcount){                      //check for data losses in object array
	    	 getTableData(table);                                  //recursively call method back to collect data
		    }   
		    System.out.println("Data check passed");
	    } else{
	                                                           //collecting data again because of data loss.
	    	getTableData(table);
	    }
	   return tableData;                                       //return object array with data.
	}     

	public static PageFormat getPageFormat(PrinterJob pj){
	        PageFormat pf = pj.defaultPage();
	        Paper paper = pf.getPaper();    
	             
	        double middleHeight =total_item_count*1.0;  //dynamic----->change with the row count of jtable
	        double headerHeight = 5.0;                  //fixed----->but can be mod
	        double footerHeight = 5.0;                  //fixed----->but can be mod
	                
	        double width = convert_CM_To_PPI(7);      //printer know only point per inch.default value is 72ppi
	        double height = convert_CM_To_PPI(headerHeight+middleHeight+footerHeight); 
	        paper.setSize(width, height);
            paper.setImageableArea(
                            convert_CM_To_PPI(0.25), 
                            convert_CM_To_PPI(0.5), 
                            width - convert_CM_To_PPI(0.35), 
                            height - convert_CM_To_PPI(1));   //define boarder size    after that print area width is about 180 points
            pf.setOrientation(PageFormat.PORTRAIT);           //select orientation portrait or landscape but for this time portrait
            pf.setPaper(paper);    
            return pf;
	}
	        
	        
	public static double convert_CM_To_PPI(double cm) {            
		        return toPPI(cm * 0.393600787);            
	}

	public static double toPPI(double inch) {            
		        return inch * 72d;            
	}

	public static String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}

	public static class MyPrintable implements Printable {
		@Override
		public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
			int result = NO_SUCH_PAGE;
			if (pageIndex == 0) {
				Graphics2D g2d = (Graphics2D) graphics;
				g2d.translate((int) pageFormat.getImageableX(),
						(int) pageFormat.getImageableY());
				Font font = new Font("Monospaced", Font.PLAIN, 7);
				g2d.setFont(font);
	
				try {
					/* Draw Header */
					int y = 80;
					g2d.drawString(hotelName, 40, y);
					g2d.drawString(hotelAddress, 40, y + 10); // shift a
																		// line by
																		// adding 10
																		// to y
																		// value
					g2d.drawString(now(), 10, y + 20); // print date
					//g2d.drawString("Cashier : admin", 10, y + 30);
					g2d.drawLine(10, y + 30, 180, y + 30);
					g2d.drawString("Order No: "+ orderNo, 40, y + 40);
					/* Draw Colums */
					g2d.drawLine(10, y + 50, 180, y + 50);
					g2d.drawString(title[0], 10, y + 60);
					g2d.drawString(title[1], 90, y + 60);
					g2d.drawString(title[2], 110, y + 60);
					g2d.drawString(title[3], 150, y + 60);
					g2d.drawLine(10, y + 70, 180, y + 70);
					int cH = 0;
					TableModel mod = itemsTable.getModel();
					for (int i = 0; i < mod.getRowCount(); i++) {
						/*
						 * Assume that all parameters are in string data type for
						 * this situation All other permeative data types are
						 * accepted.
						 */
						String itemid = mod.getValueAt(i, 0).toString();
						String itemname = mod.getValueAt(i, 1).toString();
						String price = mod.getValueAt(i, 2).toString();
						String quantity = mod.getValueAt(i, 3).toString();
						cH = (y + 80) + (10 * i); // shifting drawing line
	
						g2d.drawString(itemid, 10, cH);
						g2d.drawString(itemname, 90, cH);
						g2d.drawString(price, 110, cH);
						g2d.drawString(quantity, 150, cH);
	
					}
					
					String withoutTax = String.valueOf(Math.round((100 * Double.parseDouble(amt))/(100+(taxamount))));
					g2d.drawLine(30, cH + 10, 180, cH + 10);
					g2d.drawString("Total =" + withoutTax, 90, cH + 20);
					g2d.drawString(taxes + taxamount, 90, cH + 30);
					font = new Font("Arial", Font.BOLD, 8); // changed font size
					g2d.setFont(font);
					g2d.drawString("Total Bill(Incl. Tax) =" + amt, 60, cH + 50);
					g2d.drawLine(10, cH + 60, 180, cH + 60);
					/* Footer */
					font = new Font("Arial", Font.BOLD, 10); // changed font size
					g2d.setFont(font);
					g2d.drawString("Thank You Come Again", 10, cH + 70);
					// end of the receipt
				} catch (Exception r) {
					r.printStackTrace();
				}
	
				result = PAGE_EXISTS;
			}
			return result;
		}
	}
	
	public static class MyBarPrintable implements Printable {
		@Override
		public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
			int result = NO_SUCH_PAGE;
			if (pageIndex == 0) {
				Graphics2D g2d = (Graphics2D) graphics;
				g2d.translate((int) pageFormat.getImageableX(),
						(int) pageFormat.getImageableY());
				Font font = new Font("Monospaced", Font.PLAIN, 7);
				g2d.setFont(font);
	
				try {
					/* Draw Header */
					int y = 80;
					g2d.drawString(hotelName, 40, y);
					g2d.drawString(hotelAddress, 40, y + 10); // shift a
																		// line by
																		// adding 10
																		// to y
																		// value
					g2d.drawString(now(), 10, y + 20); // print date
					//g2d.drawString("Cashier : admin", 10, y + 30);
					g2d.drawLine(10, y + 30, 180, y + 30);
					g2d.drawString("Order No: "+ orderNo, 40, y + 40);
					/* Draw Colums */
					g2d.drawLine(10, y + 50, 180, y + 50);
					g2d.drawString(title[0], 10, y + 60);
					g2d.drawString(title[1], 90, y + 60);
					g2d.drawString(title[2], 110, y + 60);
					g2d.drawString(title[3], 150, y + 60);
					g2d.drawLine(10, y + 70, 180, y + 70);
					int cH = 0;
					TableModel mod = itemsTable.getModel();
					int foodCount = mod.getRowCount() - barorderount;
					int i = 0;
					if (barorderount > 0) {
					  if (foodCount > 1) {
						for (i = 0; i < foodCount; i++) {
							/*
							 * Assume that all parameters are in string data type for
							 * this situation All other permeative data types are
							 * accepted.
							 */
							String itemid = mod.getValueAt(i, 0).toString();
							String itemname = mod.getValueAt(i, 1).toString();
							String price = mod.getValueAt(i, 2).toString();
							String quantity = mod.getValueAt(i, 3).toString();
							cH = (y + 80) + (10 * i); // shifting drawing line
		
							g2d.drawString(itemid, 10, cH);
							g2d.drawString(itemname, 90, cH);
							g2d.drawString(price, 110, cH);
							g2d.drawString(quantity, 150, cH);
		
						}
						g2d.drawLine(30, cH + 10, 180, cH + 10);
						g2d.drawString("Food Bill =" + foodamount, 10, cH + 20 );
						
						
						g2d.drawLine(10, cH + 30, 180, cH + 30);
						g2d.drawString(title[0], 10, cH + 40);
						g2d.drawString(title[1], 90, cH + 40);
						g2d.drawString(title[2], 110, cH + 40);
						g2d.drawString(title[3], 150, cH + 40);
						g2d.drawLine(10, cH + 50, 180, cH + 50);
						
						for (int j = i; j < mod.getRowCount(); j++) {
							String itemid = mod.getValueAt(j, 0).toString();
							String itemname = mod.getValueAt(j, 1).toString();
							String price = mod.getValueAt(j, 2).toString();
							String quantity = mod.getValueAt(j, 3).toString();
							cH = (y + 130) + (10 * j); // shifting drawing line
		
							g2d.drawString(itemid, 10, cH);
							g2d.drawString(itemname, 90, cH);
							g2d.drawString(price, 110, cH);
							g2d.drawString(quantity, 150, cH);
						}
						
						g2d.drawLine(30, cH + 10, 180, cH + 10);
						g2d.drawString("Liquor Bill =" + baramount, 10, cH + 20 );
						
					} else {
						g2d.drawLine(30, cH + 10, 180, cH + 10);
						for (int j = i; j < mod.getRowCount(); j++) {
							String itemid = mod.getValueAt(j, 0).toString();
							String itemname = mod.getValueAt(j, 1).toString();
							String price = mod.getValueAt(j, 2).toString();
							String quantity = mod.getValueAt(j, 3).toString();
							cH = (y + 80) + (10 * j); // shifting drawing line
		
							g2d.drawString(itemid, 10, cH);
							g2d.drawString(itemname, 90, cH);
							g2d.drawString(price, 110, cH);
							g2d.drawString(quantity, 150, cH);
						}
					}
					String withoutTax = String.valueOf(Math.round((100 * Double.parseDouble(amt))/(100+(taxamount))));
					g2d.drawLine(30, cH + 10, 180, cH + 10);
					g2d.drawString("Total =" + withoutTax, 90, cH + 20);
					g2d.drawString(taxes + taxamount, 90, cH + 30);
					g2d.drawString(ltaxes + ltaxamount, 90, cH + 40);
					font = new Font("Arial", Font.BOLD, 8); // changed font size
					g2d.setFont(font);
					g2d.drawString("Total Bill(Incl. Tax) =" + amt, 60, cH + 50);
					g2d.drawLine(10, cH + 60, 180, cH + 60);
					/* Footer */
					font = new Font("Arial", Font.BOLD, 10); // changed font size
					g2d.setFont(font);
					g2d.drawString("Thank You Come Again", 10, cH + 70);
					// end of the receipt
				} else {
					for (i = 0; i < foodCount; i++) {
						/*
						 * Assume that all parameters are in string data type for
						 * this situation All other permeative data types are
						 * accepted.
						 */
						String itemid = mod.getValueAt(i, 0).toString();
						String itemname = mod.getValueAt(i, 1).toString();
						String price = mod.getValueAt(i, 2).toString();
						String quantity = mod.getValueAt(i, 3).toString();
						cH = (y + 80) + (10 * i); // shifting drawing line
	
						g2d.drawString(itemid, 10, cH);
						g2d.drawString(itemname, 90, cH);
						g2d.drawString(price, 110, cH);
						g2d.drawString(quantity, 150, cH);
	
					}
					String withoutTax = String.valueOf(Math.round((100 * Double.parseDouble(amt))/(100+(taxamount))));
					g2d.drawLine(30, cH + 10, 180, cH + 10);
					g2d.drawString("Total =" + withoutTax, 90, cH + 20);
					g2d.drawString(taxes + taxamount, 90, cH + 30);
					font = new Font("Arial", Font.BOLD, 8); // changed font size
					g2d.setFont(font);
					g2d.drawString("Total Bill(Incl. Tax) =" + amt, 60, cH + 50);
					g2d.drawLine(10, cH + 60, 180, cH + 60);
				}
				/* Footer */
				font = new Font("Arial", Font.BOLD, 10); // changed font size
				g2d.setFont(font);
				g2d.drawString("Thank You Come Again", 10, cH + 70);
				} catch (Exception r) {
					r.printStackTrace();
				}
				result = PAGE_EXISTS;
			}
			return result;
		}
}
}