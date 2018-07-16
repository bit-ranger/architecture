package com.rainyalley.architecture.util;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DistinctCsvRows {

    private static class Row{
        private String id;
        private String content;

        public Row(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Row)) {
                return false;
            }
            return id.equals(((Row) obj).id);
        }
    }

    public static void main(String[] args) throws Exception {
        File dir = new File(args[0]);
        File distinct = new File(dir, "distinct");
        distinct.mkdir();
        File ignore = new File(dir, "ignore");
        ignore.mkdir();
        for (File file: dir.listFiles()){
            if (file.isDirectory()){
                continue;
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            Set<Row> rowSet = new HashSet<>();
            List<Row> ignoreRowList = new ArrayList<>();
            while (true){
                String line = reader.readLine();
                if (line == null){
                    break;
                }
                String[] lineValues = line.split(";");
                Row row = new Row(lineValues[0], line);
                boolean add = rowSet.add(row);
                if(!add){
                    ignoreRowList.add(row);
                }
            }
            reader.close();

            File disFile = new File(distinct, file.getName());
            disFile.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(disFile));
            for (Row row : rowSet) {
                writer.write(row.content);
                writer.newLine();
            }
            writer.flush();
            writer.close();

            File ignFile = new File(ignore, file.getName());
            ignFile.createNewFile();
            BufferedWriter writer2 = new BufferedWriter(new FileWriter(ignFile));
            for (Row row : ignoreRowList) {
                writer2.write(row.content);
                writer2.newLine();
            }
            writer2.flush();
            writer2.close();
        }


    }
}
