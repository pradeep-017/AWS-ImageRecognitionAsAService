package com.aws.ccproject.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.aws.ccproject.exception.ImagesUploadException;
import com.aws.ccproject.service.ImageService;

@Controller
public class WebTierRestController {

	private static final Logger logger = LoggerFactory.getLogger(WebTierRestController.class);

	@Autowired
	private ImageService imageService;

	private Set<String> imageNameSet = new HashSet<>();

	@PostMapping(value = "/upload")
	@ResponseBody
	public String uploadFiles(Model model, @RequestPart(value = "myfile") MultipartFile[] multipartFiles)
			throws ImagesUploadException {
		imageNameSet.clear();
		String res = null;
//		StringBuilder res = new StringBuilder();
		try {
			logger.info("Received the Images from the user, multipartFiles: ");
			for (MultipartFile multipartFile : multipartFiles) {
				logger.info("Single multipartFile: " + multipartFile);
				String imageName = imageService.uploadFiles(multipartFile);
				logger.info("Sending to InputQueue, imageName: " + imageName + ", multipartFile.getName(): "
						+ multipartFile.getName());
				imageNameSet.add(imageName);
				imageService.sendImageToQueue(imageName, multipartFile.getName());
				res =  getImageRecogResults1(imageName);
			}
		} catch (Exception e) {
			throw new ImagesUploadException();
		}
		return res;
	}

	@GetMapping(value = "/results")
	public String getImageRecogResults(Model model) {
		List<String> resList = new ArrayList<>();
		for (String imageName : imageNameSet) {
			String imageRes = "(" + imageName.substring(0, imageName.length() - 5) + ":" + imageService.getFromHashorSQS(imageName) + ")";
			resList.add(imageRes);
		}
		model.addAttribute("results", resList);
		return "resultsFinal";
	}
	
	public String getImageRecogResults1(String imageName) {
		String imageRes = "(" + imageName.substring(0, imageName.length() - 5) + ", " + imageService.getFromHashorSQS(imageName) + ")";
		return imageRes;
	}

	@GetMapping(value = "/")
	String index() {
		return "index";
	}

	@GetMapping(value = "/upload")
	String uploadForm() {
		return "imageRecog";
	}
}
