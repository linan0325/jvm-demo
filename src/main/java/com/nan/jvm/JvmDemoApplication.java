package com.nan.jvm;

import com.nan.jvm.chatgpt.ChatApiTest;
import com.nan.jvm.chatgpt.ChatApiTest2;
import com.nan.jvm.serviceExport.DirecByteBufferTest;
import com.nan.jvm.serviceExport.MultiThreadExport;
import com.nan.jvm.serviceJvm.Demo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JvmDemoApplication  implements CommandLineRunner {

    @Autowired
    private Demo1 demo1;

    @Autowired
    private MultiThreadExport multiThreadExport;

    @Autowired
    private DirecByteBufferTest direcByteBufferTest;

    @Autowired
    private ChatApiTest2 chatApiTest;

    public static void main(String[] args) {
        SpringApplication.run(JvmDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        //demo1.LockThread();
        //multiThreadExport.exportServiceTest();
       // multiThreadExport.ExportExcelUseThreadPool();
        //direcByteBufferTest.CompareDirecAndBuffer();
        //direcByteBufferTest.ListenFolders();
        chatApiTest.GetChatGpt();

    }


 }
