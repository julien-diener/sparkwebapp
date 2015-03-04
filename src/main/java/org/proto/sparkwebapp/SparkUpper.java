package org.proto.sparkwebapp;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import java.io.File;
import java.io.IOException;
import java.net.URI;


public class SparkUpper {
    static JavaSparkContext sparkContext;
    static String master;

    public static void initSpark(String newMaster){
        if(master!=newMaster && sparkContext!=null){
            sparkContext.stop();
            sparkContext = null;
        }
        if(sparkContext ==null) {
            master = newMaster;
            SparkConf conf = new SparkConf().setAppName("Spark Upper case conversion").setMaster(master);
            conf.setJars(JavaSparkContext.jarOfClass(SparkUpper.class));

            sparkContext = new JavaSparkContext(conf);
        }
    }

    public static void upper(String inputFile, String outputDir, String master, String namenode){
        initSpark(master);

        // delete output directory
        // -----------------------
        try {
            if (namenode != null) {
                Configuration conf = new Configuration();
                conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
                conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
                FileSystem hdfs = FileSystem.get(URI.create(namenode), conf);

                hdfs.delete(new Path(outputDir), true);   // true => recursive
            } else {
                FileUtil.fullyDelete(new File(outputDir));
            }
        }catch (IOException e){
            System.out.println("\n*** could not delete output directory: "+outputDir);
            System.out.println(e.getMessage());
        }

        // file conversion with spark
        // --------------------------
        if(namenode!=null){
            inputFile = namenode+inputFile;
            outputDir = namenode+outputDir;
        }
        JavaRDD<String> inputRdd = sparkContext.textFile(inputFile).cache();
        //   for Java 8:
        //   JavaRDD<String> outputRdd = inputRdd.map(Converter::convertLine);

        //   for Java 7:
        Function fct = new Function<String,String>() {
            @Override
            public String call(String line) throws Exception {
                return line.toUpperCase();
            }
        };
        JavaRDD<String> outputRdd = inputRdd.map(fct);

        outputRdd.saveAsTextFile(outputDir);
    }
}
