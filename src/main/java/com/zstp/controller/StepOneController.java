package com.zstp.controller;

import com.hankcs.hanlp.corpus.document.sentence.Sentence;
import com.hankcs.hanlp.corpus.document.sentence.word.Word;
import com.hankcs.hanlp.model.perceptron.PerceptronLexicalAnalyzer;
import com.zstp.entity.R;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
public class StepOneController {
    @Value("${save.path}")
    String savePath;
    @PostMapping("step_one/analyze")
    public R segment(@RequestParam("file") MultipartFile file, @RequestParam(value = "fileName") String fileName) throws Exception {
        // 加txt后缀
        fileName += ".txt";
        // 文件完整路径 savePath是保存路径
        String filePath = savePath + fileName;
        // 读取文件
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream ips = file.getInputStream();
        byte[] buffer = new byte[8192]; // 这样效率与缓冲流持平
        int len = 0;
        while ((len = ips.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        String content = baos.toString();
        // 解析文件
        System.out.printf("正在解析--%s\n", fileName);
        PerceptronLexicalAnalyzer analyzer = new PerceptronLexicalAnalyzer();
        Sentence analyze = analyzer.analyze(content);
        List<Word> words = analyze.toSimpleWordList();
        System.out.printf("解析完成--%s\n", fileName);
        // 保存文件
        System.out.printf("开始保存--%s\n", filePath);
        FileOutputStream fops = new FileOutputStream(filePath);
        for(Word word: words){
            fops.write((word.toString()+'\n').getBytes());
        }
        System.out.printf("保存完毕--%s\n",filePath);
        // 关闭流
        ips.close();
        fops.close();
        baos.close();
        // 返回
        return new R(200, "解析成功", null);
    }

    @RequestMapping("download/{fileName}")
    public void download(HttpServletResponse response, @PathVariable String fileName) throws Exception {
        fileName += ".txt";
        // 设置请求头 new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1) 转码
        response.setHeader("Content-Disposition", "attachment; filename="
                + new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
        System.out.printf("正在下载--%s\n", fileName);
        // 从刚刚保存的路径下载
        FileInputStream fips = new FileInputStream(savePath + fileName);
        // 获取输出流
        ServletOutputStream sops = response.getOutputStream();
        byte[] buffer = new byte[8192];
        int len = 0;
        while ((len = fips.read(buffer)) != -1) {
            sops.write(buffer, 0, len);
        }
        System.out.printf("下载完毕--%s\n", fileName);
        // 关闭流
        sops.flush();
        fips.close();
        sops.close();
    }
}
