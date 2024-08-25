package com.hufs.AnswerDev.Model.Answer;

import com.hufs.AnswerDev.Model.Answer.Dto.fromClient.RegisterAnswerClientDto;
import com.hufs.AnswerDev.Model.Answer.Dto.fromServer.RegisterAnswerServerDto;
import com.hufs.AnswerDev.Model.Answer.Dto.fromServer.ResponseAnswerImageServerDto;
import com.hufs.AnswerDev.Model.AnswerImage.AnswerImage;
import com.hufs.AnswerDev.Model.AnswerImage.AnswerImageService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class AnswerService {

    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    AnswerImageService answerImageService;

    @Async
    @Transactional
    public CompletableFuture<RegisterAnswerServerDto> registerAnswer(RegisterAnswerClientDto dto) throws IOException {
        String url = dto.getUrl();
        Answer existAnswer = answerRepository.findByUrl(url);

        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("window-size=1400,1500");
        WebDriver driver = new ChromeDriver(options);
        try {
            driver.get(url);

            // 해답의 이름과 페이지 수를 가져옴
            String answerName = driver.findElement(By.cssSelector("h2[data-test-selector=\"document-viewer-sidebar-document-title\"]")).getText();
            int maxPageNumber = Integer.parseInt(driver.findElement(By.cssSelector("span[data-test-selector=\"total-document-available-pages\"]")).getText());

            // 해답 이름, Url으로 해답 객체 생성 후 저장
            Answer newAnswer = Answer.builder().name(answerName).url(url).build();
            answerRepository.save(newAnswer);

            // 해답 이미지 Url 파싱
            String firstImageSource = driver.findElement(By.cssSelector(".page-content div img")).getAttribute("src");
            String urlSource[] = firstImageSource.split("/bg1.png");

            ArrayList<ResponseAnswerImageServerDto> imageDtoList = new ArrayList<>();
            for (int i=1; i<=maxPageNumber; i++) {
                String hexIndex = Integer.toHexString(i);

                String completeUrl = urlSource[0] + "/bg" + hexIndex + ".png" + urlSource[1];
                answerImageService.saveImageToDB(newAnswer, i, completeUrl);

                ResponseAnswerImageServerDto imageDto = new ResponseAnswerImageServerDto(i, completeUrl);
                imageDtoList.add(imageDto);
            }

            return CompletableFuture.completedFuture(new RegisterAnswerServerDto(imageDtoList));
        } finally {
            driver.quit();
        }

        /*if (existAnswer == null) { // 해당 Url로 등록된 해답 정보가 없다면
            ChromeOptions options = new ChromeOptions();
            WebDriver driver = new ChromeDriver(options);

            try {
                driver.get(url);

                // 해답의 이름과 페이지 수를 가져옴
                String answerName = driver.findElement(By.cssSelector("h2[data-test-selector=\"document-viewer-sidebar-document-title\"]")).getText();
                int maxPageNumber = Integer.parseInt(driver.findElement(By.cssSelector("span[data-test-selector=\"total-document-available-pages\"]")).getText());

                // 해답 이름, Url으로 해답 객체 생성 후 저장
                Answer newAnswer = Answer.builder().name(answerName).url(url).build();
                answerRepository.save(newAnswer);

                // 해답 이미지 Url 파싱
                String firstImageSource = driver.findElement(By.cssSelector(".page-content div img")).getAttribute("src");
                String urlSource[] = firstImageSource.split("/bg1.png");

                ArrayList<ResponseAnswerImageServerDto> imageDtoList = new ArrayList<>();
                for (int i=1; i<=maxPageNumber; i++) {
                    String hexIndex = Integer.toHexString(i);

                    String completeUrl = urlSource[0] + "/bg" + hexIndex + ".png" + urlSource[1];
                    answerImageService.saveImageToDB(newAnswer, i, completeUrl);

                    ResponseAnswerImageServerDto imageDto = new ResponseAnswerImageServerDto(i, completeUrl);
                    imageDtoList.add(imageDto);
                }

                return CompletableFuture.completedFuture(new RegisterAnswerServerDto(imageDtoList));
            } finally {
                driver.quit();
            }

        } else { // 해당 Url로 등록된 해답 정보가 있다면
            ArrayList<ResponseAnswerImageServerDto> imageDtoList = new ArrayList<>();

            List<AnswerImage> imageList = existAnswer.getImageList();
            for (int i=1; i<=imageList.size(); i++) {
                AnswerImage theImage = imageList.get(i-1);
                ResponseAnswerImageServerDto imageDto = new ResponseAnswerImageServerDto(i, theImage.getUrl());
                imageDtoList.add(imageDto);
            }

            return CompletableFuture.completedFuture(new RegisterAnswerServerDto(imageDtoList));
        }*/
    }
}
