package se.backgrounds.app.devicewallpaper;

/**
 * Created by stude on 2018-04-13.
 */

public class StdOutTable {

    public static void printTable(int[][] tableData, String[] columnTitle) {

        String header = "";
        for (int i = 0; i < columnTitle.length; i++) {

            if (i < columnTitle.length - 1)
                header += columnTitle[i] + "\t";
            else
                header += columnTitle[i];
        }
        //LogUtils.debug(header);
        System.err.println(header);

        //innehållet
        for (int row = 0; row < tableData.length; row++) {

            // skapa en rad
            String content = "";
            for (int col = 0; col < columnTitle.length; col++) {

                if (col < columnTitle.length - 1)
                    content += tableData[row][col] + "\t";
                else
                    content += tableData[row][col];
            }
            //lägg in raden
            //LogUtils.debug(content);
            System.err.println(content);

        }


    }

}
