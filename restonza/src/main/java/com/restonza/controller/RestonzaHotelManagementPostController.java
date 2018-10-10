/**
 * 
 */
package com.restonza.controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.DottedLineSeparator;
import com.restonza.dao.HotelDetails;
import com.restonza.dao.TableDetails;
import com.restonza.dao.UserDetails;
import com.restonza.dao.repository.HotelAnalyzerRepository;
import com.restonza.dao.repository.HotelDetailsRepository;
import com.restonza.dao.repository.HotelDishesRepositories;
import com.restonza.dao.repository.HotelSubscriptionPlanDetailsRepository;
import com.restonza.dao.repository.TableDetailsRepository;
import com.restonza.dao.repository.UserDetailsRepository;
import com.restonza.util.service.RestonzaUtility;
import com.restonza.vo.BulkRequest;
import com.restonza.vo.DetailReportVO;
import com.restonza.vo.DishesVO;
import com.restonza.vo.HotelDetailsVO;
import com.restonza.vo.HotelDishMenuPdf;
import com.restonza.vo.HotelReportVO;
import com.restonza.vo.ReportRequest;
import com.restonza.vo.RestonzaRestResponseVO;
import com.restonza.vo.SalesReportVO;
import com.restonza.vo.TableVO;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * @author flex-grow developers
 *
 */
@RestController
@RequestMapping("/restonza")
public class RestonzaHotelManagementPostController {
	
	@Autowired
    private ApplicationContext appContext;
	
	@Autowired
	private HotelSubscriptionPlanDetailsRepository hotelSubscriptionPlanDetailsRepository;
	
	@Autowired
	private HotelDetailsRepository hotelDetailsRepository;
	
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
	@Autowired
	private TableDetailsRepository tableDetailsRepository;
	
	@Autowired
	private HotelAnalyzerRepository hotelAnalyzerRepository;
	
	@Autowired
	private HotelDishesRepositories dishesRepository;
	
	private static final String APPLICATION_PDF = "application/pdf";
	
	private static final String REPORTURL = "/var";
	/**
	 * post request for adding hotel
	 * @param hotelDetailsVO
	 * @return
	 */
	@PostMapping("/addHotel")
	public @ResponseBody RestonzaRestResponseVO executeAddHotel(@RequestBody HotelDetailsVO hotelDetailsVO) {
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		HotelDetails hotelDetails = new HotelDetails();
		UserDetails userDetails = new UserDetails();
		int offset = hotelSubscriptionPlanDetailsRepository.getOffset(hotelDetailsVO.getHotelsubscription().split("-")[0]);
		try {
			if (hotelDetailsVO.getHotelname() != null) {
				List<HotelDetails> listofHotel = hotelDetailsRepository.findName(hotelDetailsVO.getHotelname());
				if (listofHotel.isEmpty()) {
					hotelDetails = hotelDetails.prepareHotelDetails(hotelDetails, hotelDetailsVO, offset);
					hotelDetails = hotelDetailsRepository.save(hotelDetails);
					int hotel_id = hotelDetails.getId();
					if (userDetailsRepository.isExist(hotelDetailsVO.getUsername()) < 1) {
						userDetails = userDetails.prepareAdminUserDetails(userDetails, hotelDetailsVO, hotel_id);
						userDetailsRepository.save(userDetails);
						restonzaRestResponseVO = new RestonzaRestResponseVO("success", "Successfully added hotel");
					} else {
						hotelDetailsRepository.delete(hotel_id);
						restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Username Already exists");
					}
					
				} else {
					restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Hotel Already exists");
				}
			} else {
				restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Error occurred during adding hotel");
			}
			
		} catch (Exception e) {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Error occurred during adding hotel");
		}
		return restonzaRestResponseVO;
	}
	
