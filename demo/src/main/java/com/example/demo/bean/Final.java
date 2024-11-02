package com.example.demo.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Final {
    Map<String, String> fileNameMap = new HashMap<>();
    List<String> differentLine = new ArrayList<>();
    List<String> differentLine2 = new ArrayList<>();
    Map<String,String> filenameMap2 = new HashMap<>();
}
