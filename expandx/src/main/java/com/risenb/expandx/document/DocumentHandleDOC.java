package com.risenb.expandx.document;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.UUID;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2017/7/5
 * 描    述：
 * 修订历史：文档处理 doc
 * ================================================
 */
@SuppressWarnings("deprecation")
public class DocumentHandleDOC extends BaseDocumentHandle {

    private static DocumentHandleDOC instance;
    private HWPFDocument hwpf;
    private Range range;
    private List<Picture> pictures;
    private TableIterator tableIterator;
    private File myFile;
    private FileOutputStream output;
    private int presentPicture = 0;
    private String picturePath;
    private int screenWidth;
    private String htmlPath;


    private DocumentHandleDOC() {
    }

    public static DocumentHandleDOC getInstance() {
        if (instance == null) {
            instance = new DocumentHandleDOC();
        }
        return instance;
    }

    @Override
    public String parserFile(String path, Context context) {
        screenWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth() - 10;
        getRange(path);
        makeFile();
        readAndWrite();
        return htmlPath;
    }

    /**
     * 遍历文件
     */
    public void getRange(String path) {
        FileInputStream in = null;
        POIFSFileSystem pfs = null;
        try {
            in = new FileInputStream(path);

            pfs = new POIFSFileSystem(in);
            hwpf = new HWPFDocument(pfs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        range = hwpf.getRange();
        pictures = hwpf.getPicturesTable().getAllPictures();
        tableIterator = new TableIterator(range);

    }


    /**
     * 创建要操作的文件
     */
    @Override
    public void makeFile() {
        String sdStateString = android.os.Environment.getExternalStorageState();
        if (sdStateString.equals(android.os.Environment.MEDIA_MOUNTED)) {
            try {
                File sdFile = android.os.Environment.getExternalStorageDirectory();

                String path = sdFile.getAbsolutePath() + File.separator + TEMP_FILL_DIRECTORY;

                String temp = path + File.separator + UUID.randomUUID() + ".html";

                File dirFile = new File(path);
                if (!dirFile.exists()) {
                    dirFile.mkdir();
                }

                myFile = new File(temp);

                if (!myFile.exists()) {
                    myFile.createNewFile();
                }

                htmlPath = myFile.getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建图片
     */
    public void makePictureFile() {
        String sdString = android.os.Environment.getExternalStorageState();

        if (sdString.equals(android.os.Environment.MEDIA_MOUNTED)) {
            try {
                File picFile = android.os.Environment.getExternalStorageDirectory();

                String picPath = picFile.getAbsolutePath() + File.separator + "xiao";

                File picDirFile = new File(picPath);

                if (!picDirFile.exists()) {
                    picDirFile.mkdir();
                }
                File pictureFile = new File(picPath + File.separator + presentPicture + ".jpg");

                if (!pictureFile.exists()) {
                    pictureFile.createNewFile();
                }

                picturePath = pictureFile.getAbsolutePath();

            } catch (Exception e) {
                System.out.println("PictureFile Catch Exception");
            }
        }
    }


    /**
     * 开始转换
     */
    public void readAndWrite() {

        try {
            myFile = new File(htmlPath);
            output = new FileOutputStream(myFile);
            String head = "<html><body>";
            String tagBegin = "<p>";
            String tagEnd = "</p>";


            output.write(head.getBytes());

            int numParagraphs = range.numParagraphs();

            for (int i = 0; i < numParagraphs; i++) {
                Paragraph p = range.getParagraph(i);

                if (p.isInTable()) {
                    int temp = i;
                    if (tableIterator.hasNext()) {
                        String tableBegin = "<table style=\"border-collapse:collapse\" border=1 bordercolor=\"black\">";
                        String tableEnd = "</table>";
                        String rowBegin = "<tr>";
                        String rowEnd = "</tr>";
                        String colBegin = "<td>";
                        String colEnd = "</td>";

                        Table table = tableIterator.next();

                        output.write(tableBegin.getBytes());

                        int rows = table.numRows();

                        for (int r = 0; r < rows; r++) {
                            output.write(rowBegin.getBytes());
                            TableRow row = table.getRow(r);
                            int cols = row.numCells();
                            int rowNumParagraphs = row.numParagraphs();
                            int colsNumParagraphs = 0;
                            for (int c = 0; c < cols; c++) {
                                output.write(colBegin.getBytes());
                                TableCell cell = row.getCell(c);
                                int max = temp + cell.numParagraphs();
                                colsNumParagraphs = colsNumParagraphs + cell.numParagraphs();
                                for (int cp = temp; cp < max; cp++) {
                                    Paragraph p1 = range.getParagraph(cp);
                                    output.write(tagBegin.getBytes());
                                    writeParagraphContent(p1);
                                    output.write(tagEnd.getBytes());
                                    temp++;
                                }
                                output.write(colEnd.getBytes());
                            }
                            int max1 = temp + rowNumParagraphs;
                            for (int m = temp + colsNumParagraphs; m < max1; m++) {
                                Paragraph p2 = range.getParagraph(m);
                                temp++;
                            }
                            output.write(rowEnd.getBytes());
                        }
                        output.write(tableEnd.getBytes());
                    }
                    i = temp;
                } else {
                    output.write(tagBegin.getBytes());
                    writeParagraphContent(p);
                    output.write(tagEnd.getBytes());
                }
            }

            String end = "</body></html>";
            output.write(end.getBytes());
            output.close();
        } catch (Exception e) {
            System.out.println("readAndWrite Exception");
        }
    }

    /**
     * 解析段落内容
     */
    public void writeParagraphContent(Paragraph paragraph) {
        Paragraph p = paragraph;
        int pnumCharacterRuns = p.numCharacterRuns();

        for (int j = 0; j < pnumCharacterRuns; j++) {

            CharacterRun run = p.getCharacterRun(j);

            if (run.getPicOffset() == 0 || run.getPicOffset() >= 1000) {
                if (presentPicture < pictures.size()) {
                    writePicture();
                }
            } else {
                try {
                    String text = run.text();
                    if (text.length() >= 2 && pnumCharacterRuns < 2) {
                        output.write(text.getBytes());
                    } else {
                        int size = run.getFontSize();
                        int color = run.getColor();
                        String fontSizeBegin = "<font size=\"" + decideSize(size) + "\">";
                        String fontColorBegin = "<font color=\"" + decideColor(color) + "\">";
                        String fontEnd = "</font>";
                        String boldBegin = "<b>";
                        String boldEnd = "</b>";
                        String islaBegin = "<i>";
                        String islaEnd = "</i>";

                        output.write(fontSizeBegin.getBytes());
                        output.write(fontColorBegin.getBytes());

                        if (run.isBold()) {
                            output.write(boldBegin.getBytes());
                        }
                        if (run.isItalic()) {
                            output.write(islaBegin.getBytes());
                        }

                        output.write(text.getBytes());

                        if (run.isBold()) {
                            output.write(boldEnd.getBytes());
                        }
                        if (run.isItalic()) {
                            output.write(islaEnd.getBytes());
                        }
                        output.write(fontEnd.getBytes());
                        output.write(fontEnd.getBytes());
                    }
                } catch (Exception e) {
                    System.out.println("Write File Exception");
                }
            }
        }
    }

    /**
     * 解析图片
     */
    public void writePicture() {
        Picture picture = (Picture) pictures.get(presentPicture);

        byte[] pictureBytes = picture.getContent();

        Bitmap bitmap = BitmapFactory.decodeByteArray(pictureBytes, 0, pictureBytes.length);

        makePictureFile();
        presentPicture++;

        File myPicture = new File(picturePath);

        try {

            FileOutputStream outputPicture = new FileOutputStream(myPicture);

            outputPicture.write(pictureBytes);

            outputPicture.close();
        } catch (Exception e) {
            System.out.println("outputPicture Exception");
        }

        String imageString = "<img src=\"" + picturePath + "\"";

        if (bitmap.getWidth() > screenWidth) {
            imageString = imageString + " " + "width=\"" + screenWidth + "\"";
        }
        imageString = imageString + ">";

        try {
            output.write(imageString.getBytes());
        } catch (Exception e) {
            System.out.println("output Exception");
        }
    }


}
