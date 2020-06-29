package ci.algorithm;

import java.io.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Utility {

    public static boolean createCsvFile(List<Object[]> rows, String filePath, String fileName) {
        //标记文件生成是否成功；
        boolean flag = true;

        //文件输出流
        BufferedWriter fileOutputStream = null;

        try{
            //含文件名的全路径
            String fullPath = filePath+ File.separator+fileName+".csv";
            File file = new File(fullPath);
            if (!file.getParentFile().exists())     //如果父目录不存在，创建父目录
            {
                file.getParentFile().mkdirs();
            }
            if (file.exists())     //如果该文件已经存在，删除旧文件
            {
                file.delete();
            }
            file = new File(fullPath);
            file.createNewFile();

            //格式化浮点数据
            NumberFormat formatter = NumberFormat.getNumberInstance();
            formatter.setMaximumFractionDigits(10);     //设置最大小数位为10；

            //格式化日期数据
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

            //实例化文件输出流
            fileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"GB2312"),1024);

            //遍历输出每行
            Iterator<Object[]> ite = rows.iterator();

            while (ite.hasNext())
            {
                Object[] rowData = (Object[])ite.next();
                for(int i=0;i<rowData.length;i++)
                {
                    Object obj = rowData[i];   //当前字段
                    //格式化数据
                    String field = "";
                    if (null != obj)
                    {
                        if (obj.getClass() == String.class)     //如果是字符串
                        {
                            field = (String)obj;
                        }else if (obj.getClass() == Double.class || obj.getClass() == Float.class)   //如果是浮点型
                        {
                            field = formatter.format(obj);   //格式化浮点数，使浮点数不以科学计数法输出
                        }else if (obj.getClass() == Integer.class || obj.getClass() == Long.class | obj.getClass() == Short.class || obj.getClass() == Byte.class)
                        {
                            //如果是整型
                            field += obj;
                        }else if (obj.getClass() == Date.class)   //如果是日期类型
                        {
                            field = sdf.format(obj);
                        }else {
                            field = " ";   //null时给一个空格占位
                        }
                        //拼接所有字段为一行数据
                        if (i<rowData.length-1)     //不是最后一个元素
                        {
//                            System.out.print("\""+field+"\""+",");
                            fileOutputStream.write("\""+field+"\""+",");
                        }else {
                            //最后一个元素
                            fileOutputStream.write("\""+field+"\"");
                        }
                    }
                    //创建一个新行
                    if (ite.hasNext())
                    {
                        //fileOutputStream.newLine();
                    }
                }
                fileOutputStream.newLine();     //换行，创建一个新行；
            }
            fileOutputStream.flush();
        }catch (Exception e)
        {
            flag = false;
            e.printStackTrace();
        }finally {
            try{
                fileOutputStream.close();
            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return flag;
    }

    public static boolean createCsvFileWithTwoList(List<List<Double>> rows, String filePath, String fileName) {
        List<Object[]> newRows=new ArrayList<>();
        for (List<Double> obj: rows){
            newRows.add(obj.toArray());
        }
        return createCsvFile(newRows,filePath,fileName);
    }


    public static void main(String[] args)
    {
        Object[] objects = {1,2,3,4,8,7,9,6,5,11,12};
        ArrayList<Object[]> list = new ArrayList<>();
        list.add(objects);
        list.add(objects);
        list.add(objects);
        boolean Flag=createCsvFile(list,"G:\\CSVDir","csvFile");
        if (Flag == true)
        {
            System.out.print("CSV文件创建成功！");
        }else {
            System.out.print("CSV文件创建失败！");
        }
    }


}
