package com.example.demo.controller;

import com.example.demo.bean.Final;
import com.example.demo.bean.SYNC;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;


@RestController
@RequestMapping("/api")
public class SyncController {


    @PostMapping("/analyze")
    public Final uploadFile(@RequestBody List<SYNC> files ) {
        String startMarker = "Random";
        String endMarker = "Delay:";
        Map<String, String> fileMap = new HashMap<>();

        for (SYNC sync : files) {
            String key = sync.getName();
            String value = extractParagraph(sync.getValues(), startMarker, endMarker);
            fileMap.put(key,value);
        }

        String newStarkMarker = "Sync";
        String newEndMarker = "Random";
        Map<String, String> newFileMap = new HashMap<>();
        for (SYNC sync : files) {
            String key = sync.getName();
            String value = extractParagraph(sync.getValues(), newStarkMarker, newEndMarker);
            newFileMap.put(key,value);
        }

        // 合并相同值的键
        Map<String, List<String>> newMergedMap = new HashMap<>();
        for (Map.Entry<String, String> entry : newFileMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            newMergedMap.computeIfAbsent(value, k -> new ArrayList<>()).add(key);
        }

        Map<List<String>,String> newOutmap = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : newMergedMap.entrySet()) {
            String value = entry.getKey();
            List<String> keys = entry.getValue();
            // 对所有值进行比较
            newOutmap.put(keys, value);
        }
        Final test1 = test2(newOutmap);



        // 合并相同值的键
        Map<String, List<String>> mergedMap = new HashMap<>();
        for (Map.Entry<String, String> entry : fileMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            mergedMap.computeIfAbsent(value, k -> new ArrayList<>()).add(key);
        }
        Map<List<String>,String> outmap = new HashMap<>();
        // 输出结果
        for (Map.Entry<String, List<String>> entry : mergedMap.entrySet()) {
            String value = entry.getKey();
            List<String> keys = entry.getValue();
            // 对所有值进行比较
            outmap.put(keys, value);
        }

        Final test = test(outmap);
        test.setFilenameMap2(test1.getFilenameMap2());
        test.setDifferentLine2(test1.getDifferentLine2());
        return  test;
    }

    private static Final test2(Map<List<String>, String> fileMap){
        //正常来说 合并后只有两个 key-value 组合，那么随便取一个key的value作为模板,此处区第一个key的value为模板
        String template = fileMap.values().iterator().next();
        String key1 = "";
        String key2 = "";
        Iterator<List<String>> keyIterator = fileMap.keySet().iterator();
        if (keyIterator.hasNext()) {
            key1 = String.valueOf(keyIterator.next());
        }
        if (keyIterator.hasNext()) {
            key2 = String.valueOf(keyIterator.next());
        }

        //获取另一个value
        String secondValue = null;

        Iterator<String> valueIterator = fileMap.values().iterator();
        if (valueIterator.hasNext()) {
            valueIterator.next(); // 跳过第一个
            if (valueIterator.hasNext()) {
                secondValue = valueIterator.next(); // 获取第二个
            }
        }

//定义一个下标，第一个模板和第二个模板的格式一定是一样的，所以根据下标同时遍历两个模板
//并取出该下标位置的字符，如果相等，则不用管，如果不等，则将第二个模板该下标的字符用()包起来

        // 用 StringBuilder 来修改第二个模板的内容
        StringBuilder modifiedSecondTemplate = new StringBuilder();
        String[] firstLines = template.split("\n");
        String[] secondLines = secondValue.split("\n");

        int maxLength = Math.max(firstLines.length, secondLines.length);
        Map<String,String> finalMap = new HashMap<>();
        Final finallyMap  = new Final();
        for (int i = 0; i < maxLength; i++) {
            String firstLine = i < firstLines.length ? firstLines[i] : "";
            String secondLine = i < secondLines.length ? secondLines[i] : "";

            if (!firstLine.equals(secondLine)) {
                //  System.out.println("这里是key1的value   "+firstLine);
                finalMap.put(firstLine,key1);
                //   System.out.println("这里是key2的value   "+secondLine);
                finalMap.put(secondLine,key2);
                finallyMap.getFilenameMap2().put(key1,key2);
                finallyMap.getDifferentLine2().add(firstLine);
                finallyMap.getDifferentLine2().add( secondLine);
            }
        }
        //System.out.println("=====================");
        // System.out.println(finallyMap);


        return finallyMap;

    }








private static Final test(Map<List<String>, String> fileMap){
    //正常来说 合并后只有两个 key-value 组合，那么随便取一个key的value作为模板,此处区第一个key的value为模板
    String template = fileMap.values().iterator().next();
    String key1 = "";
    String key2 = "";
    Iterator<List<String>> keyIterator = fileMap.keySet().iterator();
    if (keyIterator.hasNext()) {
        key1 = String.valueOf(keyIterator.next());
    }
    if (keyIterator.hasNext()) {
        key2 = String.valueOf(keyIterator.next());
    }

    //获取另一个value
    String secondValue = null;

    Iterator<String> valueIterator = fileMap.values().iterator();
    if (valueIterator.hasNext()) {
        valueIterator.next(); // 跳过第一个
        if (valueIterator.hasNext()) {
            secondValue = valueIterator.next(); // 获取第二个
        }
    }

//定义一个下标，第一个模板和第二个模板的格式一定是一样的，所以根据下标同时遍历两个模板
//并取出该下标位置的字符，如果相等，则不用管，如果不等，则将第二个模板该下标的字符用()包起来

    // 用 StringBuilder 来修改第二个模板的内容
    StringBuilder modifiedSecondTemplate = new StringBuilder();
    String[] firstLines = template.split("\n");
    String[] secondLines = secondValue.split("\n");

    int maxLength = Math.max(firstLines.length, secondLines.length);
    Map<String,String> finalMap = new HashMap<>();
    Final finallyMap  = new Final();
    for (int i = 0; i < maxLength; i++) {
        String firstLine = i < firstLines.length ? firstLines[i] : "";
        String secondLine = i < secondLines.length ? secondLines[i] : "";

        if (!firstLine.equals(secondLine)) {
          //  System.out.println("这里是key1的value   "+firstLine);
            finalMap.put(firstLine,key1);
         //   System.out.println("这里是key2的value   "+secondLine);
            finalMap.put(secondLine,key2);
            finallyMap.getFileNameMap().put(key1,key2);
            finallyMap.getDifferentLine().add(firstLine);
            finallyMap.getDifferentLine().add( secondLine);
        }
    }
    //System.out.println("=====================");
   // System.out.println(finallyMap);


return finallyMap;

}

    private static String processLine(String baseLine, String currentLine) {
        StringBuilder processedLine = new StringBuilder();

        // 按字符逐字比较
        for (int i = 0; i < Math.max(baseLine.length(), currentLine.length()); i++) {
            char baseChar = (i < baseLine.length()) ? baseLine.charAt(i) : '\0';
            char currentChar = (i < currentLine.length()) ? currentLine.charAt(i) : '\0';

            if (baseChar == currentChar) {
                processedLine.append(baseChar);
            } else {
                processedLine.append("(").append(currentChar).append(")");
            }
        }
        return processedLine.toString();
    }

    // 提取段落的方法
    private String extractParagraph(String content, String startMarker, String endMarker) {
        int startIndex = content.indexOf(startMarker);
        int endIndex = content.indexOf(endMarker, startIndex);

        if (startIndex != -1 && endIndex != -1) {
            // 提取段落，并包含开始和结束的标记
            return content.substring(startIndex, endIndex + endMarker.length()).trim();
        }
        return ""; // 如果未找到段落，返回空字符串
    }



}


