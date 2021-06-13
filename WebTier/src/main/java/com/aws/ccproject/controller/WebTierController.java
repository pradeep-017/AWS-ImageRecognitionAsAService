package com.aws.ccproject.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.aws.ccproject.constants.ApiKeys;
import com.aws.ccproject.exception.ImagesUploadException;
import com.aws.ccproject.service.ImageService;
import com.aws.ccproject.service.S3Service;

@Controller
public class WebTierController {

  @Autowired
  private ImageService imageService;
  
  private Set<String> imageNameSet = new HashSet<>();

  @Autowired
  private S3Service s3Service;

  @PostMapping(value = "/upload")
  public String uploadFiles(Model model,
      @RequestPart(value = "images") MultipartFile[] multipartFiles) throws ImagesUploadException {
	  imageNameSet.clear();
    try {
      System.out.println("Received the Images from the user, multipartFiles: ");
      for (MultipartFile multipartFile : multipartFiles) {
    	  System.out.println("Single multipartFile: " + multipartFile);
        String imageName = imageService.uploadFile(multipartFile);
        System.out.println("Sending to InputQueue, imageName: " + imageName + ", multipartFile.getName(): "+ multipartFile.getName());
        imageNameSet.add(imageName);
        imageService.sendImageToQueue(imageName, multipartFile.getName());
      }
    } catch (Exception e) {
      throw new ImagesUploadException();
    }
//    model.addAttribute(multipartFiles);
    return "showResults";
  }

  //get method to push results to UI
  @GetMapping(value = "/results")
  public String getResults(Model model) {
	  List<String> resList = new ArrayList<>();
	  for(String imageName : imageNameSet) {
		  String imageRes = "(" + imageName + ":" + imageService.getFromHashorSQS(imageName) + ")";
		  resList.add(imageRes);
	  }
	  model.addAttribute("results", resList);
//	  model.addAttribute("results", s3Service.getResponseResults());
	  return "resultsFinal";
  }
  
  
  @GetMapping(value = "/")
  String index(){
      return "index";
  }
  
  @GetMapping(value = "/upload")
	String uploadForm() {
		return "imageRecog";
	}
}