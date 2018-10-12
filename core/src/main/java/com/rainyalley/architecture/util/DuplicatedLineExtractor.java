package com.rainyalley.architecture.util;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class DuplicatedLineExtractor {


    private String dirPath;

    public DuplicatedLineExtractor(String dirPath) {
        this.dirPath = dirPath;
    }

    public void extract(){
        File dirFile = new File(dirPath);
        for (File file : dirFile.listFiles()) {
            if (file.isDirectory()){
                new DuplicatedLineExtractor(file.getPath()).extract();
            } else{
                try {
                    extractLines(file);
                } catch (Exception e) {
                    System.err.println(String.format("extract %s failure", file.getPath()));
                    e.printStackTrace(System.err);
                }
            }
        }
    }

    private void extractLines(File file) throws Exception{
        LinkedHashSet<String> set = new LinkedHashSet<String>();
        ArrayList<String> dupList = new ArrayList<String>();
        BufferedReader bis = new BufferedReader(new FileReader(file));
        String line = null;
        while ((line = bis.readLine()) != null){
            if(set.contains(line)){
                dupList.add(line);
            } else {
                set.add(line);
            }
        }

        BufferedWriter bws = new BufferedWriter(new FileWriter(new File(file.getPath()+".dis")));
        try {
            for (String s : set) {
                bws.write(s);
                bws.newLine();
            }
        } finally {
            bws.close();
        }

        BufferedWriter bwd = new BufferedWriter(new FileWriter(new File(file.getPath()+".dup")));
        try {
            for (String s : dupList) {
                bwd.write(s);
                bwd.newLine();
            }
        } finally {
            bwd.close();
        }
    }


    public static void main(String[] args){
        new DuplicatedLineExtractor(args[0]).extract();
    }
}
