package com.nan.jvm.serviceExport;

import com.github.pagehelper.PageHelper;
import com.nan.jvm.entitys.exportEntity;
import com.nan.jvm.mapperDao.ExportTest;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

@Service
public class MultiThreadExport {

    private static final int PageNumber=1000;

    @Autowired
    private ExportTest exportTest;



  public void  ThreadPoolTest(){

      ThreadPoolExecutor threadPoolExecutor= new ThreadPoolExecutor(3,5,10,TimeUnit.MILLISECONDS,new ArrayBlockingQueue<>(2),
              new ThreadFactory(){
                  @Override
                  public Thread newThread(Runnable r) {
                      Thread thread = new Thread(r);
                      thread.setName("Thread Pool Test");
                      return thread;
                  }
              },
              new ThreadPoolExecutor.AbortPolicy());

      threadPoolExecutor.execute(()->{
          for (int i=0;i<10;i++){
              System.out.println(i);
          }
      });

      threadPoolExecutor.shutdown();

  }

  public void ExportExcelUseThreadPool() throws IOException {
      int PageCount = exportTest.PageCount();
      //System.out.println("PageCount:"+PageCount);Found 0

      int Pages=PageCount/PageNumber;
      int Mantissa=PageCount%PageNumber;
      if(Mantissa>0){
          Pages +=1;
          System.out.println("Pages:"+Pages);
          System.out.println("Mantissa:"+Mantissa);
      }

      ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, Pages,
              0, TimeUnit.MINUTES,
              new ArrayBlockingQueue<>(Pages),
              new ThreadPoolExecutor.AbortPolicy());

      CountDownLatch countDownLatch = new CountDownLatch(Pages);

      for(int i=1;i<=Pages;i++){

          int indexPage=i;
          try{

//              Future<?> submit = threadPoolExecutor.submit(() -> {
//                  try {
//                      exportToExcel(countDownLatch, indexPage, PageNumber);
//                  } catch (IOException e) {
//                      e.printStackTrace();
//                  }
//              });
//
//              Object o = submit.get();
              threadPoolExecutor.execute(()-> {
                  try {
                      exportToExcel(countDownLatch,indexPage,PageNumber);
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              });

          }catch (Exception e){
              e.printStackTrace();
              System.out.println("i:"+i+" Thread: ");
          }

//          threadPoolExecutor.execute(()-> {
//              try {
//                  exportToExcel(indexPage,PageNumber);
//              } catch (IOException e) {
//                  e.printStackTrace();
//              }
//          });
      }

      try {
          countDownLatch.await();
          threadPoolExecutor.shutdown();
      } catch (InterruptedException e) {
          e.printStackTrace();
      }


  }
    public void exportServiceTest(){
        int PageCount = exportTest.PageCount();
        //System.out.println("PageCount:"+PageCount);Found 0

        int Pages=PageCount/PageNumber;
        int Mantissa=PageCount%PageNumber;
        if(Mantissa>0){
            Pages +=1;
            //System.out.println("Pages:"+Pages);
            //System.out.println("Mantissa:"+Mantissa);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(Pages);

        CountDownLatch countDownLatch = new CountDownLatch(Pages);

        for(int i=1;i<=Pages;i++){


            int finalI = i;

            Thread thread = new Thread(() -> {

                try {

                    exportToExcel(countDownLatch, finalI, 1000);

                } catch (IOException e) {

                    e.printStackTrace();

                }
            });

            executorService.execute(thread);

        }

        try {
            countDownLatch.await();
        }catch (InterruptedException e){
            e.printStackTrace();
        }


//        PageHelper.startPage(0,10);
//        List<exportEntity> exportTests = exportTest.selectList();
//        exportTests.forEach((pageExport) -> System.out.println(pageExport.toString()));

    }



    private void exportToExcel(CountDownLatch countDownLatch,int start, int limitNumber) throws IOException {

        List<exportEntity> exportEntities = selectExportDataFormDB(start, limitNumber);

        String ExcelName="Export_Test_"+start+".xlsx";
        SXSSFWorkbook WB = new SXSSFWorkbook();
        SXSSFSheet sheet = WB.createSheet(ExcelName);
        Row row = sheet.createRow(0);
        Cell cell=null;
        String[] TileName = new String[]{"ID","Name","Address"};

        for(int i =0;i<TileName.length;i++){
            cell=row.createCell(i);
            cell.setCellValue(TileName[i]);
        }

        int RowNumber=1;
        for (exportEntity exportObj: exportEntities){
            row=sheet.createRow(RowNumber++);
            row.createCell(0).setCellValue(exportObj.getID());
            row.createCell(1).setCellValue(exportObj.getName());
            row.createCell(2).setCellValue(exportObj.getAddress());
        }

        String ExcelFileName="D:\\code\\"+ExcelName;

        File ExcelFile = new File(ExcelFileName);
        FileOutputStream fileOutputStream = new FileOutputStream(ExcelFile);
        WB.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();

        countDownLatch.countDown();
    }


    private void exportToExcel(int start, int limitNumber) throws IOException {

        List<exportEntity> exportEntities = selectExportDataFormDB(start, limitNumber);


        String ExcelName="Export_Test_"+start+".xlsx";
        SXSSFWorkbook WB = new SXSSFWorkbook();
        SXSSFSheet sheet = WB.createSheet(ExcelName);
        Row row = sheet.createRow(0);
        Cell cell=null;
        String[] TileName = new String[]{"ID","Name","Address"};

        for(int i =0;i<TileName.length;i++){
            cell=row.createCell(i);
            cell.setCellValue(TileName[i]);
        }

        int RowNumber=1;
        for (exportEntity exportObj: exportEntities){
            row=sheet.createRow(RowNumber++);
            row.createCell(0).setCellValue(exportObj.getID());
            row.createCell(1).setCellValue(exportObj.getName());
            row.createCell(2).setCellValue(exportObj.getAddress());
        }

        String ExcelFileName="D:\\code\\"+ExcelName;

        File ExcelFile = new File(ExcelFileName);
        FileOutputStream fileOutputStream = new FileOutputStream(ExcelFile);
        WB.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();

    }


    private List<exportEntity> selectExportDataFormDB(int start, int limitNumber){



        System.out.println("startIndex:"+start);

        PageHelper.startPage(start,limitNumber);

        List<exportEntity> exportEntities = exportTest.selectList();
        //exportEntities.forEach((pageExport) -> System.out.println(pageExport.toString()));

        return  exportEntities;
    }
}
