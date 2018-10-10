package com.restonza.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.restonza.dao.repository.HotelAnalyzerRepository;
import com.restonza.vo.RestonzaRestResponseVO;

@RestController
@RequestMapping("/restonza/mobileimage")
public class RestonzaMobileImageController {
	
	

	@Value("${imageuploaddirectory}")
	private String imageuploaddirectory; 
	
	@Autowired
	private HotelAnalyzerRepository hotelAnalyzerRepository;
	
	/**
	 * Service for Profile picture Upload used by mobile user
	 * @throws Exception 
	 */
	@PostMapping("/uploadCategoryPic/{type}/{id}") 
    public RestonzaRestResponseVO singleFileUpload(@RequestParam("file") MultipartFile file,@PathVariable("type")String type,@PathVariable("id")String id) throws Exception {
		final String relativeImageURL = "img/"+ type + "/"+ id.toString();
        if (file.isEmpty()) {
        	return new RestonzaRestResponseVO("success", "Error in saving category image");
        }
        byte[] bytes = file.getBytes();
        Path path = Paths.get(imageuploaddirectory+ type +"/" + id.toString()+".jpg");
        Files.write(path, bytes);
        hotelAnalyzerRepository.updateCategoryPicture(Integer.valueOf(id),relativeImageURL + ".jpg",type);
        return new RestonzaRestResponseVO("success", relativeImageURL + ".jpg");
    }

}
