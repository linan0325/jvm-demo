package com.nan.jvm.serviceExport;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class DirecByteBufferTest {

    private static String TagFile="D:\\code\\TestFile.pdf";
    private static String ToFile="D:\\code\\TestFileTo.pdf";
    private static String DirecFile="D:\\code\\TestFileDirec.pdf";
    private static String Folder1="D:\\code\\CopyDocTest\\Folder-origin-1";

    public void CompareDirecAndBuffer(){

        try {
            long StartTime = System.currentTimeMillis();
            CopyUseBufferStream(TagFile,ToFile);
            long EndTime = System.currentTimeMillis();
            long BufferSpendTime = EndTime - StartTime;
            System.out.println("Buffer Spend Time Is : "+BufferSpendTime);


            long DirecStartTime = System.currentTimeMillis();
            CopyUseDirecStream();
            long DirecEndTime = System.currentTimeMillis();
            long DirecSpendTime = DirecEndTime - DirecStartTime;
            System.out.println("Direc Spend Time Is : "+DirecSpendTime);

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private void CopyUseDirecStream() throws IOException {

        FileInputStream in = new FileInputStream(TagFile);
        FileChannel channelIn = in.getChannel();
        FileOutputStream out = new FileOutputStream(DirecFile);
        FileChannel channelOut= out.getChannel();

        ReentrantLock reentrantLock = new ReentrantLock();
        reentrantLock.lock();
        reentrantLock.unlock();

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024*1024);
         while (true){
             int read = channelIn.read(byteBuffer);
             if(read== -1)break;
             byteBuffer.flip();
             channelOut.write(byteBuffer);
             byteBuffer.clear();
         }


    }

    private synchronized void CopyUseBufferStream(String tagPath,String desPath) throws IOException {


        InputStream in=null;
        OutputStream out=null;
        try{
            in=new FileInputStream(new File(tagPath));
            out=new FileOutputStream(new File(desPath));

            byte[] bytes = new byte[1024];
            int length;

            while ((length=in.read(bytes) )>0){
               out.write(bytes,0,length);
            }

        }catch (IOException o){
            o.printStackTrace();
        }finally {
            in.close();
            out.close();
        }

    }


    public void ListenFolders(){

        String Path1="D:\\code\\CopyDocTest\\Folder-origin-1";
        String Path2="D:\\code\\CopyDocTest\\Folder-origin-2";
        String Path3="D:\\code\\CopyDocTest\\Folder-des-1";
        HashMap<String, String> PathMap = new HashMap<>();
        PathMap.put("Path1",Path1);
        PathMap.put("Path2",Path2);

        copyFileToDesFolderUseThread(Path1,Path3);

        try {

            WatchService watchService = FileSystems.getDefault().newWatchService();

            Path path = Paths.get(Path1);
            path.register(watchService,StandardWatchEventKinds.ENTRY_CREATE);

//            Set<String> FolderPaths = PathMap.keySet();
//            for(String folder: FolderPaths){
//                Path path = Paths.get(PathMap.get(folder));
//                path.register(watchService,StandardWatchEventKinds.ENTRY_CREATE);
//            }
            

            System.out.println("正在监听文件......");


            while (true){

                WatchKey watchKey=watchService.take();

                for(WatchEvent<?> event: watchKey.pollEvents()){

                    //WatchEvent.Kind<?> kind = event.kind();
                    WatchEvent<Path> watchEventPath= (WatchEvent<Path>) event;

                    Path FilePath =(Path) watchKey.watchable();

                    Path fileName = FilePath.getFileName();
                    if(watchEventPath.context()!=null && !"".equals(watchEventPath.context())){

                        synchronized (Folder1){

                            System.out.println("文件夹名:"+fileName.toString());

                            System.out.println("文件名："+watchEventPath.context());
                            Folder1.notify();
                        }


                    }

                }

                boolean reset = watchKey.reset();

            }
           
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void copyFileToDesFolderUseThread(String orgFolder,String desFolder){

        File[] OriginFiles = new File(orgFolder).listFiles();

        Thread thread = new Thread(() -> {

            for (File file : OriginFiles){
                try {

                    File OriginFolder = new File(orgFolder);
                    File DesFolder = new File(desFolder);
      synchronized (Folder1){
      if(OriginFolder.isDirectory()&& DesFolder.isDirectory()){
          if(OriginFiles != null && OriginFiles.length>0){

              CopyUseBufferStream(file.getAbsolutePath(),desFolder+"\\"+file.getName());
          }
      }
      Folder1.wait();
  }

                }catch (IOException ioException){
                    ioException.printStackTrace();
                }catch (InterruptedException i){
                    i.printStackTrace();
                }

            }
        });

        thread.start();

    }
}