	/**
	 * post request for adding hotel
	 * @param hotelDetailsVO
	 * @return
	 */
	@PostMapping("/updateHotel")
	public @ResponseBody RestonzaRestResponseVO executeUpdateHotel(@RequestBody HotelDetailsVO hotelDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		int hotel_id = Integer.parseInt(hotelDetailsVO.getId());
		HotelDetails hotelDetails = hotelDetailsRepository.findOne(hotel_id);
		int offset = hotelSubscriptionPlanDetailsRepository.getOffset(hotelDetailsVO.getHotelsubscription().split("-")[0]);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			hotelDetails.setHotel_type(hotelDetailsVO.getHoteltype());
			Date date = sdf.parse(hotelDetailsVO.getStartdate());
			hotelDetails.setHotel_start_date(date);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, offset);
			date = cal.getTime();
			hotelDetails.setHotel_end_date(date);
			hotelDetails.setHotel_subscription_plan(hotelDetailsVO.getHotelsubscription());
			String status = hotelDetailsVO.getStatus();
			String barPriviledge = hotelDetailsVO.getBarPriviledege();
			if (status.equals("active")) {
				hotelDetails.setHotel_enabled(true);
				hotelDetails.setHotel_expire(false);
				userDetailsRepository.updateUserStatus(true, hotel_id);
			} else {
				hotelDetails.setHotel_enabled(false);
				userDetailsRepository.updateUserStatus(false, hotel_id);
			}
			if (barPriviledge.equals("active")) {
				hotelDetails.setIsbar(true);
				userDetailsRepository.updateUserBarPriviledge("restrobaradmin", hotel_id);
			} else {
				hotelDetails.setIsbar(false);
				userDetailsRepository.updateUserBarPriviledge("admin", hotel_id);
			}
			hotelDetails = hotelDetailsRepository.save(hotelDetails);
			restonzaRestResponseVO = new RestonzaRestResponseVO("success", "Successfully updated hotel");
		} catch (Exception e) {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Error occurred during updating hotel");
		}
		return restonzaRestResponseVO;
	}
	
	/**
	 * post request for getting hotel details
	 * @param hotelDetailsVO
	 * @return
	 */
	@PostMapping("/getHotels")
	public @ResponseBody RestonzaRestResponseVO executeGetAllHotels(@RequestBody HotelDetailsVO hotelDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		List<HotelDetails> hotelDetails = hotelDetailsRepository.findAllHotelsForSuperAdmin();
		List<HotelDetailsVO> hotelList = new ArrayList<HotelDetailsVO>();
		HotelDetailsVO hotelDetailVO = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		for (HotelDetails hotelDetail : hotelDetails) {
			String barPriviledge = hotelDetail.isIsbar() ? "active" : "inactive";
			hotelDetailVO = new HotelDetailsVO(String.valueOf(hotelDetail.getId()), hotelDetail.getHotel_name(), hotelDetail.getHotel_type(), sdf.format(hotelDetail.getHotel_start_date()), sdf.format(hotelDetail.getHotel_end_date()),hotelDetail.getHotel_subscription_plan(),RestonzaUtility.getStatus(hotelDetail.isHotel_enabled(), hotelDetail.isHotel_expire()), barPriviledge);
			hotelList.add(hotelDetailVO);
		}
		restonzaRestResponseVO = new RestonzaRestResponseVO("success", hotelList);
		return restonzaRestResponseVO;
	}
	
	/**
	 * post request for deleting hotel details
	 * @param hotelDetailsVO
	 * @return
	 */
	@PostMapping("/deleteHotel")
	public @ResponseBody RestonzaRestResponseVO executeDeleteHotels(@RequestBody HotelDetailsVO hotelDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (hotelDetailsVO.getId() != null && !hotelDetailsVO.getId().equals("")) {
			hotelDetailsRepository.delete(Integer.parseInt(hotelDetailsVO.getId()));
			userDetailsRepository.removeUser(Integer.parseInt(hotelDetailsVO.getId()));
			restonzaRestResponseVO = new RestonzaRestResponseVO("success", "Successfully deleted hotel");
		} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Error occurred during deleting hotel");
		}
		
		return restonzaRestResponseVO;
	}
	
	/**
	 * post request for adding table details
	 * @param tableVO
	 * @return
	 */
	@PostMapping("/addTable")
	public @ResponseBody RestonzaRestResponseVO executeAddTableDetails(@RequestBody TableVO tableVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		TableDetails tableDetails = new TableDetails();
		try {
			tableDetails = tableDetails.prepareTableDetails(tableDetails, tableVO);
			tableDetailsRepository.save(tableDetails);
			hotelDetailsRepository.addTable(Integer.parseInt(tableVO.getHotel_id()));
			restonzaRestResponseVO = new RestonzaRestResponseVO("success", "Successfully added table details");
		} catch (Exception e) {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Error while adding table");
		}
		return restonzaRestResponseVO;
	}
	
	@PostMapping("/getTableStructue/{id}")
	public @ResponseBody RestonzaRestResponseVO executeGetTableStructure(@PathVariable (value = "id") String id){
		Map<String, Object> respnse = hotelAnalyzerRepository.getCurrentTableStructure(Integer.valueOf(id));
		return new RestonzaRestResponseVO("Success",respnse);
	}
	
	/**
	 * post request for updating table details
	 * @param tableVO
	 * @return
	 */
	@PostMapping("/updateTable")
	public @ResponseBody RestonzaRestResponseVO executeDeleteTable(@RequestBody TableVO tableVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		String status = tableVO.getStatus();
		boolean statusflag = false;
		if (status.equals("active")) {
			statusflag = true;
		} else{
			statusflag = false;
		}
		tableDetailsRepository.updateTableStatus(Integer.parseInt(tableVO.getTable_id()), Integer.parseInt(tableVO.getHotel_id()), tableVO.getDescription(),statusflag);
		restonzaRestResponseVO = new RestonzaRestResponseVO("success", "Successfully added table details");
		return restonzaRestResponseVO;
	}
	
	/**
	 * post request for getting table details
	 * @param tableVO
	 * @return
	 */
	@PostMapping("/getTables")
	public @ResponseBody RestonzaRestResponseVO executeGetTable(@RequestBody TableVO tableVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		List<TableVO> tableVOList = new ArrayList<TableVO>();
		TableVO tableVOListObject = null;
		List<TableDetails> dbData = tableDetailsRepository.getTableDetails(Integer.parseInt(tableVO.getHotel_id()));
		for (TableDetails tableDetails : dbData) {
			tableVOListObject = new TableVO(String.valueOf(tableDetails.getTable_id()),tableDetails.getDescription(), RestonzaUtility.getStatus(tableDetails.isStatus()));
			tableVOList.add(tableVOListObject);
		}
		restonzaRestResponseVO = new RestonzaRestResponseVO("success", tableVOList);
		return restonzaRestResponseVO;
	}
	
	/**
	 * post request for getting table details
	 * @param tableVO
	 * @return
	 */
	@PostMapping("/getQRCodes")
	public @ResponseBody RestonzaRestResponseVO executeGetQRCodes(@RequestBody TableVO tableVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		List<TableVO> tableVOList = new ArrayList<TableVO>();
		TableVO tableVOListObject = null;
		List<TableDetails> dbData = tableDetailsRepository.getTableDetails(Integer.parseInt(tableVO.getHotel_id()));
		for (TableDetails tableDetails : dbData) {
			String table_id =String.valueOf(tableDetails.getTable_id());
			String hotel_id = String.valueOf(tableDetails.getHotel_id());
			String qrcode = hotel_id + "-" + table_id;
			String filepath = "/var/images" + table_id+hotel_id + ".jpg"; 
			tableVOListObject = new TableVO(table_id, hotel_id,getQRCode(filepath,qrcode));
			tableVOList.add(tableVOListObject);
		}
		restonzaRestResponseVO = new RestonzaRestResponseVO("success", tableVOList);
		return restonzaRestResponseVO;
	}
	
	//qrcode generation
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private byte[] getQRCode(String filepath, String qrcode) {
		try {
			int size = 125;
			String fileType = "jpg";
			File qrFile = new File(filepath);
			Hashtable hintMap = new Hashtable();
			hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix byteMatrix = qrCodeWriter.encode(qrcode,BarcodeFormat.QR_CODE, size, size, hintMap);
			// Make the BufferedImage that are to hold the QRCode
			int matrixWidth = byteMatrix.getWidth();
			BufferedImage image = new BufferedImage(matrixWidth, matrixWidth,
					BufferedImage.TYPE_INT_RGB);
			image.createGraphics();
			Graphics2D graphics = (Graphics2D) image.getGraphics();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, matrixWidth, matrixWidth);
			// Paint and save the image using the ByteMatrix
			graphics.setColor(Color.BLACK);

			for (int i = 0; i < matrixWidth; i++) {
				for (int j = 0; j < matrixWidth; j++) {
					if (byteMatrix.get(i, j)) {
						graphics.fillRect(i, j, 1, 1);
					}
				}
			}
			ImageIO.write(image, fileType, qrFile);
			BufferedImage originalImage = ImageIO.read(qrFile);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(originalImage, "jpg", baos);
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			baos.close();
			return imageInByte;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	/**
	 * post request for getting table details
	 * @param tableVO
	 * @return
	 */
	@PostMapping("/getActiveTables")
	public @ResponseBody RestonzaRestResponseVO executeGetAllActiveTable(@RequestBody TableVO tableVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		int[] activeTables = tableDetailsRepository.getActiveTableDetails(Integer.parseInt(tableVO.getHotel_id()), true);
		restonzaRestResponseVO = new RestonzaRestResponseVO("success", activeTables);
		return restonzaRestResponseVO;
	}
	
	/**
	 * post request for getting table details
	 * @param tableVO
	 * @return
	 */
	@PostMapping("/getActiveHotels")
	public @ResponseBody RestonzaRestResponseVO executeGetAllActiveHotels(@RequestBody TableVO tableVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		String[] activeHotels = hotelDetailsRepository.getHotelName(true);
		restonzaRestResponseVO = new RestonzaRestResponseVO("success", activeHotels);
		return restonzaRestResponseVO;
	}
	
	/**
	 * post request for getting table details
	 * @param tableVO
	 * @return
	 */
	@PostMapping("/getActiveHotelsDetails")
	public @ResponseBody RestonzaRestResponseVO executeGetAllActiveHotelDetails(@RequestBody TableVO tableVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		List<HotelDetails> activeHotelDetails = hotelDetailsRepository.getHotelDetails(true);
		restonzaRestResponseVO = new RestonzaRestResponseVO("success", activeHotelDetails);
		return restonzaRestResponseVO;
	}
	
	
	/*
	 * This request is hit by Detail report for pdf Genration
	 */
	@RequestMapping(value = "/downloadDetailPdfReport", method = RequestMethod.POST, produces = APPLICATION_PDF)
	public @ResponseBody void downloadPDFSuperadmin(@RequestBody ReportRequest reportRequest, HttpServletResponse response) throws IOException {
	    File file = null;
	    final String endtime = "23:59:59";
	    System.out.println(reportRequest);
	    HotelReportVO hotelReportVO = new HotelReportVO(
				reportRequest.getHotelname(),
				reportRequest.getReporttype(),
				reportRequest.getStartdate(),
				reportRequest.getTodate(),
				reportRequest.getFromdate());
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String toDate = null;
			String fromDate = null;
			if (hotelReportVO.getReporttype().equals("daily")) {
				fromDate = sdf.format(sdf.parse(hotelReportVO.getStartdate()));
				toDate = fromDate + " " + endtime;
			} else {
				fromDate = sdf.format(sdf.parse(hotelReportVO.getFromdate()));
				toDate = sdf.format(sdf.parse(hotelReportVO.getTodate())) + " " + endtime;
			}
			String hotelName = hotelDetailsRepository.findOne(Integer.parseInt(hotelReportVO.getHotelname())).getHotel_name();
			Map<String, Object> report_Params = hotelAnalyzerRepository.getReportGenerate(hotelReportVO, toDate, fromDate);
			String totalEarningAmount = String.valueOf(hotelAnalyzerRepository.getTotalEarningForReport(hotelReportVO, toDate, fromDate));
			file = buildDetailReportPDF(report_Params, toDate, fromDate, totalEarningAmount, hotelName);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    InputStream in = new FileInputStream(file);
	    response.setContentType(APPLICATION_PDF);
	    response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
	    response.setHeader("Content-Length", String.valueOf(file.length()));
	    FileCopyUtils.copy(in, response.getOutputStream());
	}
	
	/*
	 * This request is hit by Detail report for Web Vew
	 */
	@PostMapping("/downloadDetailReportWebView")
	public @ResponseBody RestonzaRestResponseVO downloadPDFSuperadmin(@RequestBody ReportRequest reportRequest) throws IOException {
		RestonzaRestResponseVO response = null;
	    final String endtime = "23:59:59";
	    System.out.println(reportRequest);
		HotelReportVO hotelReportVO = new HotelReportVO(
				reportRequest.getHotelname(),
				reportRequest.getReporttype(),
				reportRequest.getStartdate(),
				reportRequest.getTodate(),
				reportRequest.getFromdate());
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String toDate = null;
			String fromDate = null;
			if (hotelReportVO.getReporttype().equals("daily")) {
				fromDate = sdf.format(sdf.parse(hotelReportVO.getStartdate()));
				toDate = fromDate + " " + endtime;
			} else {
				fromDate = sdf.format(sdf.parse(hotelReportVO.getFromdate()));
				toDate = sdf.format(sdf.parse(hotelReportVO.getTodate())) + " " + endtime;
			}
			String hotelName = hotelDetailsRepository.findOne(Integer.parseInt(hotelReportVO.getHotelname())).getHotel_name();
			Map<String, Object> report_Params = hotelAnalyzerRepository.getReportGenerate(hotelReportVO, toDate, fromDate);
			String totalEarningAmount = String.valueOf(hotelAnalyzerRepository.getTotalEarningForReport(hotelReportVO, toDate, fromDate));
			Map<String,Object> responsemap = new HashMap<String, Object>();
			List<DetailReportVO> list = new ArrayList<DetailReportVO>();
			for (Entry<String, Object> entity:report_Params.entrySet()) {
				DetailReportVO retailreport = new DetailReportVO();
				retailreport.setDishname(entity.getKey());
				retailreport.setQty(String.valueOf(entity.getValue()));
				list.add(retailreport);
			}
			responsemap.put("reportparams",report_Params);
			responsemap.put("totalEarningAmount",totalEarningAmount);
			responsemap.put("reportdata",list);
			response = new RestonzaRestResponseVO("sucess",responsemap);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	  return response;
	}
	

	/**
	 * Hotel Detail report
	 * @param report_Params
	 * @param toDate
	 * @param fromDate
	 * @param totalEarningAmount
	 * @param hotelName
	 * @return
	 * @throws IOException
	 */
	private File buildDetailReportPDF(Map<String, Object> report_Params, String toDate, String fromDate, String totalEarningAmount, String hotelName) throws IOException {
		//need to change as per server directory
		FileUtils.cleanDirectory(new File("/var/downloads/")); 
		String fileName = "/var/downloads/CustomDetailReport-" +new Date().getDate()+ ".pdf"; //need to add server location
		try {
			OutputStream file = new FileOutputStream(new File(fileName));

			Document document = new Document();
			PdfWriter.getInstance(document, file);
			document.open();

			document.addAuthor("Flex Grow Developers");
            document.addCreationDate();
            document.addCreator("Flex Grow Developers");
            document.addTitle("Hotel Analysis Report");
			//Create pdf
            Image img = Image.getInstance("/var/defaulthotel.png");
	        img.setAlignment(Element.ALIGN_CENTER); /*setAbsolutePosition(450f, 10f);*/
	        document.add(img);
	        document.add(Chunk.NEWLINE);
			Paragraph heading = new Paragraph(hotelName,new Font(Font.TIMES_ROMAN, 18, Font.BOLD));
			heading.setAlignment(Element.ALIGN_CENTER);
			document.add(heading);
			document.add(Chunk.NEWLINE);
			Chunk linebreak = new Chunk(new DottedLineSeparator());
			document.add(linebreak);
			Paragraph reportduration = new Paragraph();
			Chunk fromDateChunk = new Chunk("From Date:" + fromDate);
			Chunk spacing = new Chunk(Chunk.NEWLINE);
			Chunk toDateChunk = new Chunk("To Date: " + toDate);
			reportduration.add(fromDateChunk);
			reportduration.add(spacing);
			reportduration.add(toDateChunk);
			document.add(reportduration);
			document.add(linebreak);

			PdfPTable pdfTable = new PdfPTable(3);
			PdfPCell cell1 = new PdfPCell(new Phrase("#"));
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			pdfTable.addCell(cell1);

			cell1 = new PdfPCell(new Phrase("Dish Name"));
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			pdfTable.addCell(cell1);

			cell1 = new PdfPCell(new Phrase("Sold Quantity"));
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			pdfTable.addCell(cell1);
			pdfTable.setHeaderRows(1);
			
			int i= 1;
            for (String dishName : report_Params.keySet()) {
            	pdfTable.addCell(String.valueOf(i));
            	pdfTable.addCell(dishName);
            	pdfTable.addCell(String.valueOf(report_Params.get(dishName)));
            	i++;
			}
            document.add(pdfTable);
        	document.add(linebreak);
        	Paragraph totalEarning = new Paragraph("Total Earning:",new Font(Font.TIMES_ROMAN, 14, Font.BOLD));
        	totalEarning.setSpacingBefore(10);
        	Paragraph hotelNamepara = new Paragraph("This report generated with Billy" , new Font(Font.TIMES_ROMAN, 8, Font.BOLD));
        	hotelNamepara.setAlignment(Element.ALIGN_RIGHT);
			document.add(hotelNamepara);
        	BaseFont bf = BaseFont.createFont("static/bower_components/fonts/ARIAL.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        	Font font = new Font(bf, 12);
        	Chunk chunkRupee = new Chunk(" \u20B9 " + totalEarningAmount, font);
        	totalEarning.add(chunkRupee);
        	document.add(totalEarning);
        	document.close();
        	file.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
		return new File(fileName);
	}
	
	/**
	 * Download Dish Upload pdf
	 * @param allRequestParams
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/downloadTemplate", method = RequestMethod.POST, produces = "text/csv")
	public @ResponseBody void downloadTemplate(@RequestParam Map<String,String> allRequestParams, HttpServletResponse response) throws IOException {
	    File file = new File("/var/template/template.csv");
	    InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
	    response.setContentType("text/csv");
	    response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
	    response.setHeader("Content-Length", String.valueOf(file.length()));
	    FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	//sales report
	/*@RequestMapping(value = "/downloadSalesPdf", method = RequestMethod.POST, produces = APPLICATION_PDF)
	public @ResponseBody void downloadSalesPDF(@RequestParam Map<String,String> allRequestParams, HttpServletResponse response) throws IOException {
	    File file = null;
	    final String endtime = "23:59:59";
	    System.out.println(allRequestParams);
		HotelReportVO hotelReportVO = new HotelReportVO(
				allRequestParams.get("hotelname"),
				allRequestParams.get("startdate"),
				allRequestParams.get("todate"),
				allRequestParams.get("fromdate"));
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String fromDate = sdf.format(sdf.parse(hotelReportVO.getFromdate()));
			String toDate = sdf.format(sdf.parse(hotelReportVO.getTodate())) + " " + endtime;
			String hotelName = hotelDetailsRepository.findOne(Integer.parseInt(hotelReportVO.getHotelname())).getHotel_name();
			Map<String, LinkedHashMap<String, Object>> report_Params = hotelAnalyzerRepository.getSalesReport(hotelReportVO, toDate, fromDate);
			file = buildSalePDF(report_Params, toDate, fromDate,hotelName);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    InputStream in = new FileInputStream(file);
	    response.setContentType(APPLICATION_PDF);
	    response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
	    response.setHeader("Content-Length", String.valueOf(file.length()));
	    FileCopyUtils.copy(in, response.getOutputStream());
	}*/
	
	
	
	/**
	 * This will give web view for sales report page.
	 * @param allRequestParams
	 * @return
	 * @throws ParseException 
	 */
	@PostMapping("/getSalesReportWebView")
	public @ResponseBody RestonzaRestResponseVO executeGetSalesWebView(@RequestBody ReportRequest reportRequest) throws ParseException{
		RestonzaRestResponseVO restonzaRestResponseVO =null;
		final String endtime = "23:59:59";
	    System.out.println(reportRequest);
		HotelReportVO hotelReportVO = new HotelReportVO(
				reportRequest.getHotelname(),
				reportRequest.getReporttype(),
				reportRequest.getStartdate(),
				reportRequest.getTodate(),
				reportRequest.getFromdate());
		
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String toDate = null;
			String fromDate = null;
			if (hotelReportVO.getReporttype().equals("daily")) {
				fromDate = sdf.format(sdf.parse(hotelReportVO.getStartdate()));
				toDate = fromDate + " " + endtime;
			} else {
				fromDate = sdf.format(sdf.parse(hotelReportVO.getFromdate()));
				toDate = sdf.format(sdf.parse(hotelReportVO.getTodate())) + " " + endtime;
			}
			String hotelName = 
					hotelDetailsRepository.findOne(Integer.parseInt(hotelReportVO.getHotelname())).getHotel_name();
			/*Map<String, Object> report_Params = 
					hotelAnalyzerRepository.getReportGenerate(hotelReportVO, toDate, fromDate);*/
			Map<String, Object> responseParam = new HashMap<String, Object>();
			try {
				Map<String, LinkedHashMap<String, Object>> report_Params = hotelAnalyzerRepository.getSalesReport(hotelReportVO, toDate, fromDate);
				String totalEarningAmount = 
						String.valueOf(hotelAnalyzerRepository.getTotalEarningForReport(hotelReportVO, toDate, fromDate));
				Map<String, Object> mp = report_Params.get("dailyOrderCounter");
				List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
				// VO for table
				List<SalesReportVO> reportvoList = new LinkedList<SalesReportVO>();
				for (Map.Entry<String, Object> entry : mp.entrySet()) {
					SalesReportVO salesreportvo = new SalesReportVO();
					salesreportvo.setDate(entry.getKey());
					salesreportvo.setCount(String.valueOf(entry.getValue()));
					String earning = String.valueOf(report_Params.get("dailyearning").get(entry.getKey()));
					salesreportvo.setEarning(earning);
					Map<String,Object> dateMap = new HashMap<String, Object>();
					dateMap.put(""+new String("date")+"", parseDate(entry.getKey()));
					dateMap.put(""+new String("value")+"", (Integer)entry.getValue());
					list.add(dateMap);
					reportvoList.add(salesreportvo);
					responseParam.put("totalEarningAmount", totalEarningAmount);
					responseParam.put("responseParam", report_Params);
					responseParam.put("reportData", list);
					responseParam.put("salesreportvo", reportvoList);
				}
			} catch(Exception e) {
				e.printStackTrace();
				return restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Error in genrating sales report!");
			}
		return restonzaRestResponseVO = new RestonzaRestResponseVO("success", responseParam);
	}
	
	public String parseDate(String date) throws ParseException {
		Date initDate = new SimpleDateFormat("dd-MM-yyyy").parse(date);
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    String parsedDate = formatter.format(initDate);
		return parsedDate;
	}

	/**
	 * Hotel Sales Analysis Report
	 * @param report_Params
	 * @param toDate
	 * @param fromDate
	 * @param hotelName
	 * @return
	 * @throws IOException
	 */
	private File buildSalePDF(Map<String, LinkedHashMap<String, Object>> report_Params, String toDate,String fromDate, String hotelName) throws IOException {
		//FileUtils.cleanDirectory(new File("/var/downloads/")); 
		String fileName = "/var/downloads/download" + new Date().getTime() + ".pdf"; //need to add server location
		try {
			OutputStream file = new FileOutputStream(new File(fileName));

			Document document = new Document();
			PdfWriter.getInstance(document, file);
			document.open();

			document.addAuthor("Flex Grow Developers");
            document.addCreationDate();
            document.addCreator("Flex Grow Developers");
            document.addTitle("Hotel Sales Analysis Report");
			//Create pdf
			Paragraph heading = new Paragraph("Hotel Sales Report",new Font(Font.TIMES_ROMAN, 18, Font.BOLD));
			heading.setAlignment(Element.ALIGN_CENTER);
			document.add(heading);
			document.add(Chunk.NEWLINE);
			Paragraph hotelNamepara = new Paragraph("Hotel Name: " + hotelName , new Font(Font.TIMES_ROMAN, 14, Font.BOLD));
			document.add(hotelNamepara);
			Chunk linebreak = new Chunk(new DottedLineSeparator());
			document.add(linebreak);
			Paragraph reportduration = new Paragraph();
			Chunk fromDateChunk = new Chunk("Sales Report start Date(yyyy-mm-dd): " + fromDate);
			Chunk spacing = new Chunk(Chunk.NEWLINE);
			Chunk toDateChunk = new Chunk("Sales Report End Date(yyyy-mm-dd): " + toDate);
			reportduration.add(fromDateChunk);
			reportduration.add(spacing);
			reportduration.add(toDateChunk);
			document.add(reportduration);
			document.add(linebreak);


			PdfPTable pdfTable = new PdfPTable(4);
			PdfPCell cell1 = new PdfPCell(new Phrase("#"));
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			pdfTable.addCell(cell1);

			cell1 = new PdfPCell(new Phrase("Date"));
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			pdfTable.addCell(cell1);

			cell1 = new PdfPCell(new Phrase("Order Count"));
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			pdfTable.addCell(cell1);
			
			cell1 = new PdfPCell(new Phrase("Earning"));
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			pdfTable.addCell(cell1);
			pdfTable.setHeaderRows(1);
			
			int i= 1;
			double sum = 0;
			LinkedHashMap<String, Object> dailyOrderCounter = report_Params.get("dailyOrderCounter");
			LinkedHashMap<String, Object> dailyearning = report_Params.get("dailyearning");
            for (String reportdailydate : dailyOrderCounter.keySet()) {
            	pdfTable.addCell(String.valueOf(i));
            	pdfTable.addCell(reportdailydate);
            	int dailyearningcount = (Integer)dailyOrderCounter.get(reportdailydate);
            	pdfTable.addCell(String.valueOf(dailyearningcount));
            	double dailyearningamount = (Double)dailyearning.get(reportdailydate);
            	sum= sum+ dailyearningamount;
            	pdfTable.addCell(String.valueOf(dailyearningamount));
            	i++;
			}
            document.add(pdfTable);
        	document.add(linebreak);
        	Paragraph totalEarning = new Paragraph("Total Earning : ",new Font(Font.TIMES_ROMAN, 14, Font.BOLD));
        	totalEarning.setSpacingBefore(10);
        	BaseFont bf = BaseFont.createFont("static/bower_components/fonts/ARIAL.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        	Font font = new Font(bf, 12);
        	Chunk chunkRupee = new Chunk(" \u20B9 " + sum, font);
        	totalEarning.add(chunkRupee);
        	document.add(totalEarning);
        	document.close();
        	file.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
		return new File(fileName);
	}
	
	/**
	 * post request for getting table details
	 * @param tableVO
	 * @return
	 */
	@PostMapping("/getUpdatedData")
	public @ResponseBody RestonzaRestResponseVO getUpdatedData(@RequestBody TableVO tableVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (tableVO.getHotel_id() != null) {
			int hotel_id = Integer.parseInt(tableVO.getHotel_id());
			restonzaRestResponseVO = new RestonzaRestResponseVO("success", hotelAnalyzerRepository.getCustomerCall(hotel_id));
		}
		return restonzaRestResponseVO;
	}
	
	@PostMapping("/uploadDishes")
	public @ResponseBody RestonzaRestResponseVO executeBulkMenuUpload(@RequestBody BulkRequest bulkRequest){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (bulkRequest.getBulkMenu() != null && !bulkRequest.getBulkMenu().isEmpty()) {
			try {
				int count = dishesRepository.count(Integer.valueOf(bulkRequest.getBulkMenu().get(0).getHotel_id()));
				for (int i=0;i<bulkRequest.getBulkMenu().size();i++) {
					if ("".equalsIgnoreCase(bulkRequest.getBulkMenu().get(i).getHotel_id()) 
							|| bulkRequest.getBulkMenu().get(i).getHotel_id() == null) {
						bulkRequest.getBulkMenu().remove(i);
					} else {
						bulkRequest.getBulkMenu().get(i).setSeuence(count+1);
						count++;
					}
				}
				hotelAnalyzerRepository.bulkMenuUpload(bulkRequest.getBulkMenu());
				restonzaRestResponseVO = new RestonzaRestResponseVO("success", "Bulk Upload Successful");
			} catch (Exception e) {
				restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Please Verify template");
			}
		} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Please Verify template");
		}
		return restonzaRestResponseVO;
	}
	
	
	@GetMapping("/downloadSalesPdf/{id}/{fromDate}/{todate}")
	public void salesReport(@PathVariable (value = "id") int id,
			@PathVariable (value = "fromDate") String fromDate,
			@PathVariable (value = "todate") String todate, HttpServletResponse response) throws IOException {

		String sourceFileName = REPORTURL+"/SalesReportNew.jrxml";
		System.out.println(sourceFileName);
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ReportTitle", "Jasper Demo");
		parameters.put("Author", "Prepared By jCombat");
		try {
			System.out.println("Start compiling!!! ...");
			JasperCompileManager.compileReportToFile(sourceFileName);
			System.out.println("Done compiling!!! ...");
			sourceFileName = REPORTURL+"/SalesReportNew.jasper";
			List<Map<String, Object>> li= hotelAnalyzerRepository.reportForSales(id, todate, fromDate);
			JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(
					li);
			JasperReport report = (JasperReport) JRLoader
					.loadObjectFromFile(sourceFileName);
			JasperPrint jasperPrint = JasperFillManager.fillReport(report,
					parameters, beanColDataSource);
			if (jasperPrint != null) {
				byte[] pdfReport = JasperExportManager
						.exportReportToPdf(jasperPrint);
				response.reset();
				response.setContentType("application/pdf");
				response.setHeader("Cache-Control", "no-store");
				response.setHeader("Cache-Control", "private");
				response.setHeader("Pragma", "no-store");
				response.setContentLength(pdfReport.length);
				response.getOutputStream().write(pdfReport);
				response.getOutputStream().flush();
				response.getOutputStream().close();
			}
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This service will genrate menu card on hotel basis
	 * @throws IOException 
	 */
	@GetMapping("/downloadMenuPdf/{id}")
	public void report(@PathVariable (value = "id") int id, HttpServletResponse response) throws IOException {
	/*	int id = Integer.valueOf(hotelDetailsVO.getId());
	    JasperReportsPdfView view = new JasperReportsPdfView();
	    view.setUrl("classpath:GenrateMenu.jrxml");
	    view.setApplicationContext(appContext);
	    Map<String, Object> params = new HashMap<>();
	    List<HotelDishMenuPdf> list =  hotelAnalyzerRepository.genrateMenu(id);
	    if(!list.isEmpty()) {
	    	 params.put("datasource", list);
	    	 return new ModelAndView(view, params);
	    }
	    return null;
	}*/
		//int id = Integer.valueOf(hotelDetailsVO.getId());
		String sourceFileName = REPORTURL+"/GenrateMenu.jrxml";
		System.out.println(sourceFileName);
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ReportTitle", "Jasper Demo");
		parameters.put("Author", "Prepared By jCombat");
		try {
			System.out.println("Start compiling!!! ...");
			JasperCompileManager.compileReportToFile(sourceFileName);
			System.out.println("Done compiling!!! ...");
			sourceFileName = REPORTURL+"/GenrateMenu.jasper";
			List<HotelDishMenuPdf> list =  hotelAnalyzerRepository.genrateMenu(id);
			JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(
					list);
			JasperReport report = (JasperReport) JRLoader
					.loadObjectFromFile(sourceFileName);
			JasperPrint jasperPrint = JasperFillManager.fillReport(report,
					parameters, beanColDataSource);
			if (jasperPrint != null) {
				byte[] pdfReport = JasperExportManager
						.exportReportToPdf(jasperPrint);
				response.reset();
				response.setContentType("application/pdf");
				response.setHeader("Cache-Control", "no-store");
				response.setHeader("Cache-Control", "private");
				response.setHeader("Pragma", "no-store");
				response.setContentLength(pdfReport.length);
				response.getOutputStream().write(pdfReport);
				response.getOutputStream().flush();
				response.getOutputStream().close();
			}
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * This will give pdf download for sales report page.
	 * @param allRequestParams
	 * @return
	 * @throws ParseException 
	 * @throws IOException 
	 */
	@PostMapping("/getSalesReportPdfDownload")
	public @ResponseBody void executeGetSalesPdfDownload(@RequestBody ReportRequest reportRequest,HttpServletResponse response) throws ParseException, IOException{
		File file = null;
		final String endtime = "23:59:59";
	    System.out.println(reportRequest);
		HotelReportVO hotelReportVO = new HotelReportVO(
				reportRequest.getHotelname(),
				reportRequest.getReporttype(),
				reportRequest.getStartdate(),
				reportRequest.getTodate(),
				reportRequest.getFromdate());
		
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String toDate = null;
			String fromDate = null;
			if (hotelReportVO.getReporttype().equals("daily")) {
				fromDate = sdf.format(sdf.parse(hotelReportVO.getStartdate()));
				toDate = fromDate + " " + endtime;
			} else {
				fromDate = sdf.format(sdf.parse(hotelReportVO.getFromdate()));
				toDate = sdf.format(sdf.parse(hotelReportVO.getTodate())) + " " + endtime;
			}
			String hotelName = 
					hotelDetailsRepository.findOne(Integer.parseInt(hotelReportVO.getHotelname())).getHotel_name();
			Map<String, Object> responseParam = new HashMap<String, Object>();
			try {
				Map<String, LinkedHashMap<String, Object>> report_Params = hotelAnalyzerRepository.getSalesReport(hotelReportVO, toDate, fromDate);
				String totalEarningAmount = 
						String.valueOf(hotelAnalyzerRepository.getTotalEarningForReport(hotelReportVO, toDate, fromDate));
				Map<String, Object> mp = report_Params.get("dailyOrderCounter");
				List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
				// VO for table
				List<SalesReportVO> reportvoList = new LinkedList<SalesReportVO>();
				for (Map.Entry<String, Object> entry : mp.entrySet()) {
					SalesReportVO salesreportvo = new SalesReportVO();
					salesreportvo.setDate(entry.getKey());
					salesreportvo.setCount(String.valueOf(entry.getValue()));
					String earning = String.valueOf(report_Params.get("dailyearning").get(entry.getKey()));
					salesreportvo.setEarning(earning);
					Map<String,Object> dateMap = new HashMap<String, Object>();
					dateMap.put(""+new String("date")+"", parseDate(entry.getKey()));
					dateMap.put(""+new String("value")+"", (Integer)entry.getValue());
					list.add(dateMap);
					reportvoList.add(salesreportvo);
					responseParam.put("totalEarningAmount", totalEarningAmount);
					responseParam.put("responseParam", report_Params);
					responseParam.put("reportData", list);
					responseParam.put("salesreportvo", reportvoList);
				}
				file = buildSalesReportPDF(reportvoList, reportRequest.getTodate(), reportRequest.getFromdate(), totalEarningAmount, hotelName);
			} catch(Exception e) {
				e.printStackTrace();
			}
			InputStream in = new FileInputStream(file);
		    response.setContentType(APPLICATION_PDF);
		    response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
		    response.setHeader("Content-Length", String.valueOf(file.length()));
		    FileCopyUtils.copy(in, response.getOutputStream());
	}
	/**
	 * Hotel Sales report
	 * @param report_Params
	 * @param toDate
	 * @param fromDate
	 * @param totalEarningAmount
	 * @param hotelName
	 * @return
	 * @throws IOException
	 */
	private File buildSalesReportPDF(List<SalesReportVO> report_Params, String toDate, String fromDate, String totalEarningAmount, String hotelName) throws IOException {
		//need to change as per server directory
		FileUtils.cleanDirectory(new File("/var/downloads/")); 
		String fileName = "/var/downloads/SalesReport-" +new Date().getDate()+ ".pdf"; //need to add server location
		try {
			OutputStream file = new FileOutputStream(new File(fileName));

			Document document = new Document();
			PdfWriter.getInstance(document, file);
			document.open();

			document.addAuthor("Flex Grow Developers");
            document.addCreationDate();
            document.addCreator("Flex Grow Developers");
            document.addTitle("Hotel Analysis Report");
			//Create pdf
            Image img = Image.getInstance("/var/defaulthotel.png");
	        img.setAlignment(Element.ALIGN_CENTER);
	        document.add(img);
	        document.add(Chunk.NEWLINE);
			Paragraph heading = new Paragraph(hotelName,new Font(Font.TIMES_ROMAN, 18, Font.BOLD));
			heading.setAlignment(Element.ALIGN_CENTER);
			document.add(heading);
			document.add(Chunk.NEWLINE);
			Chunk linebreak = new Chunk(new DottedLineSeparator());
			document.add(linebreak);
			Paragraph reportduration = new Paragraph();
			Chunk fromDateChunk = new Chunk("From Date:" + fromDate);
			Chunk spacing = new Chunk(Chunk.NEWLINE);
			Chunk toDateChunk = new Chunk("To Date: " + toDate);
			reportduration.add(fromDateChunk);
			reportduration.add(spacing);
			reportduration.add(toDateChunk);
			document.add(reportduration);
			document.add(linebreak);

			PdfPTable pdfTable = new PdfPTable(4);
			PdfPCell cell1 = new PdfPCell(new Phrase("#"));
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			pdfTable.addCell(cell1);

			cell1 = new PdfPCell(new Phrase("Date"));
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			pdfTable.addCell(cell1);

			cell1 = new PdfPCell(new Phrase("Total Orders"));
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			pdfTable.addCell(cell1);
			
			cell1 = new PdfPCell(new Phrase("Total Earnings"));
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			pdfTable.addCell(cell1);
			
			pdfTable.setHeaderRows(1);
			
			int i= 1;
            for (SalesReportVO sales : report_Params) {
            	pdfTable.addCell(String.valueOf(i));
            	pdfTable.addCell(sales.getDate());
            	pdfTable.addCell(sales.getCount());
            	pdfTable.addCell(sales.getEarning());
            	i++;
			}
            document.add(pdfTable);
        	document.add(linebreak);
        	Paragraph totalEarning = new Paragraph("Total Earning:",new Font(Font.TIMES_ROMAN, 14, Font.BOLD));
        	totalEarning.setSpacingBefore(10);
        	Paragraph hotelNamepara = new Paragraph("This report generated with Billy" , new Font(Font.TIMES_ROMAN, 8, Font.BOLD));
        	hotelNamepara.setAlignment(Element.ALIGN_RIGHT);
			document.add(hotelNamepara);
        	BaseFont bf = BaseFont.createFont("static/bower_components/fonts/ARIAL.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        	Font font = new Font(bf, 12);
        	Chunk chunkRupee = new Chunk(" \u20B9 " + totalEarningAmount, font);
        	totalEarning.add(chunkRupee);
        	document.add(totalEarning);
        	document.close();
        	file.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
		return new File(fileName);
	}
	

	/**
	 * This will give pdf download for sales report page.
	 * @param allRequestParams
	 * @return
	 * @throws ParseException 
	 * @throws IOException 
	 */
	@PostMapping("/getHotelMenuPdfDownload/{type}")
	public @ResponseBody void executeMenuPdfDownload(@RequestBody ReportRequest reportRequest,HttpServletResponse response, @PathVariable (value = "type") String type) throws ParseException, IOException{
		File file = null;
		final String endtime = "23:59:59";
	    System.out.println(reportRequest);
		HotelReportVO hotelReportVO = new HotelReportVO(
				reportRequest.getHotelname(),
				reportRequest.getReporttype(),
				reportRequest.getStartdate(),
				reportRequest.getTodate(),
				reportRequest.getFromdate());
			Map<String,List<DishesVO>> reportvo = new HashMap<String, List<DishesVO>>();
			String hotelName = hotelDetailsRepository.findOne(Integer.parseInt(hotelReportVO.getHotelname())).getHotel_name();
			String hotelAddress = hotelDetailsRepository.findOne(Integer.parseInt(hotelReportVO.getHotelname())).getHotel_address();
			try {
				List<Map<String, Object>> response_param = hotelAnalyzerRepository.getHotelMenuForPdf(hotelReportVO.getHotelname(),type);
				
				for (Map<String, Object> entry :response_param) {
					DishesVO dishvo = new DishesVO();
					dishvo.setDish_name((String)entry.get("dish_name"));
					dishvo.setDish_price(String.valueOf(entry.get("dish_price")));
					if(reportvo.containsKey((String)entry.get("dish_category"))) {
						(reportvo.get((String)entry.get("dish_category"))).add(dishvo);
					} else {
						List<DishesVO> listDish = new ArrayList<DishesVO>();
						listDish.add(dishvo);
						reportvo.put((String)entry.get("dish_category"), listDish);
					}
				}
				file = buildMenuReportPDF(reportvo, hotelName, hotelAddress);
			} catch(Exception e) {
				e.printStackTrace();
			}
			InputStream in = new FileInputStream(file);
		    response.setContentType(APPLICATION_PDF);
		    response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
		    response.setHeader("Content-Length", String.valueOf(file.length()));
		    FileCopyUtils.copy(in, response.getOutputStream());
	}
	
	
	

	/**
	 * Hotel Sales report
	 * @param report_Params
	 * @param toDate
	 * @param fromDate
	 * @param totalEarningAmount
	 * @param hotelName
	 * @return
	 * @throws IOException
	 */
	private File buildMenuReportPDF(Map<String,List<DishesVO>> reportParam, String hotelName, String hotelAddress) throws IOException {
		//need to change as per server directory
		try {
			FileUtils.cleanDirectory(new File("/var/downloads/"));
		} catch(Exception e) {
			e.printStackTrace();
		}
		String fileName = "/var/downloads/MenuReport-" +new Date().getDate()+ ".pdf"; //need to add server location
		try {
			OutputStream file = new FileOutputStream(new File(fileName));

			Document document = new Document();
			PdfWriter.getInstance(document, file);
			document.open();

			document.addAuthor("Flex Grow Developers");
            document.addCreationDate();
            document.addCreator("Flex Grow Developers");
            document.addTitle("Hotel Menu Report");
			//Create pdf
            Image img = Image.getInstance("/var/defaulthotel.png");
	        img.setAlignment(Element.ALIGN_CENTER);
	        document.add(img);
	        document.add(Chunk.NEWLINE);
			Paragraph heading = new Paragraph(hotelName,new Font(Font.TIMES_ROMAN, 18, Font.BOLD));
			heading.setAlignment(Element.ALIGN_CENTER);
			document.add(heading);
			document.add(Chunk.NEWLINE);
			
			Paragraph headingAddress = new Paragraph(hotelAddress,new Font(Font.TIMES_ROMAN, 12));
			headingAddress.setAlignment(Element.ALIGN_CENTER);
			document.add(headingAddress);
			document.add(Chunk.NEWLINE);
			
			Chunk linebreak = new Chunk(new DottedLineSeparator());
			document.add(linebreak);
			int size = reportParam.size();
			if (!reportParam.isEmpty()) {
				//for (int i=0;i<size;i++) {
					for (String key :reportParam.keySet()) {
						document.add(getPdfTable(reportParam,key));
						document.add(linebreak);
					}
				//}
			}
			Paragraph hotelNamepara = new Paragraph("This report generated with Billy" , new Font(Font.TIMES_ROMAN, 8, Font.BOLD));
        	hotelNamepara.setAlignment(Element.ALIGN_RIGHT);
			document.add(hotelNamepara);
        	BaseFont bf = BaseFont.createFont("static/bower_components/fonts/ARIAL.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        	Font font = new Font(bf, 12);
        	/*Chunk chunkRupee = new Chunk(" \u20B9 " + totalEarningAmount, font);
        	totalEarning.add(chunkRupee);*/
        	//document.add(totalEarning);
        	document.close();
        	file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return new File(fileName);
	}
	
	private PdfPTable getPdfTable(Map<String,List<DishesVO>> reportParam,String key) {
		PdfPTable pdfTable = new PdfPTable(2);
		PdfPCell cell1 = new PdfPCell(new Phrase(key.toString()));
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfTable.addCell(cell1);
		pdfTable.completeRow();
		/*
		cell1 = new PdfPCell(new Phrase("Dish Name"));
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfTable.addCell(cell1);

		cell1 = new PdfPCell(new Phrase("Price"));
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfTable.addCell(cell1);*/
		pdfTable.setHeaderRows(1);
			List<DishesVO> dishvo = reportParam.get(key);
			for (DishesVO dish :dishvo) {
				pdfTable.addCell(dish.getDish_name());
            	pdfTable.addCell(dish.getDish_price());
			}
		return pdfTable;
	}
	
	@PostMapping("/paymentDetails")
	public @ResponseBody RestonzaRestResponseVO paymentReport(@RequestBody ReportRequest reportRequest) throws ParseException{
		RestonzaRestResponseVO response = null;
	    final String endtime = "23:59:59";
	    System.out.println(reportRequest);
		HotelReportVO hotelReportVO = new HotelReportVO(
				reportRequest.getHotelname(),
				reportRequest.getReporttype(),
				reportRequest.getStartdate(),
				reportRequest.getTodate(),
				reportRequest.getFromdate());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String toDate = null;
		String fromDate = null;
		if (hotelReportVO.getReporttype().equals("daily")) {
			fromDate = sdf.format(sdf.parse(hotelReportVO.getStartdate()));
			toDate = fromDate + " " + endtime;
		} else {
			fromDate = sdf.format(sdf.parse(hotelReportVO.getFromdate()));
			toDate = sdf.format(sdf.parse(hotelReportVO.getTodate())) + " " + endtime;
		}
		List<Map<String,Object>> list = hotelAnalyzerRepository.getPaymentByHotel(fromDate,toDate);
		return  new RestonzaRestResponseVO("success", list);	
	}
}
